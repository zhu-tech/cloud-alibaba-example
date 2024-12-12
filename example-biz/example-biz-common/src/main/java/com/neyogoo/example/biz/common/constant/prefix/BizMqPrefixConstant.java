package com.neyogoo.example.biz.common.constant.prefix;

/**
 * RabbitMQ - 各模块统一前缀常量
 */
public interface BizMqPrefixConstant {

    /**
     * 队列前缀
     */
    String QUEUE_PREFIX = "que.";
    /**
     * 死信队列前缀
     */
    String DL_QUEUE_PREFIX = "dlq.";
    /**
     * 交换机前缀
     */
    String EXCHANGE_PREFIX = "ex.";
    /**
     * 路由键前缀
     */
    String ROUTING_KEY_PREFIX = "rk.";


    /**
     * 通用服务 - 队列前缀
     */
    String BIZ_QUEUE_PREFIX = QUEUE_PREFIX + "biz.";
    /**
     * 通用服务 - 死信队列前缀
     */
    String BIZ_DL_QUEUE_PREFIX = DL_QUEUE_PREFIX + "biz.";
    /**
     * 通用服务 - 交换机前缀
     */
    String BIZ_EXCHANGE_PREFIX = EXCHANGE_PREFIX + "biz.";
    /**
     * 通用服务 - 路由键前缀
     */
    String BIZ_ROUTING_KEY_PREFIX = ROUTING_KEY_PREFIX + "biz.";

    /**
     * 业务服务 - 队列前缀
     */
    String ADMIN_QUEUE_PREFIX = QUEUE_PREFIX + "admin.";
    /**
     * 业务服务 - 死信队列前缀
     */
    String ADMIN_DL_QUEUE_PREFIX = DL_QUEUE_PREFIX + "admin.";
    /**
     * 业务服务 - 交换机前缀
     */
    String ADMIN_EXCHANGE_PREFIX = EXCHANGE_PREFIX + "admin.";
    /**
     * 业务服务 - 路由键前缀
     */
    String ADMIN_ROUTING_KEY_PREFIX = ROUTING_KEY_PREFIX + "admin.";
}
