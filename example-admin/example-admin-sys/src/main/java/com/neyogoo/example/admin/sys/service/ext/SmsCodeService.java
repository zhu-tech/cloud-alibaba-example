package com.neyogoo.example.admin.sys.service.ext;

import com.neyogoo.example.admin.sys.vo.request.token.LoginSmsCheckReqVO;
import com.neyogoo.example.admin.sys.vo.request.token.LoginSmsSendReqVO;

public interface SmsCodeService {

    /**
     * 发送短信验证码 - 用户登录
     */
    boolean sendForUserLogin(LoginSmsSendReqVO reqVO);

    /**
     * 验证短信验证码 - 用户登录
     */
    void checkForUserLogin(LoginSmsCheckReqVO reqVO);
}
