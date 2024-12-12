package com.neyogoo.example.common.log.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface SysOptLog {

    /**
     * 是否记录方法的入参
     */
    boolean request() default true;

    /**
     * 是否记录返回值
     */
    boolean response() default true;
}
