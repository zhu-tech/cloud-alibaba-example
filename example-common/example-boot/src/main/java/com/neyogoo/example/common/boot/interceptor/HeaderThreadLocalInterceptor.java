package com.neyogoo.example.common.boot.interceptor;

import com.neyogoo.example.common.core.constant.ContextConstant;
import com.neyogoo.example.common.core.context.ContextUtils;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.AsyncHandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

import static com.neyogoo.example.common.core.util.WebUtils.getHeader;

/**
 * 将请求头数据，封装到 ThreadLocal
 */
@Slf4j
public class HeaderThreadLocalInterceptor implements AsyncHandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        ContextUtils.setToken(getHeader(request, ContextConstant.HEADER_TOKEN));
        ContextUtils.setOrgId(getHeader(request, ContextConstant.HEADER_ORG_ID));
        ContextUtils.setUserType(getHeader(request, ContextConstant.HEADER_USER_TYPE));
        ContextUtils.setUserId(getHeader(request, ContextConstant.HEADER_USER_ID));
        ContextUtils.setUserName(getHeader(request, ContextConstant.HEADER_USER_NAME));
        ContextUtils.setUserAccount(getHeader(request, ContextConstant.HEADER_USER_ACCOUNT));
        ContextUtils.setLoginPoint(getHeader(request, ContextConstant.HEADER_LOGIN_POINT));
        ContextUtils.setXFeign(getHeader(request, ContextConstant.HEADER_FEIGN));
        ContextUtils.setXTraceId(getHeader(request, ContextConstant.HEADER_TRACE_ID));
        ContextUtils.setXTransactionId(getHeader(request, ContextConstant.HEADER_TRANSACTION_ID));

        Map<String, String> localMap = ContextUtils.getLocalMap();
        localMap.forEach(MDC::put);

        return true;
    }


    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
                                Exception ex) {
        ContextUtils.remove();
    }
}
