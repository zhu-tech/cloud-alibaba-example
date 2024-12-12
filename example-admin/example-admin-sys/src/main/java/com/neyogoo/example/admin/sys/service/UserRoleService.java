package com.neyogoo.example.admin.sys.service;

import com.neyogoo.example.admin.sys.model.UserRole;
import com.neyogoo.example.common.core.model.Pair;
import com.neyogoo.example.common.database.mvc.service.SuperService;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 用户关联角色
 */
public interface UserRoleService extends SuperService<UserRole> {

    /**
     * 根据用户id查询
     */
    List<UserRole> listByUserId(Long userId);

    /**
     * 根据用户id查询有效的关联关系
     */
    List<UserRole> listUsableByUserId(Long userId);

    /**
     * 根据用户id查询关联的可用角色id
     */
    List<Long> listUsableRoleIdsByUserId(Long userId);

    /**
     * 根据用户id查询关联的可用角色编码
     */
    List<String> listUsableRoleCodesByUserId(Long userId);

    /**
     * 根据部门id和角色编码查询用户id（含已禁用的用户）
     */
    List<Long> listUserIdsByDeptIdAndRoleId(Long deptId, Long roleId);

    /**
     * 根据部门id和角色编码查询可用用户id
     */
    List<Long> listUsableUserIdsByDeptIdAndRoleId(Long deptId, Long roleId);

    /**
     * 根据角色id查询关联的用户id列表
     */
    List<Long> listUsableUserIdsByOrgIdAndRoleId(Long orgId, Long roleId);

    /**
     * 根据用户id列表查询关联角色名称
     *
     * @return key：用户id，value：关联角色名称
     */
    Map<Long, String> mapRoleNamesByUserIds(List<Long> userIds);

    /**
     * 更新指定部门和角色下关联的人员
     */
    boolean updateDeptRoleUnderUser(Long deptId, Long roleId, Collection<Long> userIds);

    /**
     * 更新指定人员关联的部门下角色
     *
     * @param deptRoleIds key：部门id，value：角色id列表
     */
    boolean updateUserUnderDeptRole(Long userId, List<Pair<Long, List<Long>>> deptRoleIds);

    /**
     * 新增用户关联角色
     */
    boolean saveRelations(Long userId, Collection<Long> roleIds);

    /**
     * 新增用户关联角色
     */
    boolean saveRelations(Long deptId, Long roleId, Collection<Long> userIds);

    /**
     * 根据用户id删除
     */
    boolean removeByUserId(Long userId);

    /**
     * 根据部门id删除
     */
    boolean removeByDeptId(Long deptId);

    /**
     * 根据角色id删除
     */
    boolean removeByRoleId(Long roleId);

    /**
     * 根据用户id和角色id列表删除
     */
    boolean removeByUserIdAndRoleIds(Long userId, Collection<Long> roleIds);
}
