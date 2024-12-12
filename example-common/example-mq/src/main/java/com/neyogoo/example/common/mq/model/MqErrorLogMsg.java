package com.neyogoo.example.common.mq.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 队列消费错误记录
 */
@Getter
@Setter
@Accessors(chain = true)
@ToString(callSuper = true)
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class MqErrorLogMsg implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("队列名称")
    private String queueName;

    @ApiModelProperty("参数信息")
    private Object paramObj;

    @ApiModelProperty("线程信息")
    private Map<String, String> localMap;

    @ApiModelProperty("错误信息")
    private List<String> errorMessage;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;
}
