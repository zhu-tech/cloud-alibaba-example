package com.neyogoo.example.common.boot.handler;

import cn.hutool.core.util.StrUtil;
import com.neyogoo.example.common.core.exception.ArgumentException;
import com.neyogoo.example.common.core.exception.BizException;
import com.neyogoo.example.common.core.exception.ForbiddenException;
import com.neyogoo.example.common.core.exception.SilenceException;
import com.neyogoo.example.common.core.exception.UnauthorizedException;
import com.neyogoo.example.common.core.exception.code.ExceptionCode;
import com.neyogoo.example.common.core.model.R;
import com.neyogoo.example.common.core.util.StrPool;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static com.neyogoo.example.common.core.exception.code.ExceptionCode.*;

/**
 * 全局异常统一处理
 */
@Slf4j
public abstract class AbstractGlobalExceptionHandler {

    @ExceptionHandler(BizException.class)
    public R<?> bizException(BizException ex) {
        log.warn("BizException:", ex);
        return R.result(ex.getCode(), null, ex.getMessage()).setPath(getPath());
    }

    @ExceptionHandler(ArgumentException.class)
    public R<?> argumentException(ArgumentException ex) {
        log.warn("ArgumentException:", ex);
        return R.result(ex.getCode(), null, ex.getMessage()).setPath(getPath());
    }

    @ExceptionHandler(ForbiddenException.class)
    public R<?> forbiddenException(ForbiddenException ex) {
        log.warn("ForbiddenException:", ex);
        return R.result(ex.getCode(), null, ex.getMessage()).setPath(getPath());
    }

    @ExceptionHandler(UnauthorizedException.class)
    public R<?> unauthorizedException(UnauthorizedException ex) {
        log.warn("UnauthorizedException:", ex);
        return R.result(ex.getCode(), null, ex.getMessage()).setPath(getPath());
    }

    @ExceptionHandler(SilenceException.class)
    public R<?> silenceException(SilenceException ex) {
        log.warn("SilenceException:{}", ex.getMessage());
        return R.result(ex.getCode(), null, ex.getMessage()).setPath(getPath());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public R<?> httpMessageNotReadableException(HttpMessageNotReadableException ex) {
        log.warn("HttpMessageNotReadableException:", ex);
        String message = ex.getMessage();
        if (StrUtil.containsAny(message, "Could not read document:")) {
            String msg = String.format("无法正确的解析json类型的参数：%s", StrUtil.subBetween(message,
                    "Could not read document:", " at "));
            return R.result(ExceptionCode.PARAM_EX.getCode(), null, msg).setPath(getPath());
        }
        return R.result(ExceptionCode.PARAM_EX.getCode(), null, ExceptionCode.PARAM_EX.getMsg())
                .setPath(getPath());
    }

    @ExceptionHandler(BindException.class)
    public R<?> bindException(BindException ex) {
        log.warn("BindException:", ex);
        try {
            String msg = Objects.requireNonNull(ex.getBindingResult().getFieldError()).getDefaultMessage();
            if (StrUtil.isNotEmpty(msg)) {
                return R.result(ExceptionCode.PARAM_EX.getCode(), null, msg).setPath(getPath());
            }
        } catch (Exception ee) {
            log.debug("获取异常描述失败", ee);
        }
        StringBuilder msg = new StringBuilder();
        List<FieldError> fieldErrors = ex.getFieldErrors();
        fieldErrors.forEach((oe) ->
                msg.append("参数:[").append(oe.getObjectName())
                        .append(".").append(oe.getField())
                        .append("]的传入值:[").append(oe.getRejectedValue()).append("]与预期的字段类型不匹配.")
        );
        return R.result(ExceptionCode.PARAM_EX.getCode(), null, msg.toString()).setPath(getPath());
    }


    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public R<?> methodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex) {
        log.warn("MethodArgumentTypeMismatchException:", ex);
        String msg = "参数：[" + ex.getName() + "]的传入值：[" + ex.getValue()
                + "]与预期的字段类型：[" + Objects.requireNonNull(ex.getRequiredType()).getName() + "]不匹配";
        return R.result(ExceptionCode.PARAM_EX.getCode(), null, msg).setPath(getPath());
    }

