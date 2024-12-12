package com.neyogoo.example.admin.sys.vo.request.user;

import com.neyogoo.example.admin.sys.config.properties.LoginProperties;
import com.neyogoo.example.admin.sys.model.User;
import com.neyogoo.example.common.core.util.ArgumentAssert;
import com.neyogoo.example.common.core.util.ValidatorUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
@Accessors(chain = true)
@NoArgsConstructor
@ApiModel(description = "修改密码入参")
public class UserUpdatePwdReqVO {

    @ApiModelProperty("原密码（md5加密后）")
    @NotBlank(message = "原密码不能为空")
    @Pattern(regexp = ValidatorUtils.REGEX_PASSWORD, message = "密码错误")
    private String oldPwd;

    @ApiModelProperty("新密码（md5加密后）")
    @NotBlank(message = "新密码不能为空")
    @Pattern(regexp = ValidatorUtils.REGEX_PASSWORD, message = "密码错误")
    private String newPwd;

    @ApiModelProperty("验证码信息")
    @NotBlank(message = "验证码信息不能为空")
    private String captchaVerification;

    public void checkBeforeChangePwd(User sysUser) {
        User.checkBeforeChangePwd(sysUser);
        ArgumentAssert.equals(
                LoginProperties.encodePwd(this.oldPwd, sysUser.getLoginSalt()),
                sysUser.getLoginPwd(), "原密码不正确，请重新输入"
        );
        ArgumentAssert.isFalse(this.oldPwd.equals(this.newPwd), "新旧密码不能一致，请重新输入");
    }
}
