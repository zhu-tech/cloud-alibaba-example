package com.neyogoo.example.common.xss.filter;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.CharUtil;
import cn.hutool.core.util.StrUtil;
import com.neyogoo.example.common.xss.wrapper.XssRequestWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

@Slf4j
public class XssFilter implements Filter {

    /**
     * 可放行的请求路径
     */
    public static final String IGNORE_PATH = "ignorePath";
    /**
     * 可放行的参数值
     */
    public static final String IGNORE_PARAM_VALUE = "ignoreParamValue";
    private static final AntPathMatcher ANT_PATH_MATCHER = new AntPathMatcher();

    /**
     * 可放行的请求路径列表
     */
    private List<String> ignorePathList;
    /**
     * 可放行的参数值列表
     */
    private List<String> ignoreParamValueList;

    @Override
    public void init(FilterConfig fc) {
        this.ignorePathList = StrUtil.split(fc.getInitParameter(IGNORE_PATH), CharUtil.COMMA);
        this.ignoreParamValueList = StrUtil.split(fc.getInitParameter(IGNORE_PARAM_VALUE), CharUtil.COMMA);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        // 判断uri是否包含项目名称
        String uriPath = ((HttpServletRequest) request).getRequestURI();
        if (isIgnorePath(uriPath)) {
            log.debug("忽略过滤路径 = [{}]", uriPath);
            chain.doFilter(request, response);
            return;
        }
        log.debug("过滤器包装请求路径 = [{}]", uriPath);
        chain.doFilter(new XssRequestWrapper((HttpServletRequest) request, ignoreParamValueList), response);
    }

    private boolean isIgnorePath(String uriPath) {
        if (StrUtil.isBlank(uriPath)) {
            return true;
        }
        if (CollUtil.isEmpty(ignorePathList)) {
            return false;
        }
        return ignorePathList.stream().anyMatch(url -> uriPath.startsWith(url) || ANT_PATH_MATCHER.match(url, uriPath));
    }
}
