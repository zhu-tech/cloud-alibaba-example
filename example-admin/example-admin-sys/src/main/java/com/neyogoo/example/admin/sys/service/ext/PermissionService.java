package com.neyogoo.example.admin.sys.service.ext;

import com.neyogoo.example.admin.sys.model.Org;
import com.neyogoo.example.admin.sys.vo.request.dept.DeptSaveReqVO;
import com.neyogoo.example.admin.sys.vo.request.dept.DeptUpdateReqVO;
import com.neyogoo.example.admin.sys.vo.request.permission.RolePermissionIdReqVO;
import com.neyogoo.example.admin.sys.vo.response.dept.DeptGetRespVO;
import com.neyogoo.example.admin.sys.vo.response.permission.RoleMenuRespVO;
import com.neyogoo.example.admin.sys.vo.response.permission.RolePermissionIdRespVO;
import com.neyogoo.example.common.core.model.Pair;

import java.util.Collection;
import java.util.List;

/**
 * 角色权限
 */
public interface PermissionService {

    /**
     * 根据用户id查询权限
     */
    List<RoleMenuRespVO> listPermissionByUserId(Long userId, Org org);

    /**
     * 根据用户id查询角色名称
     */
    List<String> listRoleNamesByUserId(Long userId);

    /**
     * 根据角色id列表查询权限列表
     */
    List<RoleMenuRespVO> listUsablePermissionByRoleIds(Collection<Long> roleIds);

    /**
     * 查询权限列表
     */
    List<RoleMenuRespVO> listPermission();

    /**
     * 查询可用权限列表
     */
    List<RoleMenuRespVO> listUsablePermission();

    /**
     * 根据角色id查询权限id列表
     */
    RolePermissionIdRespVO listPermissionIdsByRoleId(Long roleId);

    /**
     * 根据部门id和角色编码查询人员的id和名字
     */
    List<Pair<Long, String>> listUserIdNameByDeptIdAndRoleCode(Long deptId, String roleCode);

    /**
     * 根据机构id查询机构负责人id列表
     */
    List<Long> listOrgAdminUserIds(Long orgId);

    /**
     * 查询部门信息
     */
    DeptGetRespVO getDeptInfo(Long id);

    /**
     * 更新角色关联的权限列表
     */
    boolean saveRolePermission(RolePermissionIdReqVO reqVO);

    /**
     * 新增部门
     */
    Long saveDeptInfo(DeptSaveReqVO reqVO);

    /**
     * 修改部门
     */
    boolean updateDeptInfo(DeptUpdateReqVO reqVO);

    /**
     * 根据角色id删除角色
     */
    boolean removeRoleByRoleId(Long roleId);

    /**
     * 根据菜单id删除菜单（级联删除下级菜单和资源）
     */
    boolean removeMenuByMenuId(Long menuId);

    /**
     * 根据部门id删除部门
     */
    boolean removeDeptByDeptId(Long deptId);
}
