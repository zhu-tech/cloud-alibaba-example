package com.neyogoo.example.biz.common.enumeration.sys;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonCreator;
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
@ApiModel("机构等级")
public enum OrgLevelEnum implements BaseEnum {

    L1((byte) 1, "一级单位"),
    L2((byte) 2, "二级单位"),
    L3((byte) 3, "三级单位");

    @EnumValue
    @ApiModelProperty("值")
    private byte val;

    @ApiModelProperty("描述")
    private String desc;

    public static OrgLevelEnum match(String val, OrgLevelEnum def) {
        return Stream.of(values()).filter((item) -> item.getCode().equalsIgnoreCase(val)).findAny().orElse(def);
    }

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static OrgLevelEnum get(String val) {
        return match(val, null);
    }

    public boolean eq(OrgLevelEnum val) {
        return val != null && eq(val.getCode());
    }

    @Override
    @ApiModelProperty(value = "编码", allowableValues = "1,2,3", example = "1")
    public String getCode() {
        return String.valueOf(this.val);
    }
}
