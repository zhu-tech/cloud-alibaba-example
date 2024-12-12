package com.neyogoo.example.common.token.enumeration;

import com.neyogoo.example.common.core.enumeration.BaseEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.stream.Stream;

@Getter
@ApiModel("Token类型")
@AllArgsConstructor
@NoArgsConstructor
public enum TokenTypeEnum implements BaseEnum {

    Login("登录Token"),
    Refresh("刷新Token");

    @ApiModelProperty("描述")
    private String desc;

    public static TokenTypeEnum match(String val, TokenTypeEnum def) {
        return Stream.of(values()).filter((item) -> item.name().equalsIgnoreCase(val)).findAny().orElse(def);
    }

    public static TokenTypeEnum get(String val) {
        return match(val, null);
    }

    public boolean eq(TokenTypeEnum val) {
        return val != null && eq(val.name());
    }

    @Override
    @ApiModelProperty(value = "编码", allowableValues = "Login,Refresh", example = "Login")
    public String getCode() {
        return this.name();
    }
}
