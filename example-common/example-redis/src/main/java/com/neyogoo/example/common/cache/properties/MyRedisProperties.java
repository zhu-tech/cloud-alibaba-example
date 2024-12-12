package com.neyogoo.example.common.cache.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

import static com.neyogoo.example.common.core.constant.BasicConstant.REDIS_PREFIX;

/**
 * 缓存配置
 */
@Data
@ConfigurationProperties(prefix = REDIS_PREFIX)
public class MyRedisProperties {

    /**
     * 是否开启 Redis
     */
    private Boolean enabled = true;

    /**
     * 是否缓存 null 值
     */
    private Boolean cacheNullVal = false;

    /**
     * 通过 @Cacheable 注解标注的方法的缓存策略
     */
    private Cache def = new Cache();

    @Data
    public static class Cache {

        /**
         * key 的过期时间
         * 默认1天过期
         * eg:
         * timeToLive: 1d
         */
        private Duration timeToLive = Duration.ofDays(1);

        /**
         * 是否允许缓存null值
         */
        private boolean cacheNullValues = true;
    }
}
