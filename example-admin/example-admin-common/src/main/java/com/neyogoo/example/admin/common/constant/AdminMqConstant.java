package com.neyogoo.example.admin.common.constant;

import com.neyogoo.example.biz.common.constant.prefix.BizMqPrefixConstant;

/**
 * 队列常量
 */
public interface AdminMqConstant {

    /**
     * 筛查服务 - 队列前缀
     */
    String PROCESS_QUEUE_PREFIX = BizMqPrefixConstant.ADMIN_QUEUE_PREFIX + "proc.";
    /**
     * 筛查服务 - 交换机前缀
     */
    String PROCESS_EXCHANGE_PREFIX = BizMqPrefixConstant.ADMIN_EXCHANGE_PREFIX + "proc.";
    /**
     * 培训服务 - 队列前缀
     */
    String TRAIN_QUEUE_PREFIX = BizMqPrefixConstant.ADMIN_QUEUE_PREFIX + "train.";
    /**
     * 培训服务 - 交换机前缀
     */
    String TRAIN_EXCHANGE_PREFIX = BizMqPrefixConstant.ADMIN_EXCHANGE_PREFIX + "train.";
}
