package com.neyogoo.example.common.log;

import com.neyogoo.example.common.core.util.JsonUtils;
import com.neyogoo.example.common.log.aspect.SysOptLogAspect;
import com.neyogoo.example.common.log.event.SysOptLogListener;
import com.neyogoo.example.common.log.properties.OptLogProperties;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

import static com.neyogoo.example.common.core.constant.BasicConstant.LOG_PREFIX;

/**
 * 日志自动配置
 */
@Slf4j
@EnableAsync
@Configuration
@AllArgsConstructor
@ConditionalOnWebApplication
@EnableConfigurationProperties(OptLogProperties.class)
public class LogAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnExpression("${example.log.sys-opt.enabled:true}")
    public SysOptLogAspect sysOptLogAspect() {
        return new SysOptLogAspect();
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnExpression("${example.log.sys-opt.enabled:true} "
            + "&& 'Log'.equals('${example.log.sys-opt.type:Log}')")
    @ConditionalOnProperty(prefix = LOG_PREFIX, name = "type", havingValue = "Log", matchIfMissing = true)
    public SysOptLogListener sysOptLogListener() {
        log.info("加载 SysOptLogListener，type = Log");
        return new SysOptLogListener(msg -> log.info("SysOptLog = {}", JsonUtils.toJson(msg)));
    }
}
