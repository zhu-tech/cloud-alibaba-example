package com.neyogoo.example.admin.sys.component.tokengranter;

import com.neyogoo.example.admin.sys.model.Org;
import com.neyogoo.example.admin.sys.model.User;
import com.neyogoo.example.admin.sys.service.OrgService;
import com.neyogoo.example.admin.sys.service.RoleService;
import com.neyogoo.example.admin.sys.service.UserOrgService;
import com.neyogoo.example.admin.sys.service.UserRoleService;
import com.neyogoo.example.admin.sys.service.UserService;
import com.neyogoo.example.admin.sys.service.ext.PermissionService;
import com.neyogoo.example.admin.sys.vo.request.token.RefreshTokenReqVO;
import com.neyogoo.example.admin.sys.vo.response.permission.RoleMenuRespVO;
import com.neyogoo.example.admin.sys.vo.response.token.SysUserAuthInfoRespVO;
import com.neyogoo.example.common.core.exception.UnauthorizedException;
import com.neyogoo.example.common.core.util.ArgumentAssert;
import com.neyogoo.example.common.token.enumeration.UserTypeEnum;
import com.neyogoo.example.common.token.model.RefreshTokenCache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 政府人员 token 分发器
 */
@Slf4j
public abstract class AbstractSysUserTokenGranter extends AbstractTokenGranter<SysUserAuthInfoRespVO> {

    @Autowired
    protected UserService userService;
    @Autowired
    protected OrgService orgService;
    @Autowired
    protected UserOrgService userOrgService;
    @Autowired
    protected UserRoleService userRoleService;
    @Autowired
    protected RoleService roleService;
    @Autowired
    protected PermissionService permissionService;

    /**
     * 刷新
     */
    @Override
    public SysUserAuthInfoRespVO refresh(RefreshTokenReqVO reqVO, HttpServletRequest request) {
        RefreshTokenCache cache = reqVO.getCache();

        // 验证用户状态
        User user = User.checkBeforeLogin(
                userService.getById(reqVO.getCache().getUser().getUserId())
        );

        // 验证租户信息
        Org org = checkOrgInfo(user.getId(), reqVO.getOrgId());

        // 生成验证信息
        SysUserAuthInfoRespVO authInfo = generalAuthInfo(user, org);
        authInfo.getLogin()
                .setLoginPoint(cache.getLogin().getLoginPoint())
                .setLoginType(cache.getLogin().getLoginType())
                .setLoginAccount(cache.getLogin().getLoginAccount());

        // 缓存新 Token 信息
        cacheToken(authInfo);

        // 移除旧刷新 Token
        removeRefreshToken(reqVO.getRefreshToken());

        // 更新登录记录
        updateUserLogin(authInfo, request);

        return authInfo;
    }

    /**
     * 根据用户id锁定用户账户
     */
    @Override
    protected void lockUserById(Long userId) {
        userService.lockUserById(userId, loginProperties.getPwdErrorLockTime());
    }

    /**
     * 生成用户验证信息（指定租户）
     */
    protected SysUserAuthInfoRespVO generalAuthInfo(User user, Org org) {
        SysUserAuthInfoRespVO authInfo = new SysUserAuthInfoRespVO();
        // 登录信息
        authInfo.getLogin()
                .setLoginTime(LocalDateTime.now());
        // 用户信息
        authInfo.getUser()
                .setUserType(UserTypeEnum.SysUser)
                .setUserId(user.getId())
                .setUserAccount(user.getUserMobile())
                .setUserName(user.getUserName());
        // 租户信息
        authInfo.getOrg()
                .setOrgId(org.getId())
                .setOrgName(org.getOrgName());

        // 权限数据
        List<RoleMenuRespVO> permissions = permissionService.listPermissionByUserId(
                authInfo.getUser().getUserId(), org
        );
        ArgumentAssert.notEmpty(permissions, "该账户无登录权限，请联系管理员进行分配");
        authInfo.putPermission(permissions);
        // 扩展数据
        authInfo.getSysUserExt()
                .setOrgLevel(org.getOrgLevel())
                .setRoleNames(permissionService.listRoleNamesByUserId(authInfo.getUser().getUserId()))
                .setCreateTime(user.getCreateTime());

        // Token 数据
        generalToken(authInfo);

        return authInfo;
    }

    /**
     * 获取关联的租户信息
     */
    protected List<Org> listOrgsByUserId(Long userId) {
        List<Long> orgIds = userOrgService.listUsableOrgIdsByUserId(userId);
        ArgumentAssert.notEmpty(orgIds, "未查询到关联的机构信息，请联系管理员");
        return orgService.listByIds(orgIds);
    }

    /**
     * 检查机构信息
     */
    protected Org checkOrgInfo(Long userId, Long orgId) {
        List<Long> orgIds = userOrgService.listUsableOrgIdsByUserId(userId);
        if (!orgIds.contains(orgId)) {
            throw UnauthorizedException.wrap("该机构不存在或已禁用");
        }
        return orgService.getById(orgId);
    }
}
