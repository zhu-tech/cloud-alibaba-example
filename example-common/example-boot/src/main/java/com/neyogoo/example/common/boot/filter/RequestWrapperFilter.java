package com.neyogoo.example.common.boot.filter;

import com.neyogoo.example.common.boot.wrapper.RequestWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Slf4j
@Order(2)
@Component
public class RequestWrapperFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        String contentType = request.getContentType();
        if (contentType == null || contentType.startsWith("multipart/")) {
            // 表单请求、文件上传类型
            chain.doFilter(request, response);
            return;
        }
        chain.doFilter(new RequestWrapper((HttpServletRequest) request), response);
    }
}