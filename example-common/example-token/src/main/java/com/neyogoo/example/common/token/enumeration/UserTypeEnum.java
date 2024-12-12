package com.neyogoo.example.common.token.enumeration;

import com.neyogoo.example.common.core.enumeration.BaseEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.stream.Stream;

@Getter
@ApiModel("用户类型")
@AllArgsConstructor
@NoArgsConstructor
public enum UserTypeEnum implements BaseEnum {

    SysUser("管理用户"),
    CstUser("C端用户");

    @ApiModelProperty("描述")
    private String desc;

    public static UserTypeEnum match(String val, UserTypeEnum def) {
        return Stream.of(values()).filter((item) -> item.name().equalsIgnoreCase(val)).findAny().orElse(def);
    }

    public static UserTypeEnum get(String val) {
        return match(val, null);
    }

    public boolean eq(UserTypeEnum val) {
        return val != null && eq(val.name());
    }

    @Override
    @ApiModelProperty(value = "编码", allowableValues = "SysUser,CstUser", example = "SysUser")
    public String getCode() {
        return this.name();
    }
}
