package com.neyogoo.example.admin.sys.dao;

import com.neyogoo.example.admin.sys.model.RolePermission;
import com.neyogoo.example.common.database.mvc.dao.SuperMapper;
import org.springframework.stereotype.Repository;

/**
 * 角色关联权限
 */
@Repository
public interface RolePermissionMapper extends SuperMapper<RolePermission> {

}
