package com.neyogoo.example.biz.common.enumeration.toolbox;

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
@ApiModel("字典类型")
public enum DictTypeEnum implements BaseEnum {

    Example("示例");

    @ApiModelProperty("描述")
    private String desc;

    public static DictTypeEnum match(String val, DictTypeEnum def) {
        return Stream.of(values()).filter((item) -> item.getCode().equalsIgnoreCase(val)).findAny().orElse(def);
    }

    public static DictTypeEnum get(String val) {
        return match(val, null);
    }

    public boolean eq(DictTypeEnum val) {
        return val != null && eq(val.getCode());
    }

    @Override
    @ApiModelProperty(value = "编码")
    public String getCode() {
        return this.name();
    }
}
