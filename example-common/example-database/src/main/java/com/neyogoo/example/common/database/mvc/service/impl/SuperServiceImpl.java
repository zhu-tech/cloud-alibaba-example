package com.neyogoo.example.common.database.mvc.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.neyogoo.example.common.core.exception.BizException;
import com.neyogoo.example.common.core.model.R;
import com.neyogoo.example.common.database.mvc.dao.SuperMapper;
import com.neyogoo.example.common.database.mvc.service.SuperService;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.ParameterizedType;

import static com.neyogoo.example.common.core.exception.code.ExceptionCode.INTERNAL_SERVER_ERROR;

/**
 * 不含缓存的Service实现
 * <p>
 * <p>
 * 2，removeById：重写 ServiceImpl 类的方法，删除db
 * 3，removeByIds：重写 ServiceImpl 类的方法，删除db
 * 4，updateAllById： 新增的方法： 修改数据（所有字段）
 * 5，updateById：重写 ServiceImpl 类的方法，修改db后
 *
 * @param <M> Mapper
 * @param <T> 实体
 */
public class SuperServiceImpl<M extends SuperMapper<T>, T> extends ServiceImpl<M, T> implements SuperService<T> {

    private Class<T> entityClass = null;

    public SuperMapper getSuperMapper() {
        if (baseMapper instanceof SuperMapper) {
            return baseMapper;
        }
        throw BizException.wrap(INTERNAL_SERVER_ERROR, "Mapper类转换异常");
    }

    @Override
    public Class<T> getEntityClass() {
        if (entityClass == null) {
            this.entityClass = (Class) ((ParameterizedType) this.getClass().getGenericSuperclass())
                    .getActualTypeArguments()[1];
        }
        return this.entityClass;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean save(T model) {
        R<Boolean> result = handlerSave(model);
        if (result.getDefExec()) {
            return super.save(model);
        }
        return result.getData();
    }

    /**
     * 处理新增相关处理
     *
     * @param model 实体
     * @return 是否成功
     */
    protected R<Boolean> handlerSave(T model) {
        return R.successDef();
    }

    /**
     * 处理修改相关处理
     *
     * @param model 实体
     * @return 是否成功
     */
    protected R<Boolean> handlerUpdateAllById(T model) {
        return R.successDef();
    }

    /**
     * 处理修改相关处理
     *
     * @param model 实体
     * @return 是否成功
     */
    protected R<Boolean> handlerUpdateById(T model) {
        return R.successDef();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateAllById(T model) {
        R<Boolean> result = handlerUpdateAllById(model);
        if (result.getDefExec()) {
            return SqlHelper.retBool(getSuperMapper().updateAllById(model));
        }
        return result.getData();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateById(T model) {
        R<Boolean> result = handlerUpdateById(model);
        if (result.getDefExec()) {
            return super.updateById(model);
        }
        return result.getData();
    }
}
