package com.neyogoo.example.common.core.enumeration;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.stream.Stream;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("数据权限类型")
public enum DataScopeTypeEnum implements BaseEnum {

    All((byte) 9, "全部"),
    Orgs((byte) 7, "当前机构及子机构"),
    Org((byte) 6, "当前机构"),
    Depts((byte) 4, "当前部门及其子部门"),
    Dept((byte) 3, "当前部门"),
    Self((byte) 1, "个人");


    @ApiModelProperty("数值")
    private byte val;

    @ApiModelProperty("描述")
    private String desc;


    public static DataScopeTypeEnum match(String val, DataScopeTypeEnum def) {
        return Stream.of(values()).filter((item) -> item.name().equalsIgnoreCase(val)).findAny()
                .orElse(def);
    }

    public static DataScopeTypeEnum match(Integer val, DataScopeTypeEnum def) {
        return Stream.of(values()).filter((item) -> val != null && item.getVal() == val).findAny()
                .orElse(def);
    }

    public static DataScopeTypeEnum get(String val) {
        return match(val, null);
    }

    public static DataScopeTypeEnum get(Integer val) {
        return match(val, null);
    }

    public boolean eq(final DataScopeTypeEnum val) {
        return val != null && eq(val.name());
    }

    @Override
    @ApiModelProperty("编码")
    public String getCode() {
        return this.name();
    }

}
