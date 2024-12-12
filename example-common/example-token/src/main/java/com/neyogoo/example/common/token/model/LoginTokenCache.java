package com.neyogoo.example.common.token.model;

import com.neyogoo.example.common.token.enumeration.LoginPointEnum;
import com.neyogoo.example.common.token.enumeration.LoginTypeEnum;
import com.neyogoo.example.common.token.enumeration.UserTypeEnum;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 登录 Token 缓存，存放数据为在网关直接放入 ThreadLocal 的数据，这些数据应该是不常变动的，或者允许延迟的
 */
@Data
@Accessors(chain = true)
@NoArgsConstructor
public class LoginTokenCache {

    /**
     * 用户信息
     */
    protected User user = new User();

    /**
     * 机构信息
     */
    protected Org org = new Org();

    /**
     * 登录信息
     */
    protected Login login = new Login();

    /**
     * 刷新令牌
     */
    private String refreshToken;

    @Data
    public static class User {

        /**
         * 用户id
         */
        private Long userId;

        /**
         * 用户类型
         */
        private UserTypeEnum userType;

        /**
         * 账号名（星星赞、启赞用户为手机号，微信用户为openid...）
         */
        private String userAccount;

        /**
         * 用户名
         */
        private String userName;
    }

    @Data
    public static class Org {

        /**
         * 机构id
         */
        private Long orgId;

        /**
         * 机构名称
         */
        private String orgName;
    }

    @Data
    public static class Login {

        /**
         * 登录端
         */
        private LoginPointEnum loginPoint;

        /**
         * 登录方式
         */
        private LoginTypeEnum loginType;

        /**
         * 登录账号（用户名密码方式为用户账号，短信验证方式为手机号，微信登录方式为openid...）
         */
        private String loginAccount;

        /**
         * 登录时间
         */
        private LocalDateTime loginTime;
    }
}
