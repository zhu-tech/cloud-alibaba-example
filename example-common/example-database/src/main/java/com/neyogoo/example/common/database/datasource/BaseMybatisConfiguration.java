package com.neyogoo.example.common.database.datasource;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ReflectUtil;
import com.baidu.fsg.uid.UidGenerator;
import com.baidu.fsg.uid.buffer.RejectedPutBufferHandler;
import com.baidu.fsg.uid.buffer.RejectedTakeBufferHandler;
import com.baidu.fsg.uid.impl.CachedUidGenerator;
import com.baidu.fsg.uid.impl.DefaultUidGenerator;
import com.baidu.fsg.uid.impl.HutoolUidGenerator;
import com.baidu.fsg.uid.worker.DisposableWorkerIdAssigner;
import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.BlockAttackInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.IllegalSQLInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.InnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.neyogoo.example.common.database.injector.CustomSqlInjector;
import com.neyogoo.example.common.database.interceptor.DataScopeInnerInterceptor;
import com.neyogoo.example.common.database.mybatis.typehandler.EncryptTypeHandler;
import com.neyogoo.example.common.database.mybatis.typehandler.FullLikeTypeHandler;
import com.neyogoo.example.common.database.mybatis.typehandler.LeftLikeTypeHandler;
import com.neyogoo.example.common.database.mybatis.typehandler.RightLikeTypeHandler;
import com.neyogoo.example.common.database.properties.DatabaseProperties;
import com.neyogoo.example.common.uid.dao.WorkerNodeDao;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.mapping.DatabaseIdProvider;
import org.apache.ibatis.mapping.VendorDatabaseIdProvider;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import static com.neyogoo.example.common.core.constant.BasicConstant.DATABASE_PREFIX;
import static com.neyogoo.example.common.core.constant.BasicConstant.MAPPER_SCAN_LOCATION;

@Slf4j
@Configuration
@EnableConfigurationProperties({DatabaseProperties.class})
@MapperScan(basePackages = {MAPPER_SCAN_LOCATION}, annotationClass = Repository.class)
public class BaseMybatisConfiguration {

    protected final DatabaseProperties databaseProperties;

    public BaseMybatisConfiguration(final DatabaseProperties databaseProperties) {
        this.databaseProperties = databaseProperties;
    }

    @Bean
    @Order(5)
    @ConditionalOnMissingBean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();

        List<InnerInterceptor> beforeInnerInterceptor = getPaginationBeforeInnerInterceptor();
        if (!beforeInnerInterceptor.isEmpty()) {
            beforeInnerInterceptor.forEach(interceptor::addInnerInterceptor);
        }

        // 分页插件
        PaginationInnerInterceptor paginationInterceptor = new PaginationInnerInterceptor();
        // 单页分页条数限制
        paginationInterceptor.setMaxLimit(databaseProperties.getMaxLimit());
        // 数据库类型
        paginationInterceptor.setDbType(DbType.MYSQL);
        paginationInterceptor.setOptimizeJoin(false);
        interceptor.addInnerInterceptor(paginationInterceptor);

        List<InnerInterceptor> afterInnerInterceptor = getPaginationAfterInnerInterceptor();
        if (!afterInnerInterceptor.isEmpty()) {
            afterInnerInterceptor.forEach(interceptor::addInnerInterceptor);
        }

        // 防止全表更新与删除插件
        if (databaseProperties.getIsBlockAttack()) {
            interceptor.addInnerInterceptor(new BlockAttackInnerInterceptor());
        }
        // sql性能规范插件
        if (databaseProperties.getIsIllegalSql()) {
            interceptor.addInnerInterceptor(new IllegalSQLInnerInterceptor());
        }
        // 乐观锁插件
        if (databaseProperties.getIsOptimisticLocker()) {
            interceptor.addInnerInterceptor(new OptimisticLockerInnerInterceptor());
        }

