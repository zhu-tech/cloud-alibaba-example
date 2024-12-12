package com.neyogoo.example.admin.sys.vo.request.token;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@ApiModel(description = "验证获取验证码入参")
public class CaptchaGetReqVO {

    @ApiModelProperty("验证码类型（滑动验证：blockPuzzle，文字点击：clickWord）")
    @NotBlank(message = "验证码类型不能为空")
    private String captchaType;

    @ApiModelProperty("客户端UI组件id")
    @NotBlank(message = "客户端UI组件id不能为空")
    private String clientUid;
}
