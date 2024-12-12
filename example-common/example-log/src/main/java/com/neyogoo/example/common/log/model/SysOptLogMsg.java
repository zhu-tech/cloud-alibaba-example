package com.neyogoo.example.common.log.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldNameConstants;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Data
@FieldNameConstants
@NoArgsConstructor
@AllArgsConstructor
public class SysOptLogMsg implements Serializable {

    /**
     * 用户类型
     *
     * @see com.neyogoo.example.common.token.enumeration.UserTypeEnum
     */
    protected String userType;

    /**
     * 用户id
     */
    protected Long userId;

    /**
     * 用户名称
     */
    protected String userName;

    /**
     * 浏览器
     */
    protected String ua;

    /**
     * 日志链路追踪id日志标志
     */
    protected String traceId;

    /**
     * 请求路径
     */
    protected String requestUrl;

    /**
     * 请求类型（GET，POST...）
     */
    protected String httpMethod;

    /**
     * 请求参数
     */
    protected String requestParams;

    /**
     * IP
     */
    protected String requestIp;

    /**
     * 操作时间
     */
    protected LocalDateTime requestTime;

    /**
     * 响应时间
     */
    protected LocalDateTime responseTime;

    /**
     * 消耗时间
     */
    protected Long consumingTime;

    /**
     * 类对象名称
     */
    protected String className;

    /**
     * 方法名称
     */
    protected String methodName;

    /**
     * 操作说明
     */
    protected String operateExplain;

    /**
     * 结果类型
     */
    protected ResultType resultType;

    /**
     * 结果内容
     */
    protected String resultContent;


    public void updateConsumingTime() {
        this.consumingTime = this.requestTime.until(this.responseTime, ChronoUnit.MILLIS);
    }

    public enum ResultType {
        Success, Fail, Exception
    }
}
