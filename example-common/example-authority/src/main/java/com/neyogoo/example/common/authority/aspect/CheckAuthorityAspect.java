package com.neyogoo.example.common.authority.aspect;

import cn.hutool.core.text.CharSequenceUtil;
import com.neyogoo.example.common.authority.annotation.CheckAuthority;
import com.neyogoo.example.common.authority.permission.AuthorityFunction;
import com.neyogoo.example.common.core.constant.ContextConstant;
import com.neyogoo.example.common.core.exception.SilenceException;
import com.neyogoo.example.common.core.exception.code.ExceptionCode;
import com.neyogoo.example.common.core.util.SpringUtils;
import com.neyogoo.example.common.core.util.StrPool;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.expression.BeanFactoryResolver;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.MethodParameter;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.annotation.SynthesizingMethodParameter;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;


@Slf4j
@Aspect
public class CheckAuthorityAspect {

    /**
     * 表达式处理
     */
    private static final ExpressionParser SP_EL_PARSER = new SpelExpressionParser();
    private static final ParameterNameDiscoverer PARAMETER_NAME_DISCOVERER = new DefaultParameterNameDiscoverer();
    private final AuthorityFunction authorityFunction;

    public CheckAuthorityAspect(AuthorityFunction authorityFunction) {
        this.authorityFunction = authorityFunction;
    }

    /**
     * 定义切点
     */
    @Pointcut("@annotation(com.neyogoo.example.common.authority.annotation.CheckAuthority) "
            + "|| @within(com.neyogoo.example.common.authority.annotation.CheckAuthority)")
    public void pointcut() {

    }

    /**
     * 织入
     */
    @Around("pointcut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        checkAuthority(joinPoint);
        return joinPoint.proceed();
    }

    /**
     * 处理权限
     */
    private void checkAuthority(ProceedingJoinPoint joinPoint) {
        // 获取注解值，先在方法上找，再在类上找
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        CheckAuthority annotation = method.getAnnotation(CheckAuthority.class);
        if (annotation == null) {
            annotation = AnnotationUtils.findAnnotation(
                    signature.getMethod().getDeclaringClass(), CheckAuthority.class
            );
        }

        // feign 远程调用无需验证
        if (fromFeign()) {
            return;
        }

        String condition = annotation.value();
        if (CharSequenceUtil.isBlank(condition)) {
            return;
        }
        if (!hasPermit(joinPoint, method, condition)) {
            log.warn(String.format("执行方法[%s]需要[%s]权限",
                    method.getDeclaringClass().getName() + "." + method.getName(), condition));
            throw new SilenceException(ExceptionCode.FORBIDDEN.getCode(), "缺少接口执行权限");
        }
    }

    private boolean fromFeign() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes == null) {
            return false;
        }
        HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
        return StrPool.TRUE.equals(request.getHeader(ContextConstant.HEADER_FEIGN));
    }

    private Boolean hasPermit(ProceedingJoinPoint point, Method method, String condition) {
        StandardEvaluationContext context = new StandardEvaluationContext(authorityFunction);
        context.setBeanResolver(new BeanFactoryResolver(SpringUtils.getApplicationContext()));
        // 方法参数值
        Object[] args = point.getArgs();
        for (int i = 0; i < args.length; i++) {
            MethodParameter parameter = new SynthesizingMethodParameter(method, i);
            parameter.initParameterNameDiscovery(PARAMETER_NAME_DISCOVERER);
            context.setVariable(parameter.getParameterName(), args[i]);
        }
        return SP_EL_PARSER.parseExpression(condition).getValue(context, Boolean.class);
    }
}
