package com.neyogoo.example.common.xss.properties;

import cn.hutool.core.collection.CollUtil;
import com.neyogoo.example.common.core.constant.BasicConstant;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * 忽略XSS 配置类
 */
@Data
@ConfigurationProperties(prefix = XssProperties.PREFIX)
public class XssProperties {

    public static final String PREFIX = BasicConstant.PROJECT_PREFIX + ".xss";
    private Boolean enabled = true;
    /**
     * 是否启用 RequestBody 注解标记的参数 反序列化时过滤XSS
     */
    private Boolean requestBodyEnabled = false;
    private int order = 1;
    private List<String> patterns = CollUtil.newArrayList("/*");
    private List<String> ignorePaths = CollUtil.newArrayList(
            "favicon.ico",
            "/**/doc.html",
            "/**/swagger-ui.html",
            "/csrf",
            "/webjars/**",
            "/swagger-resources/**",
            "/resources/**",
            "/static/**",
            "/public/**",
            "/classpath:*",
            "/actuator/**",
            "/**/noxss/**",
            "/**/activiti/**",
            "/**/service/model/**",
            "/**/service/editor/**"
    );
    private List<String> ignoreParamValues = CollUtil.newArrayList("noxss");
}
