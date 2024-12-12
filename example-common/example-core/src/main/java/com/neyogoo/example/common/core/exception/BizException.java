package com.neyogoo.example.common.core.exception;

import com.neyogoo.example.common.core.exception.code.BaseExceptionCode;
import com.neyogoo.example.common.core.exception.code.ExceptionCode;

/**
 * 业务异常
 */
public class BizException extends BaseUncheckedException {

    private static final long serialVersionUID = 1L;

    public BizException(Throwable cause) {
        super(cause);
    }

    public BizException(int code, Throwable cause) {
        super(code, cause);
    }

    public BizException(String message) {
        super(-1, message);
    }

    public BizException(String message, Throwable cause) {
        super(-1, message, cause);
    }

    public BizException(int code, String message) {
        super(code, message);
    }

    public BizException(int code, String message, Throwable cause) {
        super(code, message, cause);
    }

    public BizException(int code, String message, Object... args) {
        super(code, message, args);
    }

    /**
     * 实例化异常
     *
     * @param code    自定义异常编码
     * @param message 自定义异常消息
     * @param args    自定义异常参数
     */
    public static BizException wrap(int code, String message, Object... args) {
        return new BizException(code, message, args);
    }

    public static BizException wrap(String message, Object... args) {
        return new BizException(ExceptionCode.INTERNAL_SERVER_ERROR.getCode(), message, args);
    }

    public static BizException validFail(String message, Object... args) {
        return new BizException(ExceptionCode.BAD_REQUEST.getCode(), message, args);
    }

    public static BizException wrap(BaseExceptionCode ex) {
        throw new BizException(ex.getCode(), ex.getMsg());
    }

    public static BizException wrap(BaseExceptionCode ex, String message) {
        throw new BizException(ex.getCode(), message);
    }

    public static BizException wrap(BaseExceptionCode ex, String message, Object... args) {
        throw new BizException(ex.getCode(), message, args);
    }

    public static BizException wrap(BaseExceptionCode ex, Throwable cause) {
        return new BizException(ex.getCode(), ex.getMsg(), cause);
    }

    @Override
    public String toString() {
        return "BizException [message=" + getMessage() + ", code=" + getCode() + "]";
    }

}
