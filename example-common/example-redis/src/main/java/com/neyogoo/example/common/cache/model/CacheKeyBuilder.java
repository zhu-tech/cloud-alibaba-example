package com.neyogoo.example.common.cache.model;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.text.StrPool;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.neyogoo.example.common.core.context.ContextUtils;
import com.neyogoo.example.common.core.util.ArgumentAssert;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@FunctionalInterface
public interface CacheKeyBuilder {

    String getPrefix();

    @NonNull
    default String getOrgId() {
        return ContextUtils.getOrgId() != null ? String.valueOf(ContextUtils.getOrgId()) : null;
    }

    default CacheKeyBuilder setOrgId(Long orgId) {
        return this;
    }

    /**
     * 服务模块名
     */
    default String getModular() {
        return null;
    }

    /**
     * 缓存自动过期时间
     */
    @Nullable
    default Duration getExpire() {
        return Duration.ofHours(1);
    }

    default CacheKey key(Object... uniques) {
        String key = getKey(uniques);
        ArgumentAssert.notEmpty(key, "key 不能为空");
        return new CacheKey(key, getExpire());
    }

    default CacheHashKey hashKey(Object... uniques) {
        String key = getKey(uniques);

        ArgumentAssert.notEmpty(key, "key 不能为空");
        return new CacheHashKey(key, null, getExpire());
    }

    default CacheHashKey hashFieldKey(@NonNull Object field, Object... uniques) {
        String key = getKey(uniques);

        ArgumentAssert.notEmpty(key, "key 不能为空");
        ArgumentAssert.notNull(field, "field 不能为空");
        return new CacheHashKey(key, field, getExpire());
    }

    /**
     * 根据动态参数 拼接key，[服务模块名:]前缀:[租户id:]业务值
     *
     * @param uniques 动态参数
     */
    default String getKey(Object... uniques) {
        List<String> regionList = new ArrayList<>();

        // 1. 服务模块名
        String modular = getModular();
        if (StrUtil.isNotEmpty(modular)) {
            regionList.add(modular);
        }

        // 2. 前缀
        String prefix = this.getPrefix();
        ArgumentAssert.notEmpty(prefix, "缓存前缀不能为空");
        regionList.add(prefix);

        // 3. 租户id
        String tenantId = this.getOrgId();
        if (StrUtil.isNotEmpty(tenantId)) {
            regionList.add(tenantId);
        }

        // 4.业务值
        for (Object unique : uniques) {
            if (ObjectUtil.isNotEmpty(unique)) {
                regionList.add(String.valueOf(unique));
            }
        }
        return CollUtil.join(regionList, StrPool.COLON);
    }
}
