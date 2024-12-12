package com.neyogoo.example.biz.toolbox.vo.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

/**
 * 发送短信通知-入参
 */
@Data
@Accessors(chain = true)
@ApiModel(description = "发送短信通知入参")
public class SmsSendReqVO {


    @ApiModelProperty("短信模板编码")
    @NotNull(message = "短信模板编码不能为空")
    private String templateCode;

    @ApiModelProperty("手机号")
    @NotEmpty(message = "手机号码不能为空")
    private List<String> phoneNumbers;

    @ApiModelProperty("模板所需参数")
    private Map<String, Object> params;

}
