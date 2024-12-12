package com.neyogoo.example.admin.sys.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.text.CharSequenceUtil;
import com.neyogoo.example.admin.sys.dao.ResourceMapper;
import com.neyogoo.example.admin.sys.model.Resource;
import com.neyogoo.example.admin.sys.service.ResourceService;
import com.neyogoo.example.admin.sys.vo.request.permission.ResourceSaveReqVO;
import com.neyogoo.example.admin.sys.vo.request.permission.ResourceUpdateReqVO;
import com.neyogoo.example.common.core.context.ContextUtils;
import com.neyogoo.example.common.core.util.ArgumentAssert;
import com.neyogoo.example.common.database.mvc.service.impl.SuperServiceImpl;
import com.neyogoo.example.common.database.mybatis.conditions.Wraps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 资源
 */
@Slf4j
@Service
public class ResourceServiceImpl extends SuperServiceImpl<ResourceMapper, Resource> implements ResourceService {

    /**
     * 根据id查询
     */
    @Override
    public Resource getById(Serializable id) {
        if (id == null) {
            return null;
        }
        return baseMapper.selectOne(
                Wraps.<Resource>lbQ()
                        .eq(Resource::getId, id)
                        .eq(Resource::getDeleteFlag, false)
        );
    }

    /**
     * 根据编码查询
     */
    @Override
    public Resource getByCode(String resourceCode) {
        if (CharSequenceUtil.isBlank(resourceCode)) {
            return null;
        }
        return baseMapper.selectOne(
                Wraps.<Resource>lbQ()
                        .eq(Resource::getResourceCode, resourceCode)
                        .eq(Resource::getDeleteFlag, false)
        );
    }

    /**
     * 根据id列表查询可用列表
     */
    @Override
    public List<Resource> listUsableByIds(Collection<Long> ids) {
        if (CollectionUtil.isEmpty(ids)) {
            return Collections.emptyList();
        }
        return baseMapper.selectList(
                Wraps.<Resource>lbQ()
                        .in(Resource::getId, ids)
                        .eq(Resource::getUsableFlag, true)
                        .eq(Resource::getDeleteFlag, false)
                        .orderByAsc(Resource::getSort)
        );
    }

    /**
     * 根据菜单id列表查询
     */
    @Override
    public List<Resource> listUsableByMenuIds(Collection<Long> menuIds) {
        if (CollectionUtil.isEmpty(menuIds)) {
            return Collections.emptyList();
        }
        return baseMapper.selectList(
                Wraps.<Resource>lbQ()
                        .in(Resource::getMenuId, menuIds)
                        .eq(Resource::getUsableFlag, true)
                        .eq(Resource::getDeleteFlag, false)
                        .orderByAsc(Resource::getSort)
        );
    }

    /**
     * 根据菜单id列表查询
     */
    @Override
    public List<Resource> listByMenuIds(Collection<Long> menuIds) {
        if (CollectionUtil.isEmpty(menuIds)) {
            return Collections.emptyList();
        }
        return baseMapper.selectList(
                Wraps.<Resource>lbQ()
                        .in(Resource::getMenuId, menuIds)
                        .eq(Resource::getDeleteFlag, false)
                        .orderByAsc(Resource::getSort)
        );
    }

    /**
     * 根据菜单id列表查询资源id列表
     */
    @Override
    public List<Long> listIdsByMenuIds(Collection<Long> menuIds) {
        if (CollectionUtil.isEmpty(menuIds)) {
            return Collections.emptyList();
        }
        return baseMapper.selectList(
                Wraps.<Resource>lbQ()
                        .select(Resource::getId)
                        .in(Resource::getMenuId, menuIds)
                        .eq(Resource::getDeleteFlag, false)
                        .orderByAsc(Resource::getSort)
        ).stream().map(Resource::getId).collect(Collectors.toList());
    }

    /**
     * 新增
     */
    @Override
    public Resource save(ResourceSaveReqVO saveReqVO) {
        Resource model = getByCode(saveReqVO.getResourceCode());
        ArgumentAssert.isNull(model, "该资源编码已存在");
        model = saveReqVO.toModel();
        baseMapper.insert(model);
        return model;
    }

    /**
     * 修改
     */
    @Override
    public Resource update(ResourceUpdateReqVO updateReqVO) {
        Resource search = getByCode(updateReqVO.getResourceCode());
        if (search != null) {
            ArgumentAssert.equals(search.getId(), updateReqVO.getId(), "该资源编码已存在");
        } else {
            search = getById(updateReqVO.getId());
            ArgumentAssert.isTrue(search != null && !search.getDeleteFlag(), "该资源不存在");
        }
        updateReqVO.toModel(search);
        baseMapper.updateAllById(search);
        return search;
    }

    /**
     * 启用禁用
     */
    @Override
    public boolean updateUsableById(Long id, Boolean usableFlag) {
        if (id == null || usableFlag == null) {
            return false;
        }
        Resource model = getById(id);
        if (model == null || model.getUsableFlag().equals(usableFlag)) {
            return false;
        }
        Long userId = ContextUtils.getUserId();
        LocalDateTime now = LocalDateTime.now();
        model.setUsableFlag(usableFlag).setUpdateUserId(userId).setUpdateTime(now);
        return updateById(model);
    }

    /**
     * 根据菜单id列表删除
     */
    @Override
    @Transactional(rollbackFor = Throwable.class)
    public List<Long> removeByMenuIds(Collection<Long> menuIds) {
        List<Long> resourceIds = listIdsByMenuIds(menuIds);
        if (CollectionUtil.isEmpty(resourceIds)) {
            return Collections.emptyList();
        }
        removeByIds(resourceIds);
        return resourceIds;
    }

    /**
     * 根据id删除
     */
    @Override
    public boolean removeById(Serializable id) {
        if (id == null) {
            return false;
        }
        return update(
                Wraps.<Resource>lbU()
                        .set(Resource::getDeleteFlag, true)
                        .set(Resource::getUpdateUserId, ContextUtils.getUserId())
                        .set(Resource::getUpdateTime, LocalDateTime.now())
                        .eq(Resource::getId, id)
                        .eq(Resource::getDeleteFlag, false)
        );
    }

    /**
     * 根据id列表删除
     */
    @Override
    public boolean removeByIds(Collection<?> ids) {
        if (CollectionUtil.isEmpty(ids)) {
            return false;
        }
        return update(
                Wraps.<Resource>lbU()
                        .set(Resource::getDeleteFlag, true)
                        .set(Resource::getUpdateUserId, ContextUtils.getUserId())
                        .set(Resource::getUpdateTime, LocalDateTime.now())
                        .in(Resource::getId, ids)
                        .eq(Resource::getDeleteFlag, false)
        );
    }
}
