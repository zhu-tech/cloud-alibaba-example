package com.neyogoo.example.common.core.exception;

import com.neyogoo.example.common.core.exception.code.ExceptionCode;

/**
 * 业务异常（抛出后不打印堆栈信息）
 */
public class SilenceException extends BizException {

    public SilenceException(int code, String message) {
        super(code, message);
    }

    public SilenceException(int code, String message, Throwable cause) {
        super(code, message, cause);
    }

    public static SilenceException wrap(String msg) {
        return new SilenceException(ExceptionCode.INTERNAL_SERVER_ERROR.getCode(), msg);
    }

    @Override
    public String toString() {
        return "SilenceException [message=" + getMessage() + ", code=" + getCode() + "]";
    }
}
