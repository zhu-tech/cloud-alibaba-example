package com.neyogoo.example.biz.toolbox.vo.response.logging;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SysOptLogRespVO {

    @ApiModelProperty("日志id")
    private String id;

    @ApiModelProperty("操作类型")
    private String operateType;

    @ApiModelProperty("用户ID")
    private Long userId;

    @ApiModelProperty("用户名称")
    private String userName;

    @ApiModelProperty("浏览器")
    private String ua;

    @ApiModelProperty("请求路径")
    private String requestUrl;

    @ApiModelProperty("请求类型")
    private String httpMethod;

    @ApiModelProperty("请求参数")
    private String requestParams;

    @ApiModelProperty("操作时间")
    private LocalDateTime requestTime;

    @ApiModelProperty("响应时间")
    private LocalDateTime responseTime;

    @ApiModelProperty("类对象名称")
    private String className;

    @ApiModelProperty("方法名称")
    private String methodName;

    @ApiModelProperty("操作说明")
    private String operateExplain;

    @ApiModelProperty("结果类型")
    private String resultType;

    @ApiModelProperty("结果内容")
    private String resultContent;

}
