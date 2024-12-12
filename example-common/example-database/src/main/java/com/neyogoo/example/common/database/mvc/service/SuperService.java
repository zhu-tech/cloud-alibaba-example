package com.neyogoo.example.common.database.mvc.service;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.neyogoo.example.common.database.mvc.dao.SuperMapper;

import java.util.List;

/**
 * 基于MP的 IService 新增了2个方法： saveBatchSomeColumn、updateAllById
 * 其中：
 * 1，updateAllById 执行后，会清除缓存
 * 2，saveBatchSomeColumn 批量插入
 *
 * @param <T> 实体
 */
@SuppressWarnings("ALL")
public interface SuperService<T> extends IService<T> {

    /**
     * 获取实体的类型
     */
    @Override
    Class<T> getEntityClass();

    /**
     * 批量保存数据
     */
    default boolean saveBatchSomeColumn(List<T> entityList) {
        if (CollectionUtil.isEmpty(entityList)) {
            return true;
        }
        return SqlHelper.retBool(((SuperMapper) getBaseMapper()).insertBatchSomeColumn(entityList));
    }

    /**
     * 根据 id 修改 entity 的所有字段
     */
    boolean updateAllById(T entity);

}
