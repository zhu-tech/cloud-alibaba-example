package com.neyogoo.example.common.core.util;

import lombok.experimental.UtilityClass;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@UtilityClass
public class ReflectionUtils {

    /**
     * 获取类所有属性，含父类属性
     */
    public static <T> List<Field> getClassFieldsIncludeSuper(Class<T> clazz) {
        List<Field> fields = new ArrayList<>();
        fields.addAll(Arrays.asList(clazz.getDeclaredFields()));
        Class<? super T> superclass = clazz.getSuperclass();
        if (!superclass.equals(Object.class)) {
            fields.addAll(getClassFieldsIncludeSuper(superclass));
        }
        return fields;
    }
}
