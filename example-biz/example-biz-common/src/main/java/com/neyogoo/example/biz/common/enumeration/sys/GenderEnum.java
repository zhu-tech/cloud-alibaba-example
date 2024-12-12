package com.neyogoo.example.biz.common.enumeration.sys;

import com.neyogoo.example.common.core.enumeration.BaseEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.stream.Stream;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("性别")
public enum GenderEnum implements BaseEnum {

    M("男"),
    W("女");

    @ApiModelProperty("描述")
    private String desc;

    public static GenderEnum match(String val, GenderEnum def) {
        return Stream.of(values()).filter((item) -> item.getCode().equalsIgnoreCase(val)).findAny().orElse(def);
    }

    public static GenderEnum descOf(String desc) {
        return Stream.of(values()).filter((item) -> item.desc.equalsIgnoreCase(desc)).findAny().orElse(null);
    }

    public static GenderEnum get(String val) {
        return match(val, null);
    }

    public boolean eq(GenderEnum val) {
        return val != null && eq(val.getCode());
    }

    @Override
    @ApiModelProperty(value = "编码", allowableValues = "M,W", example = "M")
    public String getCode() {
        return this.name();
    }
}
