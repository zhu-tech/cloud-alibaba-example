package com.neyogoo.example.common.core.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.neyogoo.example.common.core.exception.BizException;
import com.neyogoo.example.common.core.exception.code.BaseExceptionCode;
import com.neyogoo.example.common.core.exception.code.ExceptionCode;
import com.neyogoo.example.common.core.util.JsonUtils;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.HashMap;
import java.util.Map;


@Getter
@Setter
@Accessors(chain = true)
@SuppressWarnings("ALL")
public class R<T> {

    public static final String DEF_ERROR_MESSAGE = "系统繁忙，请稍候再试";
    public static final String HYSTRIX_ERROR_MESSAGE = "请求超时，请稍候再试";
    public static final int SUCCESS_CODE = ExceptionCode.SUCCESS.getCode();
    public static final int FAIL_CODE = ExceptionCode.INTERNAL_SERVER_ERROR.getCode();
    public static final int TIMEOUT_CODE = ExceptionCode.SYSTEM_TIMEOUT.getCode();
    /**
     * 统一参数验证异常
     */
    public static final int VALID_EX_CODE = ExceptionCode.PARAM_EX.getCode();
    public static final int OPERATION_EX_CODE = ExceptionCode.BAD_REQUEST.getCode();
    /**
     * 调用是否成功标识，0：成功，-1:系统繁忙，此时请开发者稍候再试 详情见[ExceptionCode]
     */
    @ApiModelProperty(value = "响应编码:0/200-请求处理成功")
    private int code;

    /**
     * 是否执行默认操作
     */
    @JsonIgnore
    private Boolean defExec = true;

    @ApiModelProperty("响应数据")
    private T data;

    @ApiModelProperty("提示消息")
    private String msg = "ok";

    @ApiModelProperty("请求路径")
    private String path;

    @ApiModelProperty("附加数据")
    private Map<Object, Object> extra;

    @ApiModelProperty("响应时间戳")
    private long timestamp = System.currentTimeMillis();

    private R() {
        this.defExec = false;
        this.timestamp = System.currentTimeMillis();
    }

    public R(int code, T data, String msg) {
        this.code = code;
        this.data = data;
        this.msg = msg;
        this.defExec = false;
        this.timestamp = System.currentTimeMillis();
    }

    public R(int code, T data, String msg, boolean defExec) {
        this(code, data, msg);
        this.defExec = defExec;
    }

    public static <E> R<E> result(int code, E data, String msg) {
        return new R<>(code, data, msg);
    }

    /**
     * 请求成功消息
     *
     * @param data 结果
     * @return RPC调用结果
     */
    public static <E> R<E> success(E data) {
        return new R<>(SUCCESS_CODE, data, "ok");
    }

    public static R<Boolean> success() {
        return new R<>(SUCCESS_CODE, true, "ok");
    }


    public static <E> R<E> successDef(E data) {
        return new R<>(SUCCESS_CODE, data, "ok", true);
    }

    public static <E> R<E> successDef() {
        return new R<>(SUCCESS_CODE, null, "ok", true);
    }

    public static <E> R<E> successDef(E data, String msg) {
        return new R<>(SUCCESS_CODE, data, msg, true);
    }

    /**
     * 请求成功方法 ，data返回值，msg提示信息
     *
     * @param data 结果
     * @param msg  消息
     * @return RPC调用结果
     */
    public static <E> R<E> success(E data, String msg) {
        return new R<>(SUCCESS_CODE, data, msg);
    }

    /**
     * 请求失败消息
     */
    public static <E> R<E> fail(int code, String msg) {
        return new R<>(code, null, (msg == null || msg.isEmpty()) ? DEF_ERROR_MESSAGE : msg);
    }

    public static <E> R<E> fail(String msg) {
        return fail(OPERATION_EX_CODE, msg);
    }

    public static <E> R<E> fail(String msg, Object... args) {
        String message = (msg == null || msg.isEmpty()) ? DEF_ERROR_MESSAGE : msg;
        return new R<>(OPERATION_EX_CODE, null, String.format(message, args));
    }

    public static <E> R<E> fail(BaseExceptionCode exceptionCode) {
        return validFail(exceptionCode);
    }

    public static <E> R<E> fail(BizException exception) {
        if (exception == null) {
            return fail(DEF_ERROR_MESSAGE);
        }
        return new R<>(exception.getCode(), null, exception.getMessage());
    }

    /**
     * 请求失败消息，根据异常类型，获取不同的提供消息
     *
     * @param throwable 异常
     * @return RPC 调用结果
     */
    public static <E> R<E> fail(Throwable throwable) {
        String msg = throwable != null ? throwable.getMessage() : DEF_ERROR_MESSAGE;
        return fail(FAIL_CODE, msg);
    }

    public static <E> R<E> validFail(String msg) {
        return new R<>(VALID_EX_CODE, null, (msg == null || msg.isEmpty()) ? DEF_ERROR_MESSAGE : msg);
    }

    public static <E> R<E> validFail(String msg, Object... args) {
        String message = (msg == null || msg.isEmpty()) ? DEF_ERROR_MESSAGE : msg;
        return new R<>(VALID_EX_CODE, null, String.format(message, args));
    }

    public static <E> R<E> validFail(BaseExceptionCode exceptionCode) {
        return new R<>(
                exceptionCode.getCode(), null,
                (exceptionCode.getMsg() == null || exceptionCode.getMsg().isEmpty())
                        ? DEF_ERROR_MESSAGE : exceptionCode.getMsg()
        );
    }

    public static <E> R<E> timeout() {
        return fail(TIMEOUT_CODE, HYSTRIX_ERROR_MESSAGE);
    }


    public R<T> put(String key, Object value) {
        if (this.extra == null) {
            this.extra = new HashMap<>(16);
        }
        this.extra.put(key, value);
        return this;
    }

    public R<T> putAll(Map<Object, Object> extra) {
        if (this.extra == null) {
            this.extra = new HashMap<>(16);
        }
        this.extra.putAll(extra);
        return this;
    }

    public Boolean getIsSuccess() {
        return this.code == SUCCESS_CODE;
    }

    @Override
    public String toString() {
        return JsonUtils.toJson(this);
    }
}
