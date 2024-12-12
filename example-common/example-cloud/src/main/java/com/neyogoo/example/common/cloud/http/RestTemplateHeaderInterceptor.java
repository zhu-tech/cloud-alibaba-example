package com.neyogoo.example.common.cloud.http;

import cn.hutool.core.util.ObjectUtil;
import com.neyogoo.example.common.core.context.ContextUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Map;

/**
 * 通过 RestTemplate 调用时，传递请求头和线程变量
 */
@Slf4j
@AllArgsConstructor
public class RestTemplateHeaderInterceptor implements ClientHttpRequestInterceptor {

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] bytes,
                                        ClientHttpRequestExecution execution) throws IOException {

        HttpHeaders httpHeaders = request.getHeaders();

        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes == null) {
            Map<String, String> localMap = ContextUtils.getLocalMap();
            localMap.forEach((key, value) -> {
                httpHeaders.add(key, value);
            });
            return execution.execute(request, bytes);
        }

        HttpServletRequest httpServletRequest = ((ServletRequestAttributes) requestAttributes).getRequest();
        if (httpServletRequest == null) {
            return execution.execute(request, bytes);
        }
        // 传递请求头
        HttpHeaders headers = request.getHeaders();
        if (headers != null) {
            headers.forEach((key, values) -> {
                String value = httpServletRequest.getHeader(key);
                value = ObjectUtil.isEmpty(value) ? ContextUtils.get(key) : value;
                httpHeaders.add(key, value);
            });
        }

        return execution.execute(request, bytes);
    }
}
