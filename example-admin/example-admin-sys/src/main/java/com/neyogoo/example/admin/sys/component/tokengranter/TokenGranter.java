package com.neyogoo.example.admin.sys.component.tokengranter;

import com.neyogoo.example.admin.sys.vo.request.token.LoginReqVO;
import com.neyogoo.example.admin.sys.vo.request.token.RefreshTokenReqVO;
import com.neyogoo.example.admin.sys.vo.response.token.AuthInfoRespVO;

import javax.servlet.http.HttpServletRequest;

/**
 * token 分发器
 */
public interface TokenGranter<T extends AuthInfoRespVO> {

    /**
     * 登录
     */
    T login(LoginReqVO reqVO, HttpServletRequest request);

    /**
     * 刷新
     */
    T refresh(RefreshTokenReqVO reqVO, HttpServletRequest request);
}
