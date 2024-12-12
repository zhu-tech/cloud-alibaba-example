package com.neyogoo.example.admin.sys.service.impl.ext;

import cn.hutool.core.collection.CollUtil;
import com.neyogoo.example.admin.sys.model.Dept;
import com.neyogoo.example.admin.sys.model.Menu;
import com.neyogoo.example.admin.sys.model.Org;
import com.neyogoo.example.admin.sys.model.Resource;
import com.neyogoo.example.admin.sys.model.Role;
import com.neyogoo.example.admin.sys.model.RolePermission;
import com.neyogoo.example.admin.sys.model.UserDept;
import com.neyogoo.example.admin.sys.model.UserRole;
import com.neyogoo.example.admin.sys.service.DeptService;
import com.neyogoo.example.admin.sys.service.MenuService;
import com.neyogoo.example.admin.sys.service.ResourceService;
import com.neyogoo.example.admin.sys.service.RolePermissionService;
import com.neyogoo.example.admin.sys.service.RoleService;
import com.neyogoo.example.admin.sys.service.UserDeptService;
import com.neyogoo.example.admin.sys.service.UserRoleService;
import com.neyogoo.example.admin.sys.service.UserService;
import com.neyogoo.example.admin.sys.service.ext.PermissionService;
import com.neyogoo.example.admin.sys.vo.request.dept.DeptSaveReqVO;
import com.neyogoo.example.admin.sys.vo.request.dept.DeptUpdateReqVO;
import com.neyogoo.example.admin.sys.vo.request.permission.RolePermissionIdReqVO;
import com.neyogoo.example.admin.sys.vo.response.dept.DeptGetRespVO;
import com.neyogoo.example.admin.sys.vo.response.permission.RoleMenuRespVO;
import com.neyogoo.example.admin.sys.vo.response.permission.RolePermissionIdRespVO;
import com.neyogoo.example.biz.common.constant.BizRoleConstant;
import com.neyogoo.example.biz.common.enumeration.sys.PermissionTypeEnum;
import com.neyogoo.example.common.core.context.ContextUtils;
import com.neyogoo.example.common.core.model.Pair;
import com.neyogoo.example.common.core.util.ArgumentAssert;
import com.neyogoo.example.common.core.util.EntityUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 用户权限
 */
@Slf4j
@Service
public class PermissionServiceImpl implements PermissionService {

    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private MenuService menuService;
    @Autowired
    private DeptService deptService;
    @Autowired
    private ResourceService resourceService;
    @Autowired
    private UserRoleService userRoleService;
    @Autowired
    private UserDeptService userDeptService;
    @Autowired
    private RolePermissionService rolePermissionService;

    /**
     * 根据用户id查询权限
     */
    @Override
    public List<RoleMenuRespVO> listPermissionByUserId(Long userId, Org org) {
        List<Long> roleIds = userRoleService.listUsableRoleIdsByUserId(userId);
        return listUsablePermissionByRoleIds(roleIds);
    }

    /**
     * 根据用户id查询角色名称
     */
    @Override
    public List<String> listRoleNamesByUserId(Long userId) {
        Map<Long, String> map = roleService.mapNamesByIds(userRoleService.listUsableRoleIdsByUserId(userId));
        return map.values().stream().distinct().collect(Collectors.toList());
    }

    /**
     * 根据角色id列表查询权限列表
     */
    @Override
    public List<RoleMenuRespVO> listUsablePermissionByRoleIds(Collection<Long> roleIds) {
        if (CollUtil.isEmpty(roleIds)) {
            return Collections.emptyList();
        }
        List<RolePermission> relations = rolePermissionService.listByRoleIds(roleIds);
        if (CollUtil.isEmpty(relations)) {
            return Collections.emptyList();
        }
        List<Menu> menus = menuService.listUsableByIds(
                RolePermission.fetchPermissionIds(relations, PermissionTypeEnum.M)
        );
        List<Resource> resources = resourceService.listUsableByIds(
                RolePermission.fetchPermissionIds(relations, PermissionTypeEnum.R)
        );
        return RoleMenuRespVO.fromPermission(menus, resources);
    }

    /**
     * 查询权限列表
     */
    @Override
    public List<RoleMenuRespVO> listPermission() {
        List<Menu> menus = menuService.list();
        List<Resource> resources = resourceService.listByMenuIds(
                EntityUtils.toList(menus, Menu::getId)
        );
        return RoleMenuRespVO.fromPermission(menus, resources);
    }

    /**
     * 查询可用权限列表
     */
    @Override
    public List<RoleMenuRespVO> listUsablePermission() {
        List<Menu> menus = menuService.listUsable();
        List<Resource> resources = resourceService.listUsableByMenuIds(
                EntityUtils.toList(menus, Menu::getId)
        );
        return RoleMenuRespVO.fromPermission(menus, resources);
    }

