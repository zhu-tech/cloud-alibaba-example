package com.neyogoo.example.admin.sys.service.ext;

import com.neyogoo.example.admin.sys.vo.request.token.LoginReqVO;
import com.neyogoo.example.admin.sys.vo.request.token.LoginSmsSendReqVO;
import com.neyogoo.example.admin.sys.vo.request.token.LogoutReqVO;
import com.neyogoo.example.admin.sys.vo.request.token.RefreshTokenReqVO;
import com.neyogoo.example.admin.sys.vo.response.token.AuthInfoRespVO;
import com.neyogoo.example.common.token.enumeration.UserTypeEnum;
import org.springframework.scheduling.annotation.Async;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;

public interface TokenService {

    /**
     * 登入
     */
    AuthInfoRespVO login(LoginReqVO reqVO, HttpServletRequest request);

    /**
     * 刷新
     */
    AuthInfoRespVO refresh(RefreshTokenReqVO reqVO, HttpServletRequest request);

    /**
     * 登出
     */
    boolean logout(LogoutReqVO reqVO);

    /**
     * 发送短信验证码
     */
    boolean sendSms(LoginSmsSendReqVO reqVO);

    /**
     * 删除单个用户 Token 缓存
     */
    @Async
    void validUserToken(UserTypeEnum userType, Long userId);

    /**
     * 删除多个用户 Token 缓存
     */
    @Async
    void validUsersToken(UserTypeEnum userType, Collection<Long> userIds);
}
