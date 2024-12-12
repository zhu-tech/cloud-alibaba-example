package com.neyogoo.example.common.core.exception.code;


/**
 * 异常编码
 * <p>
 * 1**  信息，服务器收到请求，需要请求者继续执行操作
 * 2**  成功，操作被成功接收并处理
 * 3**  重定向，需要进一步的操作以完成请求
 * 4**  客户端错误，请求包含语法错误或无法完成请求
 * 5**  服务器错误，服务器在处理请求的过程中发生了错误
 */
public enum ExceptionCode implements BaseExceptionCode {

    SUCCESS(200, "成功"),
    BAD_REQUEST(400, "错误的请求"),
    UNAUTHORIZED(401, "未认证"),
    FORBIDDEN(403, "禁止访问"),
    NOT_FOUND(404, "没有找到资源"),
    METHOD_NOT_ALLOWED(405, "不支持当前请求类型"),
    UNBINDING(407, "用户未授权绑定"),
    SYSTEM_TIMEOUT(408, "系统维护中，请稍后再试"),
    PARAM_EX(412, "参数类型解析异常"),
    TOO_MANY_REQUESTS(429, "请求超过次数限制"),
    INTERNAL_SERVER_ERROR(500, "内部服务错误"),
    BAD_GATEWAY(502, "网关错误"),
    SYSTEM_BUSY(503, "系统繁忙，请稍后再试"),
    GATEWAY_TIMEOUT(504, "网关超时");

    private final int code;
    private String msg;

    ExceptionCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public String getMsg() {
        return msg;
    }


    public ExceptionCode build(String msg, Object... param) {
        this.msg = String.format(msg, param);
        return this;
    }

    public ExceptionCode param(Object... param) {
        msg = String.format(msg, param);
        return this;
    }
}
