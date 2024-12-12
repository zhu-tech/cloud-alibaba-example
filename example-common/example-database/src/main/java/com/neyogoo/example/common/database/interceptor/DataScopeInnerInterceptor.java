package com.neyogoo.example.common.database.interceptor;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.toolkit.PluginUtils;
import com.baomidou.mybatisplus.extension.plugins.inner.InnerInterceptor;
import com.google.common.collect.Lists;
import com.neyogoo.example.common.core.constant.BasicConstant;
import com.neyogoo.example.common.core.context.ContextUtils;
import com.neyogoo.example.common.core.enumeration.DataScopeTypeEnum;
import com.neyogoo.example.common.core.model.DataScope;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Slf4j
@AllArgsConstructor
public class DataScopeInnerInterceptor implements InnerInterceptor {

    /**
     * 查找参数是否包括 DataScope 对象
     */
    protected Optional<DataScope> findDataScope(Object parameterObj) {
        if (parameterObj == null) {
            return Optional.empty();
        }
        if (parameterObj instanceof DataScope) {
            return Optional.of((DataScope) parameterObj);
        }
        if (parameterObj instanceof HashMap) {
            if (((HashMap) parameterObj).containsKey("dataScope")) {
                return Optional.of((DataScope) ((HashMap) parameterObj).get("dataScope"));
            }
        }
        return Optional.empty();
    }

    @Override
    public void beforeQuery(Executor executor, MappedStatement ms, Object parameter, RowBounds rowBounds,
                            ResultHandler resultHandler, BoundSql boundSql) {
        DataScope dataScope = findDataScope(parameter).orElse(null);
        if (dataScope == null) {
            return;
        }

        String originalSql = boundSql.getSql();

        String deptFieldName = dataScope.getDeptFieldName();
        String userFieldName = dataScope.getUserFieldName();
        String orgFieldName = dataScope.getOrgFieldName();

        List<Long> defaultList = Lists.newArrayList(-1L);

        Long userId = Optional.ofNullable(dataScope.getUserId()).orElse(ContextUtils.getUserId());
        Long orgId = Optional.ofNullable(dataScope.getOrgId()).orElse(ContextUtils.getOrgId());
        List<Long> orgIds = CollectionUtil.isEmpty(dataScope.getOrgIds())
                ? defaultList : dataScope.getOrgIds();
        Long deptId = Optional.ofNullable(dataScope.getDeptId()).orElse(BasicConstant.DEFAULT_ID);
        List<Long> deptIds = CollectionUtil.isEmpty(dataScope.getDeptIds())
                ? defaultList : dataScope.getDeptIds();

        DataScopeTypeEnum dsType = Optional.ofNullable(dataScope.getDsType()).orElse(DataScopeTypeEnum.Self);

        // 查全部
        if (DataScopeTypeEnum.All.eq(dsType)) {
            return;
        }
        // 查多个机构
        if (DataScopeTypeEnum.Orgs.eq(dsType)) {
            originalSql = "select * from (" + originalSql + ") temp_data_scope where temp_data_scope."
                    + orgFieldName + " in (" + StringUtils.join(orgIds, ",") + ")";
        }
        // 查单个机构
        if (DataScopeTypeEnum.Org.eq(dsType)) {
            originalSql = "select * from (" + originalSql + ") temp_data_scope where temp_data_scope."
                    + orgFieldName + " = " + orgId + "";
        }
        // 查多个部门
        if (DataScopeTypeEnum.Depts.eq(dsType)) {
            originalSql = "select * from (" + originalSql + ") temp_data_scope where temp_data_scope."
                    + deptFieldName + " in (" + StringUtils.join(deptIds, ",") + ")";
        }
        // 查单个部门
        if (DataScopeTypeEnum.Dept.eq(dsType)) {
            originalSql = "select * from (" + originalSql + ") temp_data_scope where temp_data_scope."
                    + deptFieldName + " = " + deptId + "";
        }
        // 查个人
        if (DataScopeTypeEnum.Self.eq(dsType)) {
            originalSql = "select * from (" + originalSql + ") temp_data_scope where temp_data_scope."
                    + userFieldName + " = " + userId;
        }
        PluginUtils.MPBoundSql mpBoundSql = PluginUtils.mpBoundSql(boundSql);
        mpBoundSql.sql(originalSql);
    }
}
