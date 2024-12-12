package com.neyogoo.example.common.boot.annotation;

import com.neyogoo.example.common.core.model.PageResp;
import com.neyogoo.example.common.token.enumeration.UserTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.Supplier;

/**
 * 基于 Token 检查部分信息
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface CheckLoginToken {

    /**
     * 是否验证
     */
    boolean check() default true;

    /**
     * 要求的用户类型
     */
    UserTypeEnum[] requireUserType();

    /**
     * 是否静默模式
     * 静默模式时，状态码仍然为 200，前端不会弹出异常提示，但是结果为不走后端查询的默认空结果
     */
    boolean silenceMode() default false;

    /**
     * 静默模式结果
     * 检查不通过时返回的默认空结果
     */
    SilenceResult silenceResult() default SilenceResult.NullValue;


    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    enum SilenceResult {

        EmptyList(() -> new ArrayList()),
        EmptyMap(() -> new HashMap()),
        EmptyPage(() -> new PageResp<>()),
        ZeroInt(() -> Integer.valueOf(0)),
        ZeroLong(() -> Long.valueOf(0)),
        ZeroDouble(() -> Double.valueOf(0.0)),
        TrueBool(() -> Boolean.TRUE),
        FalseBool(() -> Boolean.FALSE),
        NullValue(() -> null);

        private Supplier result;
    }
}
