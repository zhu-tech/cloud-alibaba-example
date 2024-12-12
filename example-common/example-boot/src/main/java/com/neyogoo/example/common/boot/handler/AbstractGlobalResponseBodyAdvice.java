package com.neyogoo.example.common.boot.handler;

import com.neyogoo.example.common.boot.annotation.IgnoreResponseBody;
import com.neyogoo.example.common.core.model.R;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * 全局响应体包装
 */
public class AbstractGlobalResponseBodyAdvice implements ResponseBodyAdvice {

    @Override
    public boolean supports(MethodParameter methodParameter, Class aClass) {
        // 类上如果被 IgnoreResponseBody 标识就不拦截
        if (methodParameter.getDeclaringClass().isAnnotationPresent(IgnoreResponseBody.class)) {
            return false;
        }
        // 方法上被标注也不拦截
        if (methodParameter.getMethod().isAnnotationPresent(IgnoreResponseBody.class)) {
            return false;
        }
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object o, MethodParameter methodParameter, MediaType mediaType, Class aClass,
                                  ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {
        if (o == null) {
            return null;
        }
        if (o instanceof R) {
            return o;
        }
        if (o instanceof ResponseEntity) {
            return o;
        }
        return R.success(o);
    }
}