    /**
     * 根据角色id查询权限id列表
     */
    @Override
    public RolePermissionIdRespVO listPermissionIdsByRoleId(Long roleId) {
        RolePermissionIdRespVO respVO = new RolePermissionIdRespVO();
        if (roleId == null) {
            return respVO;
        }
        List<RolePermission> relations = rolePermissionService.listByRoleId(roleId);
        respVO.setMenuIds(RolePermission.fetchPermissionIds(relations, PermissionTypeEnum.M));
        respVO.setResourceIds(RolePermission.fetchPermissionIds(relations, PermissionTypeEnum.R));
        return respVO;
    }

    /**
     * 根据部门id和角色编码查询人员的id和名字
     */
    @Override
    public List<Pair<Long, String>> listUserIdNameByDeptIdAndRoleCode(Long deptId, String roleCode) {
        // 主负责人
        Role majorAdminRole = roleService.getByCode(roleCode);
        if (majorAdminRole == null || !majorAdminRole.getUsableFlag()) {
            return Collections.emptyList();
        }
        List<Long> userIds = userRoleService.listUsableUserIdsByDeptIdAndRoleId(deptId, majorAdminRole.getId());
        return Pair.fromMap(userService.mapNamesByIds(userIds));
    }

    /**
     * 根据机构id查询机构负责人id列表
     */
    @Override
    public List<Long> listOrgAdminUserIds(Long orgId) {
        Role orgAdminRole = roleService.getByCode(BizRoleConstant.ORG_ADMIN_ROLE_CODE);
        return userRoleService.listUsableUserIdsByOrgIdAndRoleId(orgId, orgAdminRole.getId());
    }

    /**
     * 查询部门信息
     */
    @Override
    public DeptGetRespVO getDeptInfo(Long id) {
        Dept dept = deptService.getById(id);
        if (dept == null) {
            return null;
        }
        DeptGetRespVO respVO = DeptGetRespVO.fromModel(dept);
        respVO.setMajorAdminUsers(listUserIdNameByDeptIdAndRoleCode(id, BizRoleConstant.DEPT_MAJOR_ADMIN_ROLE_CODE));
        respVO.setMinorAdminUsers(listUserIdNameByDeptIdAndRoleCode(id, BizRoleConstant.DEPT_MINOR_ADMIN_ROLE_CODE));
        return respVO;
    }

    /**
     * 更新角色关联的权限列表
     */
    @Override
    public boolean saveRolePermission(RolePermissionIdReqVO reqVO) {
        return rolePermissionService.updateByRoleId(reqVO.getRoleId(), reqVO.toPermissionList());
    }

