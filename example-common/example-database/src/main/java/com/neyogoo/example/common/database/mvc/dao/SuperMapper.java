package com.neyogoo.example.common.database.mvc.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 基于MP的 BaseMapper 新增了2个方法： insertBatchSomeColumn、updateAllById
 */
public interface SuperMapper<T> extends BaseMapper<T> {

    /**
     * 全量修改所有字段
     *
     * @param entity 实体
     * @return 修改数量
     */
    int updateAllById(@Param(Constants.ENTITY) T entity);

    /**
     * 批量插入所有字段
     *
     * @param entityList 实体集合
     * @return 插入数量
     */
    int insertBatchSomeColumn(List<T> entityList);
}
