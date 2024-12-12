package com.neyogoo.example.common.log.aspect;

import com.neyogoo.example.common.core.util.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class BaseLogAspect {

    /**
     * 获取注解
     */
    protected static <T extends Annotation> T getTargetAnnotation(JoinPoint point, Class<T> annotationClass) {
        try {
            T annotation = null;
            if (point.getSignature() instanceof MethodSignature) {
                Method method = ((MethodSignature) point.getSignature()).getMethod();
                if (method != null) {
                    annotation = method.getAnnotation(annotationClass);
                }
            }
            return annotation;
        } catch (Exception e) {
            log.warn("获取 {}.{} 的 @{} 注解失败", point.getSignature().getDeclaringTypeName(),
                    point.getSignature().getName(), annotationClass.getName(), e);
            return null;
        }
    }

    /**
     * 获取请求入参
     */
    protected String getArgs(JoinPoint joinPoint) {
        List<Object> objectList = Arrays.stream(joinPoint.getArgs())
                .filter(object -> !(object instanceof BindingResult || object instanceof HttpServletRequest
                        || object instanceof HttpServletResponse || object instanceof MultipartFile))
                .collect(Collectors.toList());
        return JsonUtils.toJson(objectList);
    }
}
