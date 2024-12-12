package com.neyogoo.example.common.cloud.interceptor;

import cn.hutool.core.net.URLEncodeUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.URLUtil;
import com.google.common.collect.ImmutableList;
import com.neyogoo.example.common.core.constant.ContextConstant;
import com.neyogoo.example.common.core.context.ContextUtils;
import com.neyogoo.example.common.core.util.StrPool;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * feign client 拦截器， 实现将 feign 调用方的 请求头封装到 被调用方的请求头
 */
@Slf4j
public class FeignAddHeaderRequestInterceptor implements RequestInterceptor {

    public static final List<String> HEADER_NAME_LIST = ImmutableList.of(
            ContextConstant.HEADER_TOKEN,
            ContextConstant.AUTHORIZATION_KEY,
            ContextConstant.HEADER_ORG_ID,
            ContextConstant.HEADER_USER_ID,
            ContextConstant.HEADER_USER_NAME,
            ContextConstant.HEADER_USER_ACCOUNT,
            ContextConstant.HEADER_USER_TYPE,
            ContextConstant.HEADER_LOGIN_POINT,
            ContextConstant.HEADER_FEIGN,
            ContextConstant.HEADER_TRACE_ID,
            ContextConstant.HEADER_TRANSACTION_ID,
            "X-Real-IP", "x-forwarded-for"
    );

    public FeignAddHeaderRequestInterceptor() {
        super();
    }

    @Override
    public void apply(RequestTemplate template) {
        template.removeHeader(ContextConstant.HEADER_FEIGN);
        template.header(ContextConstant.HEADER_FEIGN, StrPool.TRUE);
        log.info("thread id = {}, name = {}", Thread.currentThread().getId(), Thread.currentThread().getName());
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes == null) {
            Map<String, String> localMap = ContextUtils.getLocalMap();
            localMap.forEach((key, value) -> template.header(key, URLUtil.encode(value)));
            return;
        }

        HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
        if (request == null) {
            return;
        }
        // 传递请求头
        HEADER_NAME_LIST.forEach(headerName -> {
            String header = request.getHeader(headerName);
            template.header(headerName, ObjectUtil.isEmpty(header)
                    ? URLEncodeUtil.encode(ContextUtils.get(headerName)) : header);
        });
    }
}
