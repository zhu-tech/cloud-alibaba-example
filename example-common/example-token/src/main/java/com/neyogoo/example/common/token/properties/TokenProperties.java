package com.neyogoo.example.common.token.properties;

import com.neyogoo.example.common.core.constant.BasicConstant;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@NoArgsConstructor
@ConfigurationProperties(prefix = TokenProperties.PREFIX)
public class TokenProperties {

    public static final String PREFIX = BasicConstant.PROJECT_PREFIX + ".token";

    /**
     * 允许同时生效的 token 数
     */
    private Integer concurrency = 1;

    /**
     * login token 的过期时间，单位：分钟
     */
    private Integer tokenExpire = 8 * 60;
    /**
     * refresh token 的过期时间，单位：分钟
     */
    private Integer refreshExpire = 24 * 60;
}
