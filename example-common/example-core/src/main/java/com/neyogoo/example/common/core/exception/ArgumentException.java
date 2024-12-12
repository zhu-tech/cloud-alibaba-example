package com.neyogoo.example.common.core.exception;


import com.neyogoo.example.common.core.exception.code.ExceptionCode;

/**
 * 业务参数异常
 */
public class ArgumentException extends BizException {

    private static final long serialVersionUID = 1L;

    public ArgumentException(Throwable cause) {
        super(cause);
    }

    public ArgumentException(String message) {
        super(ExceptionCode.BAD_REQUEST.getCode(), message);
    }

    public ArgumentException(String message, Throwable cause) {
        super(ExceptionCode.BAD_REQUEST.getCode(), message, cause);
    }

    public ArgumentException(final String format, Object... args) {
        super(ExceptionCode.BAD_REQUEST.getCode(), format, args);
    }

    @Override
    public String toString() {
        return "ArgumentException [message=" + getMessage() + ", code=" + getCode() + "]";
    }

}
