package com.neyogoo.example.common.boot.aspect;

import com.neyogoo.example.common.boot.annotation.RepeatSubmit;
import com.neyogoo.example.common.cache.lock.DistributedLock;
import com.neyogoo.example.common.core.exception.BizException;
import com.neyogoo.example.common.core.exception.code.ExceptionCode;
import com.neyogoo.example.common.core.model.R;
import com.neyogoo.example.common.core.util.StrPool;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

import static com.neyogoo.example.common.core.constant.BasicConstant.REPEAT_SUBMIT_LOCK_PREFIX;
import static com.neyogoo.example.common.core.constant.ContextConstant.HEADER_TOKEN;

/**
 * 重复提交处理切面
 */
@Slf4j
@Aspect
public class RepeatSubmitAspect {

    @Autowired
    private DistributedLock distributedLock;

    /**
     * 定义切点
     */
    @Pointcut("@annotation(com.neyogoo.example.common.boot.annotation.RepeatSubmit)")
    public void pointcut() {

    }

    /**
     * 织入
     */
    @Around(value = "pointcut() && @annotation(repeatSubmit)", argNames = "joinPoint,repeatSubmit")
    public Object around(ProceedingJoinPoint joinPoint, RepeatSubmit repeatSubmit) throws Throwable {
        if (distributedLock == null) {
            throw BizException.wrap("开启 Redis 才能使用重复提交校验");
        }
        int lockSeconds = repeatSubmit.lockSecond();
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                .getRequest();
        // 基于Token
        String token = request.getHeader(HEADER_TOKEN);
        String path = request.getServletPath();
        String redisKey = getRedisKey(token, path);
        boolean isSuccess = distributedLock.lock(redisKey, lockSeconds * 1000L);
        if (!isSuccess) {
            return R.fail(ExceptionCode.BAD_REQUEST.getCode(), "重复请求，请您稍后再试.");

        }
        Object result;
        try {
            result = joinPoint.proceed();
        } finally {
            distributedLock.releaseLock(redisKey);
        }
        return result;
    }

    /**
     * 获取 Redis 缓存 Key，基于 token 及请求路径
     *
     * @param token 认证Token
     * @param path  请求路径
     * @return 返回重复提交缓存key
     */
    private String getRedisKey(String token, String path) {
        return REPEAT_SUBMIT_LOCK_PREFIX + token + StrPool.COLON + path;
    }
}
