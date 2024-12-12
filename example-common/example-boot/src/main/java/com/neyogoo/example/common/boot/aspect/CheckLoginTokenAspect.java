package com.neyogoo.example.common.boot.aspect;

import com.neyogoo.example.common.boot.annotation.CheckLoginToken;
import com.neyogoo.example.common.core.exception.code.ExceptionCode;
import com.neyogoo.example.common.core.model.R;
import com.neyogoo.example.common.token.enumeration.UserTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

import static com.neyogoo.example.common.core.constant.ContextConstant.HEADER_USER_TYPE;

/**
 * 验证处理切面
 */
@Slf4j
@Aspect
public class CheckLoginTokenAspect {

    /**
     * 定义切点
     */
    @Pointcut("@annotation(com.neyogoo.example.common.boot.annotation.CheckLoginToken) "
            + "|| @within(com.neyogoo.example.common.boot.annotation.CheckLoginToken)")
    public void pointcut() {

    }

    /**
     * 织入
     */
    @Around("pointcut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        // 获取注解值，先在方法上找，再在类上找
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        CheckLoginToken annotation = signature.getMethod().getAnnotation(CheckLoginToken.class);
        if (annotation == null) {
            annotation = AnnotationUtils.findAnnotation(
                    signature.getMethod().getDeclaringClass(), CheckLoginToken.class
            );
        }
        if (annotation == null || !annotation.check()) {
            return joinPoint.proceed();
        }

        // 检查用户类型
        UserTypeEnum[] userTypes = annotation.requireUserType();
        if (userTypes != null && userTypes.length > 0) {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                    .getRequest();
            UserTypeEnum userType = UserTypeEnum.get(request.getHeader(HEADER_USER_TYPE));
            if (userType == null || Arrays.stream(userTypes).noneMatch(o -> o == userType)) {
                return annotation.silenceMode() ? generalSilenceResult(annotation)
                        : R.fail(ExceptionCode.BAD_REQUEST.getCode(), "非此用户权限接口");
            }
        }
        return joinPoint.proceed();
    }

    /**
     * 返回响应码为 200 的空值
     */
    private R generalSilenceResult(CheckLoginToken annotation) {
        CheckLoginToken.SilenceResult silenceResult = annotation.silenceResult();
        if (silenceResult == null) {
            return R.success(null);
        } else {
            return R.success(silenceResult.getResult().get(), "silence");
        }
    }
}
