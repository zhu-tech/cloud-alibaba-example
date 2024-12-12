package com.neyogoo.example.admin.sys.service.impl.ext;

import cn.hutool.core.collection.CollUtil;
import com.google.common.collect.Sets;
import com.neyogoo.example.admin.sys.config.properties.LoginProperties;
import com.neyogoo.example.admin.sys.model.User;
import com.neyogoo.example.admin.sys.service.DeptService;
import com.neyogoo.example.admin.sys.service.RoleService;
import com.neyogoo.example.admin.sys.service.UserDeptService;
import com.neyogoo.example.admin.sys.service.UserLoginService;
import com.neyogoo.example.admin.sys.service.UserOrgService;
import com.neyogoo.example.admin.sys.service.UserRoleService;
import com.neyogoo.example.admin.sys.service.UserService;
import com.neyogoo.example.admin.sys.service.ext.CaptcheCheckService;
import com.neyogoo.example.admin.sys.service.ext.TokenService;
import com.neyogoo.example.admin.sys.service.ext.UserOperationService;
import com.neyogoo.example.admin.sys.vo.request.user.UserSaveReqVO;
import com.neyogoo.example.admin.sys.vo.request.user.UserUpdatePwdReqVO;
import com.neyogoo.example.admin.sys.vo.request.user.UserUpdateReqVO;
import com.neyogoo.example.common.core.util.ArgumentAssert;
import com.neyogoo.example.common.token.enumeration.UserTypeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

/**
 * 用户操作
 */
@Service
public class UserOperationServiceImpl implements UserOperationService {

    @Autowired
    private LoginProperties loginProperties;
    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private DeptService deptService;
    @Autowired
    private UserOrgService userOrgService;
    @Autowired
    private UserDeptService userDeptService;
    @Autowired
    private UserRoleService userRoleService;
    @Autowired
    private UserLoginService userLoginService;
    @Autowired
    private CaptcheCheckService captcheCheckService;
    @Autowired
    private TokenService tokenService;

    /**
     * 新增
     */
    @Override
    @Transactional(rollbackFor = Throwable.class)
    public Long save(UserSaveReqVO reqVO) {

        boolean mobileExists = userService.queryIsUserMobileInUsing(null, reqVO.getUserMobile());
        ArgumentAssert.isFalse(mobileExists, "该手机号已存在");
        boolean userCodeExists = userService.queryIsUserCodeExists(reqVO.getUserCode());
        ArgumentAssert.isFalse(userCodeExists, "该工号已存在，请重新生成");

        Set<Long> deptIds = new HashSet<>(deptService.listIdsByOrgId(reqVO.getOrgId()));
        Set<Long> roleIds = new HashSet<>(roleService.listUsableIds());
        reqVO.checkDeptAndRole(deptIds, roleIds);

        // 保存用户信息
        User user = reqVO.toModel();
        user.setLoginPwd(LoginProperties.doubleEncodePwd(loginProperties.getDefaultPwd(), user.getLoginSalt()));
        userService.save(user);
        // 新增登录记录信息
        userLoginService.saveNewRecordIfNotExists(UserTypeEnum.SysUser, user.getId());

        // 保存关联机构
        userOrgService.saveRelation(user.getId(), reqVO.getOrgId());
        // 保存关联部门
        userDeptService.updateRelations(user.getId(), reqVO.getDeptIds());
        // 保存关联角色
        userRoleService.updateUserUnderDeptRole(user.getId(), reqVO.toDeptRoleIds());

        return user.getId();
    }

    /**
     * 修改
     */
    @Override
    @Transactional(rollbackFor = Throwable.class)
    public boolean update(UserUpdateReqVO reqVO) {

        boolean mobileExists = userService.queryIsUserMobileInUsing(reqVO.getId(), reqVO.getUserMobile());
        ArgumentAssert.isFalse(mobileExists, "该手机号已存在");
        ArgumentAssert.isTrue(userOrgService.queryIsRelationExists(reqVO.getOrgId(), reqVO.getId()),
                "该机构下不存在此用户");
        Set<Long> deptIds = new HashSet<>(deptService.listIdsByOrgId(reqVO.getOrgId()));
        Set<Long> roleIds = new HashSet<>(roleService.listUsableIds());
        reqVO.checkDeptAndRole(deptIds, roleIds);

        // 更新用户信息
        User user = userService.getById(reqVO.getId());
        ArgumentAssert.notNull(user, "未查询到该用户信息");
        reqVO.toModel(user);
        userService.updateAllById(user);

        // 保存关联部门
        userDeptService.updateRelations(user.getId(), reqVO.getDeptIds());
        // 保存关联角色
        userRoleService.updateUserUnderDeptRole(user.getId(), reqVO.toDeptRoleIds());

        return true;
    }

