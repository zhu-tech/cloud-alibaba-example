package com.neyogoo.example.common.log.aspect;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.util.StrUtil;
import com.neyogoo.example.common.core.context.ContextUtils;
import com.neyogoo.example.common.core.exception.BizException;
import com.neyogoo.example.common.core.model.R;
import com.neyogoo.example.common.core.util.SpringUtils;
import com.neyogoo.example.common.core.util.StrPool;
import com.neyogoo.example.common.core.util.WebUtils;
import com.neyogoo.example.common.log.annotation.SysOptLog;
import com.neyogoo.example.common.log.event.SysOptLogEvent;
import com.neyogoo.example.common.log.model.SysOptLogMsg;
import io.swagger.annotations.ApiOperation;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.function.Consumer;

@Slf4j
@Aspect
public class SysOptLogAspect extends BaseLogAspect {

    public static final int MAX_LENGTH = 65535;
    private static final ThreadLocal<SysOptLogMsg> THREAD_LOCAL = new ThreadLocal<>();

    @Pointcut("@annotation(com.neyogoo.example.common.log.annotation.SysOptLog)")
    public void pointcut() {
    }

    /**
     * 执行方法之前
     *
     * @param joinPoint 端点
     */
    @Before("pointcut()")
    public void doBefore(JoinPoint joinPoint) {
        tryCatch(p -> {
            SysOptLog annotation = getTargetAnnotation(joinPoint, SysOptLog.class);
            buildMessage(joinPoint, annotation);
        });
    }

    /**
     * 返回通知
     *
     * @param ret       返回值
     * @param joinPoint 端点
     */
    @AfterReturning(pointcut = "pointcut()", returning = "ret")
    public void doAfterReturning(JoinPoint joinPoint, Object ret) {
        tryCatch(p -> {
            SysOptLog myOperationLog = getTargetAnnotation(joinPoint, SysOptLog.class);
            SysOptLogMsg message = getBaseLogMessage();
            message.setResponseTime(LocalDateTime.now());
            message.updateConsumingTime();
            putUserInfoToMsg(message);
            R r = Convert.convert(R.class, ret);
            if (r != null && !r.getIsSuccess()) {
                message.setResultType(SysOptLogMsg.ResultType.Fail);
                message.setResultContent(StrUtil.sub(r.getMsg(), 0, MAX_LENGTH));
                sendOperationLog(message);
                return;
            }
            message.setResultType(SysOptLogMsg.ResultType.Success);
            if (myOperationLog.response()) {
                if (r == null) {
                    message.setResultContent(
                            StrUtil.sub(String.valueOf(ret == null ? StrPool.EMPTY : ret),
                                    0, MAX_LENGTH));
                } else {
                    message.setResultContent(StrUtil.sub(r.toString(), 0, MAX_LENGTH));
                }
            }
            sendOperationLog(message);
        });
    }

    /**
     * 异常通知
     *
     * @param joinPoint 端点
     * @param e         异常
     */
    @AfterThrowing(pointcut = "pointcut()", throwing = "e")
    public void doAfterThrowable(JoinPoint joinPoint, Throwable e) {
        tryCatch(p -> {
            SysOptLogMsg message = getBaseLogMessage();
            message.setResponseTime(LocalDateTime.now());
            message.updateConsumingTime();
            putUserInfoToMsg(message);
            message.setResultType(SysOptLogMsg.ResultType.Exception);
            message.setResultContent(ExceptionUtil.stacktraceToString(e, MAX_LENGTH));
            sendOperationLog(message);
        });
    }

    /**
     * 发送操作日志到消息队列
     */
    private void sendOperationLog(SysOptLogMsg msg) {
        SpringUtils.publishEvent(new SysOptLogEvent(msg));
    }

    private void tryCatch(Consumer<String> consumer) {
        try {
            consumer.accept("");
        } catch (Exception e) {
            log.warn("记录系统操作日志异常", e);
            THREAD_LOCAL.remove();
        }
    }

    @SneakyThrows
    private SysOptLogMsg buildMessage(JoinPoint joinPoint, SysOptLog myOperationLog) {
        HttpServletRequest request = ((ServletRequestAttributes)
                Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();

        SysOptLogMsg message = getBaseLogMessage();
        message.setRequestIp(WebUtils.getIpAddr(request));
        message.setRequestUrl(request.getRequestURI());
        message.setHttpMethod(request.getMethod());
        message.setTraceId(ContextUtils.getXTraceId());
        message.setUa(StrUtil.sub(request.getHeader("user-agent"), 0, 500));
        message.setRequestTime(LocalDateTime.now());
        if (myOperationLog.request()) {
            message.setRequestParams(getArgs(joinPoint));
        }

        ApiOperation apiOperation = getTargetAnnotation(joinPoint, ApiOperation.class);
        if (apiOperation != null) {
            message.setOperateExplain(apiOperation.value());
        }

        // 获取切入点方法签名对象，（在此处应该获取到被拦截的方法）
        Signature signature = joinPoint.getSignature();
        if (!(signature instanceof MethodSignature)) {
            throw BizException.wrap("签名方法获取异常，中断连接");
        }
        // 将签名对象强制转换成方法签名对象
        MethodSignature methodSignature = (MethodSignature) signature;
        // 获取被代理的对象
        Object target = joinPoint.getTarget();

        message.setClassName(target.getClass().toString());
        message.setMethodName(methodSignature.getName());

        return message;
    }

    private SysOptLogMsg getBaseLogMessage() {
        SysOptLogMsg message = THREAD_LOCAL.get();
        if (message == null) {
            message = new SysOptLogMsg();
            THREAD_LOCAL.set(message);
        }
        return message;
    }

    /**
     * 向消息里放入用户信息，放在接口执行后，兼容登录
     */
    private void putUserInfoToMsg(SysOptLogMsg message) {
        message.setUserType(ContextUtils.getUserType());
        message.setUserId(ContextUtils.getUserId());
        message.setUserName(ContextUtils.getUserName());
        if (StringUtils.isBlank(message.getUserType())) {
            message.setUserType("SysRobot");
        }
    }
}
