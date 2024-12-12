package com.neyogoo.example.admin.sys.dao;

import com.neyogoo.example.admin.sys.model.UserRole;
import com.neyogoo.example.common.core.model.Pair;
import com.neyogoo.example.common.database.mvc.dao.SuperMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 用户关联角色
 */
@Repository
public interface UserRoleMapper extends SuperMapper<UserRole> {

    /**
     * 根据用户id查询有效的关联关系
     */
    List<UserRole> listUsableByUserId(@Param("userId") Long userId);

    /**
     * 根据用户id查询关联的可用角色id
     */
    List<Long> listUsableRoleIdsByUserId(@Param("userId") Long userId);

    /**
     * 根据用户id查询关联的可用角色编码
     */
    List<String> listUsableRoleCodesByUserId(@Param("userId") Long userId);

    /**
     * 根据用户id查询关联的可用权限编码
     */
    List<String> listUsablePermissionCodesByUserId(@Param("userId") Long userId);

    /**
     * 根据部门id和角色编码查询用户id（含已禁用的用户）
     */
    List<Long> listUserIdsByDeptIdAndRoleId(@Param("deptId") Long deptId, @Param("roleId") Long roleId);

    /**
     * 根据部门id和角色编码查询用户id
     */
    List<Long> listUsableUserIdsByDeptIdAndRoleId(@Param("deptId") Long deptId, @Param("roleId") Long roleId);

    /**
     * 根据机构id和角色id查询用户id
     */
    List<Long> listUsableUserIdsByOrgIdAndRoleId(@Param("orgId") Long orgId, @Param("roleId") Long roleId);

    /**
     * 根据用户id列表查询关联角色名称
     */
    List<Pair<Long, String>> listRoleNamesByUserIds(@Param("userIds") List<Long> userIds);
}
