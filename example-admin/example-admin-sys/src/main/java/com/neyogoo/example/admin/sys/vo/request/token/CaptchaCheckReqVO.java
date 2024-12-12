package com.neyogoo.example.admin.sys.vo.request.token;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@ApiModel(description = "验证核对验证码入参")
public class CaptchaCheckReqVO {

    @ApiModelProperty("验证码类型（滑动验证：blockPuzzle，文字点击：clickWord）")
    @NotBlank(message = "验证码类型不能为空")
    private String captchaType;

    @ApiModelProperty("aes加密坐标信息")
    @NotBlank(message = "坐标信息不能为空")
    private String pointJson;

    @ApiModelProperty("get请求返回的token")
    @NotBlank(message = "token不能为空")
    private String token;
}
