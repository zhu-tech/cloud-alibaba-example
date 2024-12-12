package com.neyogoo.example.admin.sys.vo.request.token;

import com.neyogoo.example.common.core.util.ValidatorUtils;
import com.neyogoo.example.common.token.enumeration.UserTypeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter
@Setter
@ToString
@ApiModel(description = "手机验证码发送请求入参")
public abstract class LoginSmsSendReqVO {

    @ApiModelProperty("手机号")
    @NotBlank(message = "手机号不能为空")
    @Pattern(regexp = ValidatorUtils.REGEX_MOBILE, message = "手机号格式不正确")
    private String userMobile;

    @ApiModelProperty("验证码信息")
    private String captchaVerification;

    /**
     * 用户类型
     */
    public abstract UserTypeEnum userType();
}
