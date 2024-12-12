package com.neyogoo.example.common.mq;

import com.neyogoo.example.common.core.constant.BasicConstant;
import com.neyogoo.example.common.mq.listener.CustomerHeaderProcess;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * rabbit 禁用配置
 */
@Configuration
@Import(CustomRabbitMqConfiguration.RabbitMqConfiguration.class)
public class CustomRabbitMqConfiguration {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Slf4j
    @Configuration
    @ConditionalOnProperty(prefix = BasicConstant.MQ_PREFIX, name = "enabled", havingValue = "false",
            matchIfMissing = true)
    @EnableAutoConfiguration(exclude = {RabbitAutoConfiguration.class})
    public static class RabbitMqConfiguration {
        public RabbitMqConfiguration() {
            log.warn("检测到 {}.enabled = false，排除了 RabbitMQ", BasicConstant.MQ_PREFIX);
        }
    }

    /**
     * 自定义的消息头
     */
    @Bean
    public void messagePostProcessor() {
        rabbitTemplate.addBeforePublishPostProcessors(new CustomerHeaderProcess());
    }
}
