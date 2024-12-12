package com.neyogoo.example.biz.common.constant.prefix;

/**
 * 分布式锁前缀常量
 */
public interface BizLockPrefixConstant {

    String LOCK_PREFIX = "lock:";

    String BIZ_PREFIX = LOCK_PREFIX + "biz:";

    String ADMIN_PREFIX = LOCK_PREFIX + "admin:";
}
