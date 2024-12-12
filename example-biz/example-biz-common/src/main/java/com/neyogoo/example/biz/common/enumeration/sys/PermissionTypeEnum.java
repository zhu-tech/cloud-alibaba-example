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
@ApiModel("权限类型")
public enum PermissionTypeEnum implements BaseEnum {

    M("菜单"),
    R("资源");

    @ApiModelProperty("描述")
    private String desc;

    public static PermissionTypeEnum match(String val, PermissionTypeEnum def) {
        return Stream.of(values()).filter((item) -> item.getCode().equalsIgnoreCase(val)).findAny().orElse(def);
    }

    public static PermissionTypeEnum get(String val) {
        return match(val, null);
    }

    public boolean eq(PermissionTypeEnum val) {
        return val != null && eq(val.getCode());
    }

    @Override
    @ApiModelProperty(value = "编码", allowableValues = "M,R", example = "M")
    public String getCode() {
        return this.name();
    }
}