    @ExceptionHandler(IllegalStateException.class)
    public R<?> illegalStateException(IllegalStateException ex) {
        log.warn("IllegalStateException:", ex);
        return R.result(BAD_REQUEST.getCode(), null, "无效参数异常").setPath(getPath());
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public R<?> missingServletRequestParameterException(MissingServletRequestParameterException ex) {
        log.warn("MissingServletRequestParameterException:", ex);
        return R.result(BAD_REQUEST.getCode(), null,
                        "缺少必须的[" + ex.getParameterType() + "]类型的参数[" + ex.getParameterName() + "]")
                .setPath(getPath());
    }

    @ExceptionHandler(NullPointerException.class)
    public R<?> nullPointerException(NullPointerException ex) {
        log.warn("NullPointerException:", ex);
        return R.result(ExceptionCode.INTERNAL_SERVER_ERROR.getCode(), null, "空指针异常")
                .setPath(getPath());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public R<?> illegalArgumentException(IllegalArgumentException ex) {
        log.warn("IllegalArgumentException:", ex);
        return R.result(BAD_REQUEST.getCode(), null, "无效参数异常").setPath(getPath());
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public R<?> httpMediaTypeNotSupportedException(HttpMediaTypeNotSupportedException ex) {
        log.warn("HttpMediaTypeNotSupportedException:", ex);
        MediaType contentType = ex.getContentType();
        if (contentType != null) {
            return R.result(BAD_REQUEST.getCode(), null,
                            "请求类型(Content-Type)[" + contentType + "] 与实际接口的请求类型不匹配")
                    .setPath(getPath());
        }
        return R.result(BAD_REQUEST.getCode(), null, "无效的Content-Type类型")
                .setPath(getPath());
    }

    @ExceptionHandler(MissingServletRequestPartException.class)
    public R<?> missingServletRequestPartException(MissingServletRequestPartException ex) {
        log.warn("MissingServletRequestPartException:", ex);
        return R.result(BAD_REQUEST.getCode(), null, "请求中必须至少包含一个有效文件")
                .setPath(getPath());
    }

    @ExceptionHandler(ServletException.class)
    public R<?> servletException(ServletException ex) {
        log.warn("ServletException:", ex);
        String msg = "UT010016: Not a multi part request";
        if (msg.equalsIgnoreCase(ex.getMessage())) {
            return R.result(BAD_REQUEST.getCode(), null, "请求中必须至少包含一个有效文件");
        }
        return R.result(ExceptionCode.SYSTEM_BUSY.getCode(), null, ex.getMessage())
                .setPath(getPath());
    }

    @ExceptionHandler(MultipartException.class)
    public R<?> multipartException(MultipartException ex) {
        log.warn("MultipartException:", ex);
        return R.result(BAD_REQUEST.getCode(), null, "请求中必须至少包含一个有效文件")
                .setPath(getPath());
    }

    /**
     * jsr 规范中的验证异常
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public R<?> constraintViolationException(ConstraintViolationException ex) {
        log.warn("ConstraintViolationException:", ex);
        Set<ConstraintViolation<?>> violations = ex.getConstraintViolations();
        String message = violations.stream().map(ConstraintViolation::getMessage)
                .collect(Collectors.joining(";"));
        return R.result(BAD_REQUEST.getCode(), null, message).setPath(getPath());
    }

    /**
     * spring 封装的参数验证异常， 在 controller 中没有写 result 参数时，会进入
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public R<?> methodArgumentNotValidException(MethodArgumentNotValidException ex) {
        log.warn("MethodArgumentNotValidException:", ex);
        return R.result(BAD_REQUEST.getCode(),
                null,
                Objects.requireNonNull(ex.getBindingResult().getFieldError()).getDefaultMessage()
        ).setPath(getPath());
    }

    @ExceptionHandler(Exception.class)
    public R<?> otherExceptionHandler(Exception ex) {
        log.warn("Exception:", ex);
        if (ex.getCause() instanceof BizException) {
            return this.bizException((BizException) ex.getCause());
        }
        return R.result(
                ExceptionCode.SYSTEM_BUSY.getCode(),
                null,
                ExceptionCode.SYSTEM_BUSY.getMsg()
        ).setPath(getPath());
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public R<?> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException ex) {
        log.warn("HttpRequestMethodNotSupportedException:", ex);
        return R.result(METHOD_NOT_ALLOWED.getCode(), null, METHOD_NOT_ALLOWED.getMsg())
                .setPath(getPath());
    }

    @ExceptionHandler(SQLException.class)
    public R<?> sqlException(SQLException ex) {
        log.warn("SQLException:", ex);
        return R.result(INTERNAL_SERVER_ERROR.getCode(), null, "运行SQL出现异常")
                .setPath(getPath());
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public R<?> dataIntegrityViolationException(DataIntegrityViolationException ex) {
        log.warn("DataIntegrityViolationException:", ex);
        return R.result(INTERNAL_SERVER_ERROR.getCode(), null, "运行SQL出现异常")
                .setPath(getPath());
    }

    private String getPath() {
        String path = StrPool.EMPTY;
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes != null) {
            HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
            path = request.getRequestURI();
        }
        return path;
    }
}
