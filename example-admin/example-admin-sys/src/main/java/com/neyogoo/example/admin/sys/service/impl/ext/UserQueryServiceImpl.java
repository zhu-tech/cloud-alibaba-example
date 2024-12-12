package com.neyogoo.example.admin.sys.service.impl.ext;

import cn.hutool.core.collection.CollUtil;
import com.neyogoo.example.admin.sys.model.Role;
import com.neyogoo.example.admin.sys.model.User;
import com.neyogoo.example.admin.sys.model.UserRole;
import com.neyogoo.example.admin.sys.service.DeptService;
import com.neyogoo.example.admin.sys.service.OrgService;
import com.neyogoo.example.admin.sys.service.RoleService;
import com.neyogoo.example.admin.sys.service.UserDeptService;
import com.neyogoo.example.admin.sys.service.UserOrgService;
import com.neyogoo.example.admin.sys.service.UserRoleService;
import com.neyogoo.example.admin.sys.service.UserService;
import com.neyogoo.example.admin.sys.service.ext.UserQueryService;
import com.neyogoo.example.admin.sys.vo.request.user.UserPageReqVO;
import com.neyogoo.example.admin.sys.vo.response.user.UserGetRespVO;
import com.neyogoo.example.admin.sys.vo.response.user.UserPageRespVO;
import com.neyogoo.example.biz.common.constant.BizRoleConstant;
import com.neyogoo.example.common.core.context.ContextUtils;
import com.neyogoo.example.common.core.enumeration.DataScopeTypeEnum;
import com.neyogoo.example.common.core.model.DataScope;
import com.neyogoo.example.common.core.model.PageReq;
import com.neyogoo.example.common.core.model.PageResp;
import com.neyogoo.example.common.core.model.Pair;
import com.neyogoo.example.common.core.util.EntityUtils;
import com.neyogoo.example.common.database.mybatis.conditions.Wraps;
import com.neyogoo.example.common.database.util.SqlPageUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 用户查询
 */
@Service
public class UserQueryServiceImpl implements UserQueryService {

    @Autowired
    private UserService userService;
    @Autowired
    private OrgService orgService;
    @Autowired
    private DeptService deptService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private UserRoleService userRoleService;
    @Autowired
    private UserDeptService userDeptService;
    @Autowired
    private UserOrgService userOrgService;

    /**
     * 根据用户id查询用户信息
     */
    @Override
    public UserGetRespVO getById(Long id) {
        // 用户信息
        UserGetRespVO respVO = UserGetRespVO.fromModel(userService.getById(id));
        if (respVO == null) {
            return null;
        }

        // key：部门id，value：部门名称
        Map<Long, String> deptMap = deptService.mapNamesByOrgId(ContextUtils.getOrgId());
        // key：角色id，value：角色名称
        Map<Long, String> roleMap = roleService.mapIdNames();

        // 关联部门
        List<Long> deptIds = userDeptService.listDeptIdsByUserId(id);
        respVO.setDepts(deptIds.stream().map(o -> new Pair<>(o, deptMap.get(o))).collect(Collectors.toList()));

        // 关联角色
        List<UserRole> userRoles = userRoleService.listUsableByUserId(id);
        respVO.setRoles(UserGetRespVO.DeptRole.from(deptIds, userRoles, deptMap, roleMap));

        return respVO;
    }

    /**
     * 查询当前登录人数据权限
     */
    @Override
    public DataScope getDataScope() {
        return getDataScope(ContextUtils.getUserId(), ContextUtils.getOrgId());
    }

