package com.neyogoo.example.biz.toolbox.config.properties;

import com.neyogoo.example.common.core.constant.BasicConstant;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = SmsProperties.PREFIX)
public class SmsProperties {

    public static final String PREFIX = BasicConstant.ALIYUN_PREFIX + ".sms";

    private String product;
    private String domain;
    private String accessKeyId;
    private String accessKeySecret;
    private String signName;
    private String regionId;
}
