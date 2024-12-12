package com.neyogoo.example.common.core.enumeration;

import com.fasterxml.jackson.annotation.JsonValue;
import com.neyogoo.example.common.core.util.MapHelper;
import com.neyogoo.example.common.core.util.StringFormatUtils;

import java.util.Arrays;
import java.util.Map;

/**
 * 枚举类型基类
 */
public interface BaseEnum {

    /**
     * 将制定的枚举集合转成 map
     * key -> code
     * value -> desc
     */
    static Map<String, String> getMap(BaseEnum[] list) {
        return MapHelper.uniqueIndex(Arrays.asList(list), BaseEnum::getCode, BaseEnum::getDesc);
    }

    /**
     * 将制定的枚举集合转成 map
     * key -> desc
     * value -> code
     */
    static Map<String, String> descMap(BaseEnum[] list) {
        return MapHelper.uniqueIndex(Arrays.asList(list), BaseEnum::getDesc, BaseEnum::getCode);
    }

    /**
     * 将关联枚举集合转成 map
     * key -> code
     * value -> desc
     */
    static Map<String, BaseEnum[]> getRelationMap(BaseEnum[] list) {
        return MapHelper.uniqueIndex(Arrays.asList(list), BaseEnum::getCode, BaseEnum::relationEnum);
    }

    /**
     * 编码
     */
    @JsonValue
    default String getCode() {
        return toString();
    }

    /**
     * 描述
     */
    String getDesc();

    /**
     * 关联枚举
     */
    default BaseEnum[] relationEnum() {
        return new BaseEnum[]{};
    }

    /**
     * 扩展参数
     */
    default String fetchExtra() {
        return "";
    }

    /**
     * 判断 val 是否跟当前枚举相等
     */
    default boolean eq(String val) {
        return this.getCode().equalsIgnoreCase(val);
    }

    /**
     * code 转 小写下划线分隔模式
     */
    default String lowerUnderscoreCode() {
        return StringFormatUtils.upperCamel2LowerUnderscore(getCode());
    }
}
