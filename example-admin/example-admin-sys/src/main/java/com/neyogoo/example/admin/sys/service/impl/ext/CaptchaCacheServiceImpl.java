package com.neyogoo.example.admin.sys.service.impl.ext;

import com.anji.captcha.service.CaptchaCacheService;
import com.neyogoo.example.admin.sys.constant.SysCacheConstant;
import com.neyogoo.example.common.cache.model.CacheKey;
import com.neyogoo.example.common.cache.repository.CacheOps;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Duration;

public class CaptchaCacheServiceImpl implements CaptchaCacheService {

    @Override
    public String type() {
        return "Redis";
    }

    @Autowired
    private CacheOps cacheOps;

    @Override
    public void set(String key, String value, long expiresInSeconds) {
        key = key(key);
        CacheKey cacheKey = new CacheKey(key, Duration.ofSeconds(expiresInSeconds));
        cacheOps.set(cacheKey, value);
    }

    @Override
    public boolean exists(String key) {
        key = key(key);
        return cacheOps.exists(new CacheKey(key));
    }

    @Override
    public void delete(String key) {
        key = key(key);
        cacheOps.del(key);
    }

    @Override
    public String get(String key) {
        key = key(key);
        return cacheOps.get(key);
    }

    @Override
    public Long increment(String key, long val) {
        key = key(key);
        CacheKey cacheKey = new CacheKey(key);
        return cacheOps.incrBy(cacheKey, val);
    }

    private String key(String key) {
        return SysCacheConstant.BLOCK_PUZZLE_CAPTCHA + key;
    }
}
