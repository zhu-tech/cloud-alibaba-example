package com.neyogoo.example.common.boot.aspect;

import cn.hutool.core.collection.CollUtil;
import com.google.common.collect.Lists;
import com.neyogoo.example.common.boot.annotation.CheckSignature;
import com.neyogoo.example.common.boot.util.SignUtils;
import com.neyogoo.example.common.boot.wrapper.RequestWrapper;
import com.neyogoo.example.common.core.constant.ContextConstant;
import com.neyogoo.example.common.core.exception.code.ExceptionCode;
import com.neyogoo.example.common.core.model.R;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.HttpMethod;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * 请求接口验签切面处理
 */
@Slf4j
@Aspect
public class CheckSignatureAspect {

    /**
     * 定义切点
     */
    @Pointcut("@annotation(com.neyogoo.example.common.boot.annotation.CheckSignature) "
            + "|| @within(com.neyogoo.example.common.boot.annotation.CheckSignature)")
    public void pointcut() {

    }

    /**
     * 织入
     */
    @Around("pointcut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        // 获取注解值，先在方法上找，再在类上找
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        CheckSignature annotation = signature.getMethod().getAnnotation(CheckSignature.class);
        if (annotation == null) {
            annotation = AnnotationUtils.findAnnotation(
                    signature.getMethod().getDeclaringClass(), CheckSignature.class
            );
        }
        if (annotation == null || !annotation.check()) {
            return joinPoint.proceed();
        }

        HttpServletRequest request = ((ServletRequestAttributes)
                Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();

        String sign = request.getHeader(ContextConstant.HEADER_SIGN);
        String nonce = request.getHeader(ContextConstant.HEADER_NONCE);
        String timestamp = request.getHeader(ContextConstant.HEADER_TIMESTAMP);
        if (StringUtils.isAnyBlank(sign, nonce, timestamp)) {
            log.warn("check signature error, sign = {}, nonce = {}, timestamp = {}", sign, nonce, timestamp);
            return R.fail(ExceptionCode.BAD_REQUEST.getCode(), "签名验证异常，缺少验证参数");
        }

        // 获取 PathVariable 参数
        List<String> pathVariables = Lists.newArrayList();
        ServletWebRequest webRequest = new ServletWebRequest(request, null);
        Map<String, String> uriTemplateVars = (Map<String, String>) webRequest.getAttribute(
                HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE, RequestAttributes.SCOPE_REQUEST
        );
        if (CollUtil.isNotEmpty(uriTemplateVars)) {
            uriTemplateVars.keySet().forEach(e -> pathVariables.add(uriTemplateVars.get(e)));
            Collections.sort(pathVariables);
        }
        // 获取 Parameter 参数 排序
        SortedMap<String, String> paramsMap = new TreeMap<>();
        Enumeration paramEnumeration = request.getParameterNames();
        while (paramEnumeration.hasMoreElements()) {
            String paramName = (String) paramEnumeration.nextElement();
            String paramVal = request.getParameter(paramName);
            if (StringUtils.isNotBlank(paramVal)) {
                paramsMap.put(paramName, paramVal);
            }
        }
        // 获取 Body 参数
        String requestBody = request.getMethod().equalsIgnoreCase(HttpMethod.POST.name())
                || request.getMethod().equalsIgnoreCase(HttpMethod.PUT.name())
                ? new RequestWrapper(request).getBodyString() : "";

        SignUtils.SignParam signParam = new SignUtils.SignParam();
        signParam.setPathVariables(pathVariables);
        signParam.setParamsMap(paramsMap);
        signParam.setRequestBody(requestBody);
        signParam.setSign(sign);
        signParam.setNonce(nonce);
        signParam.setTimestamp(timestamp);
        if (!SignUtils.checkSign(signParam)) {
            return R.fail(ExceptionCode.BAD_REQUEST.getCode(), "签名验证异常，签名值不正确");
        }
        return joinPoint.proceed();
    }
}