package com.neyogoo.example.biz.common.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DateBetween implements Serializable {

    @ApiModelProperty("开始日期")
    @NotNull(message = "开始日期不能为空")
    protected LocalDate startDate;

    @ApiModelProperty("结束日期")
    @NotNull(message = "结束日期不能为空")
    protected LocalDate endDate;

    /**
     * 是否开始日期晚于结束日期
     */
    @JsonIgnore
    public boolean isInverted() {
        if (startDate == null) {
            return false;
        }
        if (endDate == null) {
            return false;
        }
        return startDate.isAfter(endDate);
    }
}
