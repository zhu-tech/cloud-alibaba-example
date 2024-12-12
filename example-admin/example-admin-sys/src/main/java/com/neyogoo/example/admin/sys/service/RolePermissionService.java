package com.neyogoo.example.admin.sys.service;

import com.neyogoo.example.admin.sys.model.RolePermission;
import com.neyogoo.example.biz.common.enumeration.sys.PermissionTypeEnum;
import com.neyogoo.example.common.database.mvc.service.SuperService;

import java.util.Collection;
import java.util.List;

/**
 * 角色关联权限
 */
public interface RolePermissionService extends SuperService<RolePermission> {

    /**
     * 根据角色id列表查询
     */
    List<RolePermission> listByRoleId(Long roleId);

    /**
     * 根据角色id列表查询
     */
    List<RolePermission> listByRoleIds(Collection<Long> roleIds);

    /**
     * 根据角色id更新
     */
    boolean updateByRoleId(Long roleId, List<RolePermission> relations);

    /**
     * 根据角色id删除
     */
    int removeByRoleId(Long roleId);

    /**
     * 根据权限id列表删除
     */
    int removeByPermissionIds(PermissionTypeEnum permissionType, Collection<Long> permissionIds);
}
