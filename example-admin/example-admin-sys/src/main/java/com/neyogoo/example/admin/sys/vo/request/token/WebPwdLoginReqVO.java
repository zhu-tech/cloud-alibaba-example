package com.neyogoo.example.admin.sys.vo.request.token;

import com.neyogoo.example.common.core.util.ValidatorUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
@ApiModel(description = "PC端（政府）密码登录请求入参")
public class WebPwdLoginReqVO extends LoginReqVO {

    @ApiModelProperty("登录名")
    @NotBlank(message = "登录名不能为空")
    @Pattern(regexp = ValidatorUtils.REGEX_MOBILE, message = "用户名或密码错误")
    private String userAccount;

    @ApiModelProperty("登录密码（md5加密后）")
    @NotBlank(message = "登录密码不能为空")
    @Pattern(regexp = ValidatorUtils.REGEX_PASSWORD, message = "用户名或密码错误")
    private String userPwd;

    @ApiModelProperty("验证码信息")
    private String captchaVerification;
}
