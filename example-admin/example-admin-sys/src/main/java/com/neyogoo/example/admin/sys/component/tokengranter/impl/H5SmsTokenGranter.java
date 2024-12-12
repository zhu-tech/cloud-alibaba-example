package com.neyogoo.example.admin.sys.component.tokengranter.impl;

import com.neyogoo.example.admin.sys.component.tokengranter.AbstractCstUserTokenGranter;
import com.neyogoo.example.admin.sys.vo.request.token.H5SmsLoginReqVO;
import com.neyogoo.example.admin.sys.vo.request.token.LoginReqVO;
import com.neyogoo.example.admin.sys.vo.response.token.CstUserAuthInfoRespVO;
import com.neyogoo.example.common.token.enumeration.LoginPointEnum;
import com.neyogoo.example.common.token.enumeration.LoginTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * 用户端 - 用户名密码 - token 分发器
 */
@Slf4j
@Component
public class H5SmsTokenGranter extends AbstractCstUserTokenGranter {

    @Override
    public CstUserAuthInfoRespVO login(LoginReqVO loginReqVO, HttpServletRequest request) {
        if (!(loginReqVO instanceof H5SmsLoginReqVO)) {
            return null;
        }
        H5SmsLoginReqVO reqVO = (H5SmsLoginReqVO) loginReqVO;

        // 验证短信验证码
        checkSmsCode(reqVO);

        // 生成验证信息
        CstUserAuthInfoRespVO authInfo = generalAuthInfo(reqVO.getUserMobile());

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
    protected CstUserAuthInfoRespVO generalAuthInfo(String userMobile) {
        CstUserAuthInfoRespVO authInfo = super.generalAuthInfo(userMobile);
        // 登录信息
        authInfo.getLogin()
                .setLoginPoint(LoginPointEnum.H5)
                .setLoginType(LoginTypeEnum.Sms)
                .setLoginAccount(userMobile);
        return authInfo;
    }
}
