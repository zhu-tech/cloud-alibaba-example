package com.neyogoo.example.common.core.exception.code;

/**
 * 异常编码
 */
public interface BaseExceptionCode {

    /**
     * 异常编码
     */
    int getCode();

    /**
     * 异常消息
     *
     * @return 异常消息
     */
    String getMsg();
}
