package com.neyogoo.example.admin.sys.service.impl.ext;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.neyogoo.example.admin.sys.model.Dept;
import com.neyogoo.example.admin.sys.model.Role;
import com.neyogoo.example.admin.sys.model.User;
import com.neyogoo.example.admin.sys.service.DeptService;
import com.neyogoo.example.admin.sys.service.OrgService;
import com.neyogoo.example.admin.sys.service.RoleService;
import com.neyogoo.example.admin.sys.service.UserService;
import com.neyogoo.example.admin.sys.service.ext.OrgQuerySerivice;
import com.neyogoo.example.admin.sys.service.ext.PermissionService;
import com.neyogoo.example.admin.sys.service.ext.UserQueryService;
import com.neyogoo.example.admin.sys.vo.response.org.OrgDeptRespVO;
import com.neyogoo.example.admin.sys.vo.response.org.OrgGetRespVO;
import com.neyogoo.example.admin.sys.vo.response.org.OrgSimpleRespVO;
import com.neyogoo.example.admin.sys.vo.response.org.OrgTreeRespVO;
import com.neyogoo.example.biz.common.enumeration.sys.OrgCategoryEnum;
import com.neyogoo.example.common.core.context.ContextUtils;
import com.neyogoo.example.common.core.model.DataScope;
import com.neyogoo.example.common.core.model.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 机构查询
 */
@Service
public class OrgQueryServiceImpl implements OrgQuerySerivice {

    @Autowired
    private OrgService orgService;
    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private DeptService deptService;
    @Autowired
    private UserQueryService userQueryService;
    @Autowired
    private PermissionService permissionService;

    /**
     * 根据id查询
     */
    @Override
    public OrgGetRespVO getById(Long id) {
        OrgGetRespVO respVO = OrgGetRespVO.fromModel(orgService.getById(id));
        if (respVO == null) {
            return null;
        }
        if (respVO.getParent() != null) {
            respVO.getParent().setValue(orgService.getNameById(respVO.getParent().getKey()));
        }
        // 机构管理人
        List<Long> userIds = permissionService.listOrgAdminUserIds(id);
        if (CollUtil.isNotEmpty(userIds)) {
            User user = userService.getById(userIds.get(0));
            respVO.setAdminUser(new OrgGetRespVO.AdminUser(user.getUserName(), user.getUserMobile()));
        }

        return respVO;
    }

    /**
     * 机构简易信息
     */
    @Override
    public List<OrgSimpleRespVO> listSimple(OrgCategoryEnum orgCategory, Integer orgLevel) {
        DataScope dataScope = userQueryService.getDataScope();
        List<OrgSimpleRespVO> list;
        switch (dataScope.getDsType()) {
            case All:
                list = orgService.listUsableSimple(null);
                break;
            default:
                list = orgService.listUsableSimple(Collections.singletonList(dataScope.getOrgId()));
                break;
        }
        if (orgCategory != null) {
            list = list.stream().filter(o -> o.getOrgCategory().contains(orgCategory.getCode()))
                    .collect(Collectors.toList());
        }
        if (orgLevel != null) {
            list = list.stream().filter(o -> o.getOrgLevel().getVal() == orgLevel)
                    .collect(Collectors.toList());
        }
        return list;
    }

    /**
     * 机构树
     */
    @Override
    public List<OrgTreeRespVO> findTree() {
        DataScope dataScope = userQueryService.getDataScope();
        switch (dataScope.getDsType()) {
            case All:
                return OrgTreeRespVO.toTree(null, orgService.list());
            case Orgs:
                return OrgTreeRespVO.toTree(dataScope.getOrgId(), orgService.listByIds(dataScope.getOrgIds()));
            default:
                return OrgTreeRespVO.toTree(orgService.getById(dataScope.getOrgId()));
        }
    }

    /**
     * 根据类型查询
     */
    @Override
    public List<OrgSimpleRespVO> listByOrgCategory(OrgCategoryEnum orgCategory) {
        return orgService.listUsableSimple(null).stream()
                .filter(o -> o.getOrgCategory().contains(orgCategory.getCode()))
                .collect(Collectors.toList());
    }

    /**
     * 查询机构（包含子机构）部门列表
     */
    @Override
    public List<OrgDeptRespVO> listOrgDept(String orgName) {
        Map<Long, String> orgMap = orgService.mapChildrenIdNameByParentId(ContextUtils.getOrgId(), orgName);
        if (CollectionUtil.isEmpty(orgMap)) {
            return Collections.emptyList();
        }
        Map<Long, List<Dept>> orgDeptMap = deptService.mapByOrgIds(orgMap.keySet());
        return OrgDeptRespVO.transferList(orgMap, orgDeptMap);
    }

    /**
     * 查询机构含有指定角色的人
     */
    private List<Pair<Long, String>> listOrgRoleUser(Long orgId, String roleCode) {
        Role role = roleService.getByCode(roleCode);
        if (role == null || !role.getUsableFlag()) {
            return Collections.emptyList();
        }
        List<Long> userIds = userService.listUserIdsWithOrgIdAndRoleId(orgId, role.getId());
        if (CollectionUtil.isEmpty(userIds)) {
            return Collections.emptyList();
        }
        return Pair.fromMap(userService.mapNamesByIds(userIds));
    }
}
