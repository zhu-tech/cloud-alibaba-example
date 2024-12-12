package com.neyogoo.example.biz.toolbox.config;

import com.neyogoo.example.biz.common.constant.BizMqConstant;
import com.neyogoo.example.common.core.util.JsonUtils;
import com.neyogoo.example.common.log.event.SysOptLogListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 日志发送
 */
@Slf4j
@Configuration
public class ToolboxLogConfiguration {

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnExpression(
            "${example.log.sys-opt.enabled:true} && 'DB'.equals('${example.log.sys-opt.type:Log}')"
    )
    public SysOptLogListener sysOptLogListener(RabbitTemplate rabbitTemplate) {
        return new SysOptLogListener(
                msg -> rabbitTemplate.convertAndSend(BizMqConstant.SYS_OPT_LOG_QUEUE, JsonUtils.toJson(msg))
        );
    }
}
