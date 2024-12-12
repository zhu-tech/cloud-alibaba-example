package com.neyogoo.example.common.mq.properties;

import com.neyogoo.example.common.core.constant.BasicConstant;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;


/**
 * 操作日志配置类
 */
@Data
@NoArgsConstructor
@ConfigurationProperties(prefix = BasicConstant.MQ_PREFIX)
public class MqProperties {

    /**
     * 是否启用
     */
    private Boolean enabled = true;

}
