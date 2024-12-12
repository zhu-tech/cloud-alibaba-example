package com.neyogoo.example.common.token.constant;

public interface TokenConstant {

    /**
     * admin:sys:login_token:{loginToken} -> 缓存的登录用户信息
     */
    String LOGIN_TOKEN_CACHE = "admin:sys:login_token:";

    /**
     * admin:sys:refresh_token:{refreshToken} -> 缓存的登录用户信息
     */
    String REFRESH_TOKEN_CACHE = "admin:sys:refresh_token:";

    /**
     * admin:sys:user_token:{userType}:{userId} -> 登录用户当前生效 token 列表
     */
    String USER_TOKEN_CACHE = "admin:sys:user_token:";

}
