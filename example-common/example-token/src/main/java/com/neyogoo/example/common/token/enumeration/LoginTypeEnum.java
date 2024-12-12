package com.neyogoo.example.common.token.enumeration;

import com.neyogoo.example.common.core.enumeration.BaseEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.stream.Stream;

@Getter
@ApiModel("登录方式")
@AllArgsConstructor
@NoArgsConstructor
public enum LoginTypeEnum implements BaseEnum {

    Pwd("用户名密码登录"),
    Sms("短信验证码登录"),
    WxGrant("微信授权登录");

    @ApiModelProperty("描述")
    private String desc;

    public static LoginTypeEnum match(String val, LoginTypeEnum def) {
        return Stream.of(values()).filter((item) -> item.name().equalsIgnoreCase(val)).findAny().orElse(def);
    }

    public static LoginTypeEnum get(String val) {
        return match(val, null);
    }

    public boolean eq(LoginTypeEnum val) {
        return val != null && eq(val.name());
    }

    @Override
    @ApiModelProperty(value = "编码", allowableValues = "Pwd,Sms,WxGrant", example = "Pwd")
    public String getCode() {
        return this.name();
    }
}
