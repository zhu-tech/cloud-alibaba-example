package com.neyogoo.example.admin.sys.config;

import com.neyogoo.example.biz.common.constant.BizMqConstant;
import com.neyogoo.example.common.boot.handler.AbstractGlobalExceptionHandler;
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
public class SysMqConfiguration extends AbstractGlobalExceptionHandler {

    //-----------队列----------------------------------------------------------------------------------------------------

    /**
     * 站内信队列
     */
    @Bean
    public Queue mailMsgQueue() {
        return new Queue(BizMqConstant.MAIL_MSG_QUEUE);
    }

}
