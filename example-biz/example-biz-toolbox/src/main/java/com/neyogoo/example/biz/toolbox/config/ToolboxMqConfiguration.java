package com.neyogoo.example.biz.toolbox.config;

import com.neyogoo.example.biz.common.constant.BizMqConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Queue;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.neyogoo.example.common.core.constant.BasicConstant.MQ_PREFIX;


/**
 * 消息接收
 */
@Slf4j
@Configuration
@ConditionalOnProperty(prefix = MQ_PREFIX, name = "enabled", havingValue = "true")
public class ToolboxMqConfiguration {

    //-----------队列---------------------------------------------------------------------------------------------------

    /**
     * 系统操作日志
     */
    @Bean
    public Queue sysOptLogQueue() {
        return new Queue(BizMqConstant.SYS_OPT_LOG_QUEUE);
    }

    /**
     * 系统登录日志
     */
    @Bean
    public Queue sysLoginLogQueue() {
        return new Queue(BizMqConstant.SYS_LOGIN_LOG_QUEUE);
    }

    /**
     * 站内信消息
     */
    @Bean
    public Queue mailMsgQueue() {
        return new Queue(BizMqConstant.MAIL_MSG_QUEUE);
    }

    /**
     * 发送短信消息
     */
    @Bean
    public Queue smsSendQueue() {
        return new Queue(BizMqConstant.SMS_SEND_QUEUE);
    }

    /**
     * mq消费失败记录
     */
    @Bean
    public Queue mqErrorMsgQueue() {
        return new Queue(BizMqConstant.MQ_ERROR_MSG_QUEUE);
    }
}
