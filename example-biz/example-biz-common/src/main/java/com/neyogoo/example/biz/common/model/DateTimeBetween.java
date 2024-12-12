package com.neyogoo.example.biz.common.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DateTimeBetween implements Serializable {

    @ApiModelProperty("开始时间")
    @NotNull(message = "开始时间不能为空")
    protected LocalDateTime startTime;

    @ApiModelProperty("结束时间")
    @NotNull(message = "结束时间不能为空")
    protected LocalDateTime endTime;

    /**
     * 是否开始时间晚于结束时间
     */
    @JsonIgnore
    public boolean isInverted() {
        if (startTime == null) {
            return false;
        }
        if (endTime == null) {
            return false;
        }
        return startTime.isAfter(endTime);
    }
}
