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
@ConfigurationProperties(prefix = FileProperties.PREFIX)
public class FileProperties {

    public static final String PREFIX = BasicConstant.PROJECT_PREFIX + ".file";

    private String basePath;
    private String allowSuffix;
    private Long allowSize;
}
