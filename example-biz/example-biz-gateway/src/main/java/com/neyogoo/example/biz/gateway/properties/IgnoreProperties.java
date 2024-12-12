package com.neyogoo.example.biz.gateway.properties;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import com.neyogoo.example.biz.common.enumeration.gateway.HttpMethodEnum;
import com.neyogoo.example.common.core.constant.BasicConstant;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.util.AntPathMatcher;

import java.util.Map;
import java.util.Set;

/**
 * 忽略 token 配置类
 */
@Data
@ConfigurationProperties(prefix = IgnoreProperties.PREFIX)
public class IgnoreProperties {

    public static final String PREFIX = BasicConstant.PROJECT_PREFIX + ".ignore";
    private static final AntPathMatcher ANT_PATH_MATCHER = new AntPathMatcher();

    private Map<String, Set<String>> baseUri = MapUtil.<String, Set<String>>builder(
            HttpMethodEnum.ALL.name(),
            CollUtil.newHashSet(
                    "/**/*.css",
                    "/**/*.js",
                    "/**/*.html",
                    "/**/*.ico",
                    "/**/*.jpg",
                    "/**/*.jpeg",
                    "/**/*.png",
                    "/**/*.gif",
                    "/**/api-docs/**",
                    "/**/api-docs-ext/**",
                    "/**/swagger-resources/**",
                    "/**/webjars/**",
                    "/actuator/**",
                    "/**/static/**",
                    "/**/public/**",
                    "/error",
                    // 不需要租户编码、不需要登录、不需要权限即可访问的接口
                    "/**/anno/**",
                    "/**/druid/**")
    ).build();

    private Map<String, Set<String>> ignoreAuthority = baseUri;

    private Map<String, Set<String>> ignoreToken = baseUri;

    /**
     * 是否忽略 Authorization
     *
     * @param path 相对路径
     */
    public boolean isIgnoreAuthorization(String method, String path) {
        return isIgnore(method, path, ignoreAuthority);
    }

    /**
     * 是否忽略登录 Token
     *
     * @param path 相对路径
     */
    public boolean isIgnoreToken(String method, String path) {
        return isIgnore(method, path, ignoreToken);
    }

    private boolean isIgnore(String method, String path, Map<String, Set<String>> all) {
        for (Map.Entry<String, Set<String>> entry : all.entrySet()) {
            String m = entry.getKey();
            Set<String> paths = entry.getValue();
            if (HttpMethodEnum.ALL.name().equalsIgnoreCase(m)) {
                return paths.stream().anyMatch(url -> ANT_PATH_MATCHER.match(url, path));
            } else {
                return m.equalsIgnoreCase(method) && paths.stream().anyMatch(url -> ANT_PATH_MATCHER.match(url, path));
            }
        }
        return false;
    }
}
