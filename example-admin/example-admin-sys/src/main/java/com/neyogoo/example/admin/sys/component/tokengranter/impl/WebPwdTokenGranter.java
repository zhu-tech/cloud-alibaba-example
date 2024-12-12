package com.neyogoo.example.admin.sys.component.tokengranter.impl;

import cn.hutool.crypto.SecureUtil;
import com.neyogoo.example.admin.sys.component.tokengranter.AbstractSysUserTokenGranter;
import com.neyogoo.example.admin.sys.model.Org;
import com.neyogoo.example.admin.sys.model.User;
import com.neyogoo.example.admin.sys.vo.entity.UserPwdCheckEntity;
import com.neyogoo.example.admin.sys.vo.request.token.LoginReqVO;
import com.neyogoo.example.admin.sys.vo.request.token.WebPwdLoginReqVO;
import com.neyogoo.example.admin.sys.vo.response.token.SysUserAuthInfoRespVO;
import com.neyogoo.example.common.token.enumeration.LoginPointEnum;
import com.neyogoo.example.common.token.enumeration.LoginTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * PC端（政府） - 用户名密码 - token 分发器
 */
@Slf4j
@Component
public class WebPwdTokenGranter extends AbstractSysUserTokenGranter {

    @Override
    public SysUserAuthInfoRespVO login(LoginReqVO loginReqVO, HttpServletRequest request) {
        if (!(loginReqVO instanceof WebPwdLoginReqVO)) {
            return null;
        }
        WebPwdLoginReqVO reqVO = (WebPwdLoginReqVO) loginReqVO;

        // 是否使用了万能码
        boolean powerFlag = StringUtils.isNotBlank(loginProperties.getPowerKey())
                && SecureUtil.md5(loginProperties.getPowerKey()).equals(reqVO.getUserPwd());

        // 验证滑动验证码
        if (!powerFlag) {
            captcheCheckService.checkBlockPuzzleCaptcha(reqVO.getCaptchaVerification());
        }

        // 验证用户状态
        User user = User.checkBeforeLogin(userService.getByInUseUserMobile(reqVO.getUserAccount()));

        // 验证用户密码
        if (!powerFlag) {
            UserPwdCheckEntity userPwdCheckBO = UserPwdCheckEntity.fromUser(user).setInputPwd(reqVO.getUserPwd());
            checkUserPwd(userPwdCheckBO);
        }

        // 选择租户信息
        List<Org> orgs = listOrgsByUserId(user.getId());

        // 生成验证信息
        SysUserAuthInfoRespVO authInfo = generalAuthInfo(user, orgs.get(0));

        // 缓存Token信息
        cacheToken(authInfo);

        // 更新登录记录
        updateUserLogin(authInfo, request);

        return authInfo;
    }

    /**
     * 生成用户验证信息
     */
    @Override
    protected SysUserAuthInfoRespVO generalAuthInfo(User user, Org org) {
        SysUserAuthInfoRespVO authInfo = super.generalAuthInfo(user, org);
        // 登录信息
        authInfo.getLogin()
                .setLoginPoint(LoginPointEnum.PC)
                .setLoginType(LoginTypeEnum.Pwd)
                .setLoginAccount(user.getUserMobile());

        return authInfo;
    }
}