        return interceptor;
    }


    /**
     * 数据库配置
     */
    @Bean
    public DatabaseIdProvider getDatabaseIdProvider() {
        DatabaseIdProvider databaseIdProvider = new VendorDatabaseIdProvider();
        Properties properties = new Properties();
        properties.setProperty("MySQL", DbType.MYSQL.getDb());
        databaseIdProvider.setProperties(properties);
        return databaseIdProvider;
    }

    /**
     * 分页拦截器之后的插件
     */
    protected List<InnerInterceptor> getPaginationAfterInnerInterceptor() {
        return new ArrayList<>();
    }

    /**
     * 分页拦截器之前的插件
     */
    protected List<InnerInterceptor> getPaginationBeforeInnerInterceptor() {
        List<InnerInterceptor> list = new ArrayList<>();
        // 数据权限插件
        if (databaseProperties.getIsDataScope()) {
            list.add(new DataScopeInnerInterceptor());
        }
        return list;
    }

    /**
     * Mybatis Plus 注入器
     */
    @Bean("myMetaObjectHandler")
    @ConditionalOnMissingBean
    public MetaObjectHandler getMyMetaObjectHandler() {
        return new CustomMetaObjectHandler();
    }


    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnExpression("'Default'.equals('${" + DATABASE_PREFIX + ".id-type:Default}') "
            + "|| 'Cache'.equals('${" + DATABASE_PREFIX + ".id-type:Default}')")
    public DisposableWorkerIdAssigner disposableWorkerIdAssigner(WorkerNodeDao workerNodeDao) {
        return new DisposableWorkerIdAssigner(workerNodeDao);
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = DATABASE_PREFIX, name = "id-type", havingValue = "Default",
            matchIfMissing = true)
    public UidGenerator getDefaultUidGenerator(DisposableWorkerIdAssigner disposableWorkerIdAssigner) {
        log.info("使用 DefaultUidGenerator");
        DefaultUidGenerator uidGenerator = new DefaultUidGenerator();
        BeanUtil.copyProperties(databaseProperties.getDefaultId(), uidGenerator);
        uidGenerator.setWorkerIdAssigner(disposableWorkerIdAssigner);
        return uidGenerator;
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = DATABASE_PREFIX, name = "id-type", havingValue = "Cache")
    public UidGenerator getCacheUidGenerator(DisposableWorkerIdAssigner disposableWorkerIdAssigner) {
        log.info("使用 CacheUidGenerator");
        CachedUidGenerator uidGenerator = new CachedUidGenerator();
        DatabaseProperties.CacheId cacheId = databaseProperties.getCacheId();
        BeanUtil.copyProperties(cacheId, uidGenerator);
        if (cacheId.getRejectedPutBufferHandlerClass() != null) {
            RejectedPutBufferHandler rejectedPutBufferHandler = ReflectUtil.newInstance(
                    cacheId.getRejectedPutBufferHandlerClass()
            );
            uidGenerator.setRejectedPutBufferHandler(rejectedPutBufferHandler);
        }
        if (cacheId.getRejectedTakeBufferHandlerClass() != null) {
            RejectedTakeBufferHandler rejectedTakeBufferHandler = ReflectUtil.newInstance(
                    cacheId.getRejectedTakeBufferHandlerClass()
            );
            uidGenerator.setRejectedTakeBufferHandler(rejectedTakeBufferHandler);
        }
        uidGenerator.setWorkerIdAssigner(disposableWorkerIdAssigner);
        return uidGenerator;
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = DATABASE_PREFIX, name = "id-type", havingValue = "Hutool")
    public UidGenerator getHutoolUidGenerator() {
        log.info("使用 HutoolUidGenerator");
        DatabaseProperties.HutoolId id = databaseProperties.getHutoolId();
        return new HutoolUidGenerator(id.getWorkerId(), id.getDataCenterId());
    }

    /**
     * 敏感字段加解密时使用
     */
    @Bean
    public EncryptTypeHandler getEncryptTypeHandler() {
        return new EncryptTypeHandler(databaseProperties.getEncryptEnabled(), databaseProperties.getEncryptKey());
    }

    /**
     * 用于左模糊查询时使用 例如：and name like #{name, typeHandler=leftLike}
     */
    @Bean
    public LeftLikeTypeHandler getLeftLikeTypeHandler() {
        return new LeftLikeTypeHandler();
    }

    /**
     * 用于右模糊查询时使用，例如：and name like #{name, typeHandler=rightLike}
     */
    @Bean
    public RightLikeTypeHandler getRightLikeTypeHandler() {
        return new RightLikeTypeHandler();
    }

    /**
     * 用于全模糊查询时使用，例如：and name like #{name, typeHandler=fullLike}
     */
    @Bean
    public FullLikeTypeHandler getFullLikeTypeHandler() {
        return new FullLikeTypeHandler();
    }


    @Bean
    @ConditionalOnMissingBean
    public CustomSqlInjector getMySqlInjector() {
        return new CustomSqlInjector();
    }

}
