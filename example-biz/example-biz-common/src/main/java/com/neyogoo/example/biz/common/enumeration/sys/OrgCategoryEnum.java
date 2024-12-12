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
@ApiModel("机构类别")
public enum OrgCategoryEnum implements BaseEnum {

    PS("初筛机构"),
    SS("复筛机构"),
    DG("终筛机构");

    @ApiModelProperty("描述")
    private String desc;

    public static OrgCategoryEnum match(String val, OrgCategoryEnum def) {
        return Stream.of(values()).filter((item) -> item.getCode().equalsIgnoreCase(val)).findAny().orElse(def);
    }

    public static OrgCategoryEnum get(String val) {
        return match(val, null);
    }

    public boolean eq(OrgCategoryEnum val) {
        return val != null && eq(val.getCode());
    }

    @Override
    @ApiModelProperty(value = "编码", allowableValues = "PS,SS,DG", example = "PS")
    public String getCode() {
        return this.name();
    }
}
