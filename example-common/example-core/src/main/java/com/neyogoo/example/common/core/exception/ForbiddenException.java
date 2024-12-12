package com.neyogoo.example.common.core.exception;

import com.neyogoo.example.common.core.exception.code.BaseExceptionCode;
import com.neyogoo.example.common.core.exception.code.ExceptionCode;

/**
 * 403 禁止访问
 */
public class ForbiddenException extends BizException {

    private static final long serialVersionUID = 1L;

    public ForbiddenException(int code, String message) {
        super(code, message);
    }

    public ForbiddenException(int code, String message, Object... args) {
        super(code, message, args);
    }

    public static ForbiddenException wrap(BaseExceptionCode ex) {
        return new ForbiddenException(ex.getCode(), ex.getMsg());
    }

    public static ForbiddenException wrap(String msg) {
        return new ForbiddenException(ExceptionCode.FORBIDDEN.getCode(), msg);
    }

    public static ForbiddenException wrap(String msg, Object... args) {
        return new ForbiddenException(ExceptionCode.FORBIDDEN.getCode(), msg, args);
    }

    @Override
    public String toString() {
        return "ForbiddenException [message=" + getMessage() + ", code=" + getCode() + "]";
    }

}
