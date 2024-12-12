package com.neyogoo.example.admin.common.constant;

import com.neyogoo.example.biz.common.constant.prefix.BizLockPrefixConstant;

/**
 * 锁常量
 */
public interface AdminLockConstant {

    /**
     * process 模块缓存前缀
     */
    String PROCESS_MODULAR_PREFIX = BizLockPrefixConstant.ADMIN_PREFIX + "proc:";

    /**
     * sys 模块缓存前缀
     */
    String SYS_MODULAR_PREFIX = BizLockPrefixConstant.ADMIN_PREFIX + "sys:";

    /**
     * train 模块缓存前缀
     */
    String TRAIN_MODULAR_PREFIX = BizLockPrefixConstant.ADMIN_PREFIX + "train:";
}
