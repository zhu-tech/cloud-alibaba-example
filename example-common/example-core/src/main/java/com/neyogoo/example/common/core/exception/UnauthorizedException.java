package com.neyogoo.example.common.core.exception;


import com.neyogoo.example.common.core.exception.code.ExceptionCode;

/**
 * 401 未认证 未登录
 */
public class UnauthorizedException extends BizException {

    private static final long serialVersionUID = 1L;

    public UnauthorizedException(int code, String message) {
        super(code, message);
    }

    public UnauthorizedException(int code, String message, Throwable cause) {
        super(code, message, cause);
    }

    public static UnauthorizedException wrap(String msg) {
        return new UnauthorizedException(ExceptionCode.UNAUTHORIZED.getCode(), msg);
    }

    public static UnauthorizedException wrap(ExceptionCode ec) {
        return new UnauthorizedException(ec.getCode(), ec.getMsg());
    }

    @Override
    public String toString() {
        return "UnauthorizedException [message=" + getMessage() + ", code=" + getCode() + "]";
    }

}
