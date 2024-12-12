package com.neyogoo.example.biz.common.enumeration.gateway;

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
@ApiModel("Http方法")
public enum HttpMethodEnum implements BaseEnum {

    ALL("ALL"),
    GET("GET"),
    POST("POST"),
    PUT("PUT"),
    DELETE("DELETE"),
    OPTIONS("OPTIONS");

    @ApiModelProperty("描述")
    private String desc;

    public static HttpMethodEnum match(String val, HttpMethodEnum def) {
        return Stream.of(values()).filter((item) -> item.getCode().equalsIgnoreCase(val)).findAny().orElse(def);
    }

    public static HttpMethodEnum get(String val) {
        return match(val, null);
    }

    public boolean eq(HttpMethodEnum val) {
        return val != null && eq(val.getCode());
    }

    @Override
    @ApiModelProperty(value = "编码", allowableValues = "ALL,GET,POST,PUT,DELETE,OPTIONS", example = "ALL")
    public String getCode() {
        return this.name();
    }
}