    /**
     * 修改部门
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long saveDeptInfo(DeptSaveReqVO reqVO) {
        Long deptId = deptService.save(reqVO);

        List<UserDept> userDepts = new ArrayList<>();
        List<UserRole> userRoles = new ArrayList<>();

        // 部门主负责人
        if (CollUtil.isNotEmpty(reqVO.getMajorAdminUserIds())) {
            Role majorAdminRole = roleService.getByCode(BizRoleConstant.DEPT_MAJOR_ADMIN_ROLE_CODE);
            ArgumentAssert.notNull(majorAdminRole, "主负责人角色不存在");
            ArgumentAssert.isTrue(majorAdminRole.getUsableFlag(), "主负责人角色已禁用");
            userDepts.addAll(EntityUtils.toList(
                    reqVO.getMajorAdminUserIds(),
                    o -> UserDept.builder().userId(o).deptId(deptId).orgId(reqVO.getOrgId()).build()
            ));
            userRoles.addAll(EntityUtils.toList(
                    reqVO.getMajorAdminUserIds(),
                    o -> UserRole.builder().userId(o).deptId(deptId).roleId(majorAdminRole.getId()).build()
            ));
        }

        // 部门副负责人
        if (CollUtil.isNotEmpty(reqVO.getMinorAdminUserIds())) {
            Role minorAdminRole = roleService.getByCode(BizRoleConstant.DEPT_MINOR_ADMIN_ROLE_CODE);
            ArgumentAssert.notNull(minorAdminRole, "副负责人角色不存在");
            ArgumentAssert.isTrue(minorAdminRole.getUsableFlag(), "副负责人角色已禁用");
            userDepts.addAll(EntityUtils.toList(
                    reqVO.getMinorAdminUserIds(),
                    o -> UserDept.builder().userId(o).deptId(deptId).orgId(reqVO.getOrgId()).build()
            ));
            userRoles.addAll(EntityUtils.toList(
                    reqVO.getMinorAdminUserIds(),
                    o -> UserRole.builder().userId(o).deptId(deptId).roleId(minorAdminRole.getId()).build()
            ));
        }

        if (CollUtil.isNotEmpty(userDepts)) {
            userDeptService.saveBatch(userDepts);
        }
        if (CollUtil.isNotEmpty(userRoles)) {
            userRoleService.saveBatch(userRoles);
        }
        return deptId;
    }

    /**
     * 修改部门
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateDeptInfo(DeptUpdateReqVO reqVO) {
        // 更新部门信息
        deptService.update(reqVO);

        // 已关联此部门的人
        Set<Long> deptUserIds = new HashSet<>(userDeptService.listUsableUserIdsByDeptId(reqVO.getId()));
        Set<Long> newUserIds = new HashSet<>();
        // 部门主负责人
        Role majorAdminRole = roleService.getByCode(BizRoleConstant.DEPT_MAJOR_ADMIN_ROLE_CODE);
        ArgumentAssert.notNull(majorAdminRole, "主负责人角色不存在");
        ArgumentAssert.isTrue(majorAdminRole.getUsableFlag(), "主负责人角色已禁用");
        if (CollUtil.isNotEmpty(reqVO.getMajorAdminUserIds())) {
            // 尚未在此部门的，关联到此部门中
            newUserIds.addAll(reqVO.getMajorAdminUserIds().stream().filter(o -> !deptUserIds.contains(o))
                    .collect(Collectors.toSet()));
        }

        // 部门副负责人
        Role minorAdminRole = roleService.getByCode(BizRoleConstant.DEPT_MINOR_ADMIN_ROLE_CODE);
        ArgumentAssert.notNull(minorAdminRole, "副负责人角色不存在");
        ArgumentAssert.isTrue(minorAdminRole.getUsableFlag(), "副负责人角色已禁用");
        if (CollUtil.isNotEmpty(reqVO.getMinorAdminUserIds())) {
            // 尚未在此部门的，关联到此部门中
            newUserIds.addAll(reqVO.getMinorAdminUserIds().stream().filter(o -> !deptUserIds.contains(o))
                    .collect(Collectors.toSet()));

        }

        // 保存人和部门关联关系
        if (CollUtil.isNotEmpty(newUserIds)) {
            userDeptService.saveBatch(
                    newUserIds.stream()
                            .map(o -> UserDept.builder().userId(o).deptId(reqVO.getId()).orgId(reqVO.getOrgId())
                                    .build())
                            .collect(Collectors.toList())
            );
        }
        // 更新主负责人
        userRoleService.updateDeptRoleUnderUser(reqVO.getId(), majorAdminRole.getId(), reqVO.getMajorAdminUserIds());
        // 更新副负责人
        userRoleService.updateDeptRoleUnderUser(reqVO.getId(), minorAdminRole.getId(), reqVO.getMinorAdminUserIds());
        return true;
    }

    /**
     * 根据角色id删除角色
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeRoleByRoleId(Long roleId) {
        Role role = roleService.getById(roleId);
        if (role == null) {
            return false;
        }
        ArgumentAssert.isFalse(BizRoleConstant.BUILD_IN_ROLE_CODES.contains(role.getRoleCode()),
                "内置角色无法删除");
        roleService.removeById(roleId);
        rolePermissionService.removeByRoleId(roleId);
        userRoleService.removeByRoleId(roleId);
        return true;
    }

    /**
     * 根据菜单id删除菜单（级联删除下级菜单和资源）
     */
    @Override
    @Transactional(rollbackFor = Throwable.class)
    public boolean removeMenuByMenuId(Long menuId) {
        List<Long> menuIds = menuService.removeByIdWithChild(menuId);
        if (CollUtil.isEmpty(menuIds)) {
            return false;
        }
        List<Long> resourceIds = resourceService.removeByMenuIds(menuIds);

        rolePermissionService.removeByPermissionIds(PermissionTypeEnum.M, menuIds);
        rolePermissionService.removeByPermissionIds(PermissionTypeEnum.R, resourceIds);

        return true;
    }

    /**
     * 根据部门id删除部门
     */
    @Override
    public boolean removeDeptByDeptId(Long deptId) {
        Dept dept = deptService.getById(deptId);
        if (dept == null || !dept.getOrgId().equals(ContextUtils.getOrgId())) {
            return false;
        }
        ArgumentAssert.isTrue(CollUtil.isEmpty(userDeptService.listUsableUserIdsByDeptId(deptId)),
                "该部门下存在人员，不可删除");
        deptService.removeById(deptId);
        return true;
    }
}
