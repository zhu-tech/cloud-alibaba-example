package com.neyogoo.example.admin.sys.vo.request.token;

import com.neyogoo.example.common.core.util.ValidatorUtils;
import com.neyogoo.example.common.token.enumeration.UserTypeEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter
@Setter
@ToString
public abstract class LoginSmsCheckReqVO extends LoginReqVO {

    @ApiModelProperty("手机号")
    @NotBlank(message = "手机号不能为空")
    @Pattern(regexp = ValidatorUtils.REGEX_MOBILE, message = "手机号格式不正确")
    protected String userMobile;

    @ApiModelProperty("短信验证码")
    @NotBlank(message = "短信验证码不能为空")
    @Pattern(regexp = "^[0-9]{6}$", message = "短信验证码格式不正确")
    protected String smsCode;

    /**
     * 用户类型
     */
    public abstract UserTypeEnum userType();
}
