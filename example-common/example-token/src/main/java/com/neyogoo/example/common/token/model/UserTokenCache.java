package com.neyogoo.example.common.token.model;

import com.neyogoo.example.common.token.enumeration.LoginPointEnum;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 用户 Token 缓存，一个 userId 对应多个生效的此实体
 */
@Data
@Accessors(chain = true)
@NoArgsConstructor
public class UserTokenCache {

    /**
     * 登录端
     */
    private LoginPointEnum loginPoint;

    /**
     * Token 值
     */
    private String token;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 过期时间
     */
    private LocalDateTime expireTime;
}
