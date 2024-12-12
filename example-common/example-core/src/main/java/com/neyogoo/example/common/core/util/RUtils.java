package com.neyogoo.example.common.core.util;

import com.neyogoo.example.common.core.exception.BizException;
import com.neyogoo.example.common.core.model.R;
import lombok.experimental.UtilityClass;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * 服务的返回值处理
 */
@UtilityClass
public class RUtils {

    /**
     * 响应是否正常
     *
     * @param r         响应的包装类
     * @param isThrowEx 不正常的时候 是否抛出{@link R#getMsg()}
     * @return true 成功 false 没有
     */
    public static boolean isSuccess(R<?> r, boolean isThrowEx) {
        if (Objects.nonNull(r) && r.getIsSuccess()) {
            return true;
        }

        if (isThrowEx) {
            throw BizException.wrap(r.getCode(), r.getMsg());
        }
        return false;
    }

    /**
     * 响应是否正常
     *
     * @param r       响应的包装类
     * @param msgFunc 获取错误信息的动作
     * @return true 成功 false 没有
     */
    public static boolean isSuccess(R<?> r, Function<R, String> msgFunc) {
        if (Objects.nonNull(r) && r.getIsSuccess()) {
            return true;
        }
        if (msgFunc != null) {
            throw BizException.wrap(r.getCode(), msgFunc.apply(r));
        }
        return false;
    }

    /**
     * 获取响应的实体
     *
     * @param r 响应的包装类
     * @return T 实体
     */
    public static <T> T getData(R<T> r) {
        if (isSuccess(r, false)) {
            return r.getData();
        }
        return null;
    }

    /**
     * 获取响应的实体
     *
     * @param r       响应的包装类
     * @param msgFunc 获取错误信息的动作
     * @return T 实体
     */
    public static <T> T getData(R<T> r, Function<R, String> msgFunc) {
        return isSuccess(r, msgFunc) ? r.getData() : null;
    }

    /**
     * 获取响应的实体(允许做自定义处理)
     *
     * @param r 响应的包装类
     * @return S 实体 没有成功的时候会返回异常
     */
    public static <T, S> S useData(R<T> r, Function<T, S> successFunc) {
        if (isSuccess(r, false)) {
            return successFunc.apply(r.getData());
        }
        return null;
    }

    /**
     * 获取响应的实体(允许做自定义处理)
     *
     * @param r       响应的包装类
     * @param msgFunc 获取错误信息的动作
     * @return S 实体 没有成功的时候会返回异常
     */
    public static <T, S> S useData(R<T> r, Function<T, S> successFunc, Function<R, String> msgFunc) {
        if (isSuccess(r, msgFunc)) {
            return successFunc.apply(r.getData());
        }
        return null;
    }

    /**
     * 获取响应的实体(允许做自定义处理)
     *
     * @param r 响应的包装类
     * @return S 实体 没有成功的时候会返回异常
     */
    public static <T> void useData(R<T> r, Consumer<T> successFunc) {
        if (isSuccess(r, false)) {
            successFunc.accept(r.getData());
        }
    }

    /**
     * 获取响应的实体(允许做自定义处理)
     *
     * @param r       响应的包装类
     * @param msgFunc 获取错误信息的动作
     * @return S 实体 没有成功的时候会返回异常
     */
    public static <T> void useData(R<T> r, Consumer<T> successFunc, Function<R, String> msgFunc) {
        if (isSuccess(r, msgFunc)) {
            successFunc.accept(r.getData());
        }
    }

    /**
     * 获取响应的实体
     *
     * @param r 响应的包装类
     * @return T 实体 没有成功的时候会返回异常
     */
    public static <T> T getDataThrowEx(R<T> r) {
        isSuccess(r, true);
        return r.getData();
    }
}