    /**
     * 启用禁用
     */
    @Override
    public boolean updateUsableById(Long userId, Boolean usable) {
        boolean flag = userService.updateUsableById(userId, usable);
        if (flag && !usable) {
            // 禁用用户，删除 Token 缓存
            tokenService.validUserToken(UserTypeEnum.SysUser, userId);
        }
        return flag;
    }

    /**
     * 更新密码
     */
    @Override
    public boolean updateLoginPwdById(Long userId, UserUpdatePwdReqVO reqVO) {
        // 验证滑动验证码
        captcheCheckService.checkBlockPuzzleCaptcha(reqVO.getCaptchaVerification());
        User user = userService.getById(userId);
        // 验证
        reqVO.checkBeforeChangePwd(user);
        // 更新
        boolean flag = userService.updateLoginPwdById(
                userId, LoginProperties.encodePwd(reqVO.getNewPwd(), user.getLoginSalt())
        );
        if (flag) {
            doAfterChangeLoginPwd(userId);
        }
        return flag;
    }

    /**
     * 重置密码
     */
    @Override
    public boolean resetLoginPwdById(Long userId) {
        // 检查用户信息
        User user = userService.getById(userId);
        User.checkBeforeChangePwd(user);
        String newOne = LoginProperties.doubleEncodePwd(loginProperties.getDefaultPwd(), user.getLoginSalt());
        ArgumentAssert.isFalse(newOne.equals(user.getLoginPwd()), "该用户当前密码已为默认密码");
        // 更新用户密码
        boolean flag = userService.updateLoginPwdById(userId, newOne);
        if (flag) {
            doAfterChangeLoginPwd(userId);
        }
        return flag;
    }

    /**
     * 删除
     */
    @Override
    @Transactional(rollbackFor = Throwable.class)
    public boolean removeById(Long userId) {
        boolean flag = userService.removeById(userId);
        if (flag) {
            userOrgService.removeByUserId(userId);
            userDeptService.removeByUserId(userId);
            userRoleService.removeByUserId(userId);
            tokenService.validUserToken(UserTypeEnum.SysUser, userId);
        }
        return flag;
    }

    /**
     * 修改密码后执行
     */
    private void doAfterChangeLoginPwd(Long userId) {
        // 更新最新更新密码时间
        userLoginService.updateLatestChangePwdTime(UserTypeEnum.SysUser, userId);
        // 删除 Token 缓存
        tokenService.validUserToken(UserTypeEnum.SysUser, userId);
    }

    /**
     * 更新用户关联角色
     */
    private void updateUserRole(Long userId, Set<Long> newRoleIds) {
        Set<Long> usableRoleIds = new HashSet<>(roleService.listUsableIds());
        Set<Long> errorRoleIds = Sets.difference(newRoleIds, usableRoleIds).copyInto(new HashSet<>());
        ArgumentAssert.isTrue(CollUtil.isEmpty(errorRoleIds), "存在不存在或已禁用的角色");

        Set<Long> oldRoleIds = new HashSet<>(userRoleService.listUsableRoleIdsByUserId(userId));
        Set<Long> needRemoveRoleIds = Sets.difference(oldRoleIds, newRoleIds).copyInto(new HashSet<>());
        if (CollUtil.isNotEmpty(needRemoveRoleIds)) {
            userRoleService.removeByUserIdAndRoleIds(userId, needRemoveRoleIds);
        }

        Set<Long> needSaveRoleIds = Sets.difference(newRoleIds, oldRoleIds).copyInto(new HashSet<>());
        if (CollUtil.isNotEmpty(needSaveRoleIds)) {
            userRoleService.saveRelations(userId, needSaveRoleIds);
        }
    }
}
