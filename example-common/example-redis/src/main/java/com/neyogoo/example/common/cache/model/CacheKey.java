package com.neyogoo.example.common.cache.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.NonNull;

import java.time.Duration;

/**
 * 缓存 key 封装
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CacheKey {

    /**
     * redis key
     */
    @NonNull
    private String key;
    /**
     * 超时时间
     */
    private Duration expire;

    public CacheKey(final @NonNull String key) {
        this.key = key;
    }


}
