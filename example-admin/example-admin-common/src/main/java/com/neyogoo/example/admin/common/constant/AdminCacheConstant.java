package com.neyogoo.example.admin.common.constant;

import com.neyogoo.example.biz.common.constant.prefix.BizCachePrefixConstant;

/**
 * 缓存常量
 */
public interface AdminCacheConstant {

    /**
     * process 模块缓存前缀
     */
    String PROCESS_MODULAR_PREFIX = BizCachePrefixConstant.ADMIN_PREFIX + "proc:";

    /**
     * sys 模块缓存前缀
     */
    String SYS_MODULAR_PREFIX = BizCachePrefixConstant.ADMIN_PREFIX + "sys:";

    /**
     * train 模块缓存前缀
     */
    String TRAIN_MODULAR_PREFIX = BizCachePrefixConstant.ADMIN_PREFIX + "train:";
}
