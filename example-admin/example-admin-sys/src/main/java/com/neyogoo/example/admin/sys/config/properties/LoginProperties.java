package com.neyogoo.example.admin.sys.config.properties;

import cn.hutool.crypto.SecureUtil;
import com.neyogoo.example.common.core.constant.BasicConstant;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@ToString
@RefreshScope
@Configuration
@ConfigurationProperties(prefix = LoginProperties.PREFIX)
public class LoginProperties {

    public static final String PREFIX = BasicConstant.PROJECT_PREFIX + ".login";

    /**
     * 登录时否验证密码有效性（常用于开发环境快速登录）
     */
    private Boolean verifyPassword = true;

    /**
     * 登录时否验证验证码有效性（常用于开发环境快速登录）
     */
    private Boolean verifyCaptcha = true;

    /**
     * 默认用户密码
     */
    private String defaultPwd = "123456";

    /**
     * 密码最大输错次数，小于 0 不限制
     */
    private Integer maxPwdErrorNum = 5;

    /**
     * 密码错误锁定用户时间，小于 0 不锁定，单位：分钟
     */
    private Integer pwdErrorLockTime = 20;

    /**
     * 允许同时生效的 token 数
     */
    private Integer tokenConcurrency = 1;

    /**
     * login token 的过期时间，单位：分钟
     */
    private Integer tokenExpire = 2 * 60;

    /**
     * refresh token 的过期时间，单位：分钟
     */
    private Integer refreshExpire = 8 * 60;

    /**
     * 万能密码
     */
    private String powerKey = "";

    /**
     * 单层加密（sha256，用于登录验证密码、修改密码等）
     */
    public static String encodePwd(String pwd, String salt) {
        return SecureUtil.sha256(pwd + salt);
    }

    /**
     * 双层加密（md5 + sha256，用于给用户初始化密码）
     */
    public static String doubleEncodePwd(String pwd, String salt) {
        return SecureUtil.sha256(SecureUtil.md5(pwd) + salt);
    }

    public static String generalSalt() {
        return RandomStringUtils.randomAlphanumeric(6).toLowerCase();
    }
}
