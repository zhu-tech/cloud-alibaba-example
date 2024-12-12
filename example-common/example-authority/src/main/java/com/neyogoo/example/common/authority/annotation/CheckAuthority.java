package com.neyogoo.example.common.authority.annotation;

import com.neyogoo.example.common.authority.permission.AuthorityFunction;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 检查权限
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface CheckAuthority {

    /**
     * Spring el
     *
     * @see AuthorityFunction
     */
    String value() default "permit()";
}
