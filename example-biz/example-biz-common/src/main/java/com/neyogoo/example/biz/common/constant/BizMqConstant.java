package com.neyogoo.example.biz.common.constant;


import com.neyogoo.example.biz.common.constant.prefix.BizMqPrefixConstant;

/**
 * biz 仓库 MQ 相关常量
 */
public final class BizMqConstant {

    /**
     * 系统操作日志保存队列名称
     */
    public static final String SYS_OPT_LOG_QUEUE = BizMqPrefixConstant.BIZ_QUEUE_PREFIX + "sys_opt_log";

    /**
     * 系统登录日志保存队列名称
     */
    public static final String SYS_LOGIN_LOG_QUEUE = BizMqPrefixConstant.BIZ_QUEUE_PREFIX + "sys_login_log";

    /**
     * 操作站内信保存队列名称
     */
    public static final String MAIL_MSG_QUEUE = BizMqPrefixConstant.BIZ_QUEUE_PREFIX + "mail_msg";

    /**
     * 操作短信发送队列名称
     */
    public static final String SMS_SEND_QUEUE = BizMqPrefixConstant.BIZ_QUEUE_PREFIX + "sms_send";

    /**
     * mq消费失败记录队列名称
     */
    public static final String MQ_ERROR_MSG_QUEUE = BizMqPrefixConstant.BIZ_QUEUE_PREFIX + "mq_error_msg";

    private BizMqConstant() {

    }
}
