package com.neyogoo.example.admin.sys.service.impl.ext;

import com.neyogoo.example.admin.sys.model.Org;
import com.neyogoo.example.admin.sys.model.Role;
import com.neyogoo.example.admin.sys.service.DeptService;
import com.neyogoo.example.admin.sys.service.OrgService;
import com.neyogoo.example.admin.sys.service.RoleService;
import com.neyogoo.example.admin.sys.service.UserOrgService;
import com.neyogoo.example.admin.sys.service.ext.OrgOperationService;
import com.neyogoo.example.admin.sys.service.ext.TokenService;
import com.neyogoo.example.admin.sys.service.ext.UserOperationService;
import com.neyogoo.example.admin.sys.service.ext.UserQueryService;
import com.neyogoo.example.admin.sys.vo.request.dept.DeptSaveReqVO;
import com.neyogoo.example.admin.sys.vo.request.org.OrgSaveReqVO;
import com.neyogoo.example.admin.sys.vo.request.org.OrgUpdateReqVO;
import com.neyogoo.example.admin.sys.vo.request.user.UserEditReqVO;
import com.neyogoo.example.admin.sys.vo.request.user.UserSaveReqVO;
import com.neyogoo.example.biz.common.constant.BizRoleConstant;
import com.neyogoo.example.biz.common.enumeration.sys.GenderEnum;
import com.neyogoo.example.biz.common.enumeration.sys.OrgCategoryEnum;
import com.neyogoo.example.biz.common.enumeration.sys.OrgLevelEnum;
import com.neyogoo.example.biz.toolbox.api.AreaApi;
import com.neyogoo.example.common.core.context.ContextUtils;
import com.neyogoo.example.common.core.exception.BizException;
import com.neyogoo.example.common.core.util.ArgumentAssert;
import com.neyogoo.example.common.core.util.RUtils;
import com.neyogoo.example.common.core.util.StrPool;
import com.neyogoo.example.common.token.enumeration.UserTypeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

/**
 * 机构操作
 */
@Service
public class OrgOperationServiceImpl implements OrgOperationService {

    @Autowired
    private OrgService orgService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private DeptService deptService;
    @Autowired
    private UserOrgService userOrgService;
    @Autowired
    private UserQueryService userQueryService;
    @Autowired
    private UserOperationService userOperationService;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private AreaApi areaApi;

    /**
     * 新增
     */
    @Override
    @Transactional(rollbackFor = Throwable.class)
    public Long save(OrgSaveReqVO reqVO) {

        // 检查机构信息
        boolean orgCodeExists = orgService.queryIsOrgCodeExists(reqVO.getOrgCode());
        ArgumentAssert.isFalse(orgCodeExists, "该机构编码已存在，请重新生成");
        boolean orgNameExists = orgService.queryIsOrgNameExists(reqVO.getOrgName(), null);
        ArgumentAssert.isFalse(orgNameExists, "该机构名称已存在，请重新输入");
        checkOrgCategory(reqVO.getOrgCategory());

        // 保存机构信息
        Org model = reqVO.toModel();
        addParentInfo(model);
        model.setAreaName(RUtils.getDataThrowEx(areaApi.getFullNameByCode(reqVO.getCountyCode())));
        orgService.save(model);

        // 生成默认部门id
        Long deptId = deptService.save(DeptSaveReqVO.generalDefault(model.getId()));

        // 生成高级管理员
        Role role = roleService.getByCode(BizRoleConstant.ORG_ADMIN_ROLE_CODE);
        UserSaveReqVO userSaveReqVO = new UserSaveReqVO();
        userSaveReqVO
                .setUserCode(userQueryService.generateUserCode())
                .setUserName(reqVO.getAdminUser().getUserName())
                .setUserMobile(reqVO.getAdminUser().getUserMobile())
                .setUserGender(GenderEnum.M)
                .setDeptIds(Collections.singleton(deptId))
                .setDeptRoles(Collections.singletonList(
                        new UserEditReqVO.DeptRole(deptId, Collections.singletonList(role.getId()))
                ))
                .setOrgId(model.getId());
        userOperationService.save(userSaveReqVO);

        return model.getId();
    }

    /**
     * 修改
     */
    @Override
    @Transactional(rollbackFor = Throwable.class)
    public boolean update(OrgUpdateReqVO reqVO) {

        // 检查机构信息
        boolean orgNameExists = orgService.queryIsOrgNameExists(reqVO.getOrgName(), reqVO.getId());
        ArgumentAssert.isFalse(orgNameExists, "该机构名称已存在，请重新输入");
        checkOrgCategory(reqVO.getOrgCategory());
        Org model = orgService.getById(reqVO.getId());
        ArgumentAssert.notNull(model, "未查询到该机构信息");
        ArgumentAssert.isTrue(model.getUsableFlag(), "该机构已被禁用");

        // 更新机构信息
        reqVO.toModel(model);
        model.setAreaName(RUtils.getDataThrowEx(areaApi.getFullNameByCode(reqVO.getCountyCode())));
        orgService.updateAllById(model);

        return true;
    }

    /**
     * 启用禁用
     */
    @Override
    public boolean updateUsableById(Long id, Boolean usable) {
        if (id == null) {
            return false;
        }
        if (!usable) {
            boolean hasChildren = orgService.queryIsChildrenUsable(id);
            ArgumentAssert.isFalse(hasChildren, "该机构下存在启用的子机构，请先删除/禁用/移动子机构");
        }
        boolean flag = orgService.updateUsableById(id, usable);
        if (!usable && flag) {
            List<Long> userIds = userOrgService.listUsableUserIdsByOrgId(id);
            tokenService.validUsersToken(UserTypeEnum.SysUser, userIds);
        }
        return flag;
    }

    /**
     * 删除
     */
    @Override
    public boolean removeById(Long id) {
        if (id == null) {
            return false;
        }
        ArgumentAssert.isFalse(ContextUtils.getOrgId().equals(id), "不可删除当前登录机构");
        boolean hasChildren = orgService.queryIsChildrenExists(id);
        ArgumentAssert.isFalse(hasChildren, "该机构下存在子机构，请先删除/移动子机构");
        boolean flag = orgService.removeById(id);
        if (flag) {
            List<Long> userIds = userOrgService.listUsableUserIdsByOrgId(id);
            userOrgService.removeByOrgId(id);
            tokenService.validUsersToken(UserTypeEnum.SysUser, userIds);
        }
        return flag;
    }

    /**
     * 检查机构类别值是否正确
     */
    private void checkOrgCategory(String orgCategory) {
        String[] values = orgCategory.split(",");
        for (String value : values) {
            ArgumentAssert.notNull(OrgCategoryEnum.get(value), "机构类别选择错误");
        }
    }

    /**
     * 检查上级单位是否选择正确
     */
    private void addParentInfo(Org model) {
        Org currentOrg = orgService.getById(ContextUtils.getOrgId());
        if (currentOrg.getOrgLevel() == OrgLevelEnum.L1) {
            model.setOrgLevel(OrgLevelEnum.L2);
        } else if (currentOrg.getOrgLevel() == OrgLevelEnum.L2) {
            model.setOrgLevel(OrgLevelEnum.L3);
        } else {
            throw BizException.wrap("三级单位不能添加下级单位");
        }
        model.setParentId(currentOrg.getId());
        model.setParentIds(currentOrg.getParentIds() + currentOrg.getId() + StrPool.COMMA);
    }
}
