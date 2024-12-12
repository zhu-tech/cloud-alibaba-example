package com.neyogoo.example.common.token.model;

import com.neyogoo.example.common.token.enumeration.LoginPointEnum;
import com.neyogoo.example.common.token.enumeration.LoginTypeEnum;
import com.neyogoo.example.common.token.enumeration.UserTypeEnum;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 刷新 Token 缓存，存放的信息均为生成新的登录 Token 时需要用到的信息
 */
@Data
@Accessors(chain = true)
@NoArgsConstructor
public class RefreshTokenCache {

    /**
     * 用户信息
     */
    private User user = new User();

    /**
     * 租户信息
     */
    protected Org org = new Org();

    /**
     * 登录信息
     */
    private Login login = new Login();

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
    }

    @Data
    public static class Org {

        /**
         * 机构id
         */
        private Long orgId;
    }

    @Data
    public static class Login {

        /**
         * 客户端id
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
    }
}
