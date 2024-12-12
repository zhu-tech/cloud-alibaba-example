package com.neyogoo.example.common.core.constant;

public interface ContextConstant {

    /**
     * Header - 验证信息 key
     */
    String AUTHORIZATION_KEY = "authorization";

    /**
     * Header - Token key
     */
    String HEADER_TOKEN = "token";

    /**
     * Header - 用户id key
     */
    String HEADER_USER_ID = "userId";

    /**
     * Header - 用户名称 key
     */
    String HEADER_USER_NAME = "userName";

    /**
     * Header - 用户账号 key
     */
    String HEADER_USER_ACCOUNT = "userAccount";

    /**
     * Header - 用户类型 key
     */
    String HEADER_USER_TYPE = "userType";

    /**
     * Header - 机构id key
     */
    String HEADER_ORG_ID = "orgId";

    /**
     * Header - 登录端 key
     */
    String HEADER_LOGIN_POINT = "loginPoint";

    /**
     * Header - 是否是从 feign 发起的请求
     */
    String HEADER_FEIGN = "x-feign";

    /**
     * Header - 日志链路追踪id信息头
     */
    String HEADER_TRACE_ID = "x-trace-id";

    /**
     * Header - 分布式事务追踪id信息头
     */
    String HEADER_TRANSACTION_ID = "x-transaction-id";

    /**
     * Header - 签名验证
     */
    String HEADER_SIGN = "sign";

    /**
     * Header - 签名验证
     */
    String HEADER_NONCE = "nonce";

    /**
     * Header - 签名验证
     */
    String HEADER_TIMESTAMP = "timestamp";
}
