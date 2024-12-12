package com.neyogoo.example.common.core.util;

import com.neyogoo.example.common.core.enumeration.BaseEnum;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;

import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@UtilityClass
public class EnumUtils {

    public static <T extends Enum<T> & BaseEnum> T descOf(
            Class<T> enumClass, String desc) {
        if (enumClass == null || StringUtils.isBlank(desc)) {
            return null;
        }
        T[] enumConstants = enumClass.getEnumConstants();
        return Stream.of(enumConstants)
                .filter(e -> e.getDesc().equalsIgnoreCase(desc))
                .findFirst()
                .orElse(null);
    }

    public static <T extends Enum<T> & BaseEnum> T descOf(
            Class<T> enumClass, String desc, T defaultValue) {
        T target = descOf(enumClass, desc);
        return target == null ? defaultValue : target;
    }

    public static <T extends Enum<T> & BaseEnum> Map<String, String> codeDescMap(
            Class<T> enumClass) {
        if (enumClass == null) {
            return Collections.emptyMap();
        }
        T[] enumConstants = enumClass.getEnumConstants();
        return Stream.of(enumConstants)
                .collect(Collectors.toMap(T::getCode, T::getDesc));
    }

    public static <T extends Enum<T> & BaseEnum> Map<String, String> descCodeMap(
            Class<T> enumClass) {
        if (enumClass == null) {
            return Collections.emptyMap();
        }
        T[] enumConstants = enumClass.getEnumConstants();
        return Stream.of(enumConstants)
                .collect(Collectors.toMap(T::getDesc, T::getCode));
    }
}