    /**
     * 根据用户id和机构id查询数据权限
     */
    @Override
    public DataScope getDataScope(Long userId, Long orgId) {
        DataScope dataScope = new DataScope();
        dataScope.setUserId(userId);
        dataScope.setOrgId(orgId);
        dataScope.setOrgIds(Collections.singletonList(orgId));
        dataScope.setDsType(DataScopeTypeEnum.Self);

        List<Long> orgIds = userOrgService.listUsableOrgIdsByUserId(userId);
        if (!orgIds.contains(orgId)) {
            dataScope.setOrgId(-1L);
            dataScope.setOrgIds(Collections.singletonList(-1L));
            return dataScope;
        }
        List<Long> roleIds = userRoleService.listUsableRoleIdsByUserId(userId);
        if (CollUtil.isNotEmpty(roleIds)) {
            List<Role> roles = roleService.listByIds(roleIds);
            DataScopeTypeEnum dsType = roles.stream()
                    .map(Role::getDataScopeType)
                    .distinct()
                    .max(Comparator.comparing(DataScopeTypeEnum::getVal))
                    .orElse(DataScopeTypeEnum.Self);
            dataScope.setDsType(dsType);
            if (dataScope.getDsType().getVal() >= DataScopeTypeEnum.Orgs.getVal()) {
                orgIds = orgService.listChildrenIdsByParentId(orgId);
                if (CollUtil.isNotEmpty(orgIds)) {
                    dataScope.setOrgIds(orgIds);
                }
            }
        }
        return dataScope;
    }

    /**
     * 根据部门id查询
     */
    @Override
    public List<User> listByDeptId(Long deptId) {
        List<Long> userIds = userDeptService.listUsableUserIdsByDeptId(deptId);
        if (CollUtil.isEmpty(userIds)) {
            return Collections.emptyList();
        }
        return userService.listByIds(userIds);
    }

    /**
     * 根据机构id查询机构管理员
     */
    @Override
    public List<User> listOrgAdminsByOrgId(Long orgId) {
        Role orgAdmin = roleService.getByCode(BizRoleConstant.ORG_ADMIN_ROLE_CODE);
        if (orgAdmin == null || !orgAdmin.getUsableFlag()) {
            return Collections.emptyList();
        }
        List<Long> userIds = userRoleService.listUsableUserIdsByOrgIdAndRoleId(orgId, orgAdmin.getId());
        if (CollUtil.isEmpty(userIds)) {
            return Collections.emptyList();
        }
        return userService.listByIds(userIds);
    }

    /**
     * 分页
     */
    @Override
    public PageResp<UserPageRespVO> findPage(PageReq<UserPageReqVO> pageReq) {
        // 数据权限
        pageReq.getModel().setDataScope(getDataScope());

        // 用户分页
        PageResp<UserPageRespVO> pageResp = SqlPageUtils.transferResp(userService.findPage(pageReq));
        if (CollUtil.isEmpty(pageResp.getRecords())) {
            return pageResp;
        }

        List<Long> userIds = EntityUtils.toList(pageResp.getRecords(), UserPageRespVO::getId);
        Map<Long, String> orgNameMap = userOrgService.mapOrgNamesByUserIds(userIds);
        Map<Long, String> deptNameMap = userDeptService.mapDeptNamesByUserIds(userIds);
        Map<Long, String> roleNameMap = userRoleService.mapRoleNamesByUserIds(userIds);
        pageResp.getRecords().forEach(o -> {
            o.setOrgNames(orgNameMap.get(o.getId()));
            o.setDeptNames(deptNameMap.get(o.getId()));
            o.setRoleNames(roleNameMap.get(o.getId()));
        });

        return pageResp;
    }

    /**
     * 根据机构id查询下拉框
     */
    @Override
    public List<Pair<Long, String>> getComboBoxByOrgId(Long orgId) {
        List<Long> userIds = userOrgService.listUsableUserIdsByOrgId(orgId);
        return Pair.fromMap(userService.mapNamesByIds(userIds));
    }

    /**
     * 自动生成用户工号
     */
    @Override
    public String generateUserCode() {
        Set<String> userCodes = userService.list(Wraps.<User>lbQ().select(User::getUserCode))
                .stream().map(User::getUserCode).collect(Collectors.toSet());
        int index = userCodes.size();
        while (true) {
            String userCode = StringUtils.leftPad(String.valueOf(index + 1), 4, "0");
            if (!userCodes.contains(userCode)) {
                return userCode;
            }
            index++;
        }
    }
}
