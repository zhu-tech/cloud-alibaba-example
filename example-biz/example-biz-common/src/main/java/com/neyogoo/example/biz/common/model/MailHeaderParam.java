package com.neyogoo.example.biz.common.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 站内信表头参数定义
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MailHeaderParam {

    @ApiModelProperty("属性字段名")
    private String key;

    @ApiModelProperty("属性名称")
    private String value;

}
