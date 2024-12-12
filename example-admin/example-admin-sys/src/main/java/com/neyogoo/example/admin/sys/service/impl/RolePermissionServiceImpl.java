package com.neyogoo.example.admin.sys.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.neyogoo.example.admin.sys.dao.RolePermissionMapper;
import com.neyogoo.example.admin.sys.model.RolePermission;
import com.neyogoo.example.admin.sys.service.RolePermissionService;
import com.neyogoo.example.biz.common.enumeration.sys.PermissionTypeEnum;
import com.neyogoo.example.common.database.mvc.service.impl.SuperServiceImpl;
import com.neyogoo.example.common.database.mybatis.conditions.Wraps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * 角色和权限关联关系
 */
@Slf4j
@Service
public class RolePermissionServiceImpl extends SuperServiceImpl<RolePermissionMapper, RolePermission>
        implements RolePermissionService {

    /**
     * 根据角色id查询
     */
    @Override
    public List<RolePermission> listByRoleId(Long roleId) {
        if (roleId == null) {
            return Collections.emptyList();
        }
        return baseMapper.selectList(
                Wraps.<RolePermission>lbQ()
                        .eq(RolePermission::getRoleId, roleId)
        );
    }

    /**
     * 根据角色id列表查询
     */
    @Override
    public List<RolePermission> listByRoleIds(Collection<Long> roleIds) {
        if (CollectionUtil.isEmpty(roleIds)) {
            return Collections.emptyList();
        }
        return baseMapper.selectList(
                Wraps.<RolePermission>lbQ().in(RolePermission::getRoleId, roleIds)
        );
    }

    /**
     * 根据角色id更新
     */
    @Override
    @Transactional(rollbackFor = Throwable.class)
    public boolean updateByRoleId(Long roleId, List<RolePermission> relations) {
        removeByRoleId(roleId);
        saveBatch(relations);
        return true;
    }

    /**
     * 根据角色id删除
     */
    @Override
    public int removeByRoleId(Long roleId) {
        if (roleId == null) {
            return 0;
        }
        return baseMapper.delete(Wraps.<RolePermission>lbQ().eq(RolePermission::getRoleId, roleId));
    }

    /**
     * 根据权限id列表删除
     */
    @Override
    public int removeByPermissionIds(PermissionTypeEnum permissionType, Collection<Long> permissionIds) {
        if (permissionType == null || CollectionUtil.isEmpty(permissionIds)) {
            return 0;
        }
        return baseMapper.delete(
                Wraps.<RolePermission>lbQ()
                        .in(RolePermission::getPermissionId, permissionIds)
                        .eq(RolePermission::getPermissionType, permissionType)
        );
    }
}
