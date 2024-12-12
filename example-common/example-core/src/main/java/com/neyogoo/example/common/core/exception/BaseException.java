package com.neyogoo.example.common.core.exception;

/**
 * 异常接口类
 */
public interface BaseException {

    /**
     * 统一参数验证异常码
     */
    int BASE_VALID_PARAM = -9;

    /**
     * 返回异常信息
     */
    String getMessage();

    /**
     * 返回异常编码
     */
    int getCode();

}
