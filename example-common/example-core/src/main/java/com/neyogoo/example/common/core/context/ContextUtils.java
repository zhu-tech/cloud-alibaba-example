package com.neyogoo.example.common.core.context;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.ttl.TransmittableThreadLocal;
import com.neyogoo.example.common.core.constant.BasicConstant;
import com.neyogoo.example.common.core.constant.ContextConstant;
import com.neyogoo.example.common.core.util.StrPool;
import lombok.experimental.UtilityClass;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 获取当前线程变量中的 用户id、用户昵称、账号等信息
 */
@UtilityClass
public class ContextUtils {


    /**
     * 支持多线程传递参数
     */
    private static final ThreadLocal<Map<String, String>> THREAD_LOCAL = new TransmittableThreadLocal<>();

    public static void putAll(Map<String, String> map) {
        map.forEach(ContextUtils::set);
    }

    public static void set(String key, Object value) {
        Map<String, String> map = getLocalMap();
        map.put(key, value == null ? StrPool.EMPTY : value.toString());
    }

    public static <T> T get(String key, Class<T> type) {
        Map<String, String> map = getLocalMap();
        return Convert.convert(type, map.get(key));
    }

    public static <T> T get(String key, Class<T> type, Object def) {
        Map<String, String> map = getLocalMap();
        String value;
        if (def == null) {
            value = map.get(key);
        } else {
            value = map.getOrDefault(key, String.valueOf(def));
        }
        return Convert.convert(type, CharSequenceUtil.isEmpty(value) ? def : value);
    }

    public static String get(String key) {
        Map<String, String> map = getLocalMap();
        return map.getOrDefault(key, StrPool.EMPTY);
    }

    public static Map<String, String> getLocalMap() {
        Map<String, String> map = THREAD_LOCAL.get();
        if (map == null) {
            map = new ConcurrentHashMap<>(10);
            THREAD_LOCAL.set(map);
        }
        return map;
    }

    public static void setLocalMap(Map<String, String> localMap) {
        THREAD_LOCAL.set(localMap);
    }

    public static Long getUserId() {
        return get(ContextConstant.HEADER_USER_ID, Long.class, null);
    }

    public static void setUserId(Object userId) {
        set(ContextConstant.HEADER_USER_ID, userId == null ? BasicConstant.DEFAULT_ID : userId);
    }

    public static String getUserName() {
        return get(ContextConstant.HEADER_USER_NAME, String.class, null);
    }

    public static void setUserName(Object userName) {
        set(ContextConstant.HEADER_USER_NAME, userName == null ? "" : userName);
    }

    public static String getUserAccount() {
        return get(ContextConstant.HEADER_USER_ACCOUNT, String.class, null);
    }

    public static void setUserAccount(Object userAccount) {
        set(ContextConstant.HEADER_USER_ACCOUNT, userAccount == null ? "" : userAccount);
    }

    public static String getUserType() {
        return get(ContextConstant.HEADER_USER_TYPE, String.class, null);
    }

    public static void setUserType(Object userType) {
        set(ContextConstant.HEADER_USER_TYPE, userType == null ? "" : userType);
    }

    public static Long getOrgId() {
        return get(ContextConstant.HEADER_ORG_ID, Long.class);
    }

    public static void setOrgId(Object orgId) {
        set(ContextConstant.HEADER_ORG_ID, orgId);
    }

    public static String getToken() {
        return get(ContextConstant.HEADER_TOKEN, String.class);
    }

    public static void setToken(String token) {
        set(ContextConstant.HEADER_TOKEN, token == null ? StrPool.EMPTY : token);
    }

    public static String getLoginPoint() {
        return get(ContextConstant.HEADER_LOGIN_POINT, String.class);
    }

    public static void setLoginPoint(Object obj) {
        set(ContextConstant.HEADER_LOGIN_POINT, obj);
    }

    public static String getXFeign() {
        return get(ContextConstant.HEADER_FEIGN, String.class, StrPool.FALSE);
    }

    public static void setXFeign(String val) {
        set(ContextConstant.HEADER_FEIGN, val);
    }

    public static String getXTraceId() {
        return get(ContextConstant.HEADER_TRACE_ID, String.class);
    }

    public static void setXTraceId(String val) {
        set(ContextConstant.HEADER_TRACE_ID, val);
    }

    public static String getXTransactionId() {
        return get(ContextConstant.HEADER_TRANSACTION_ID, String.class);
    }

    public static void setXTransactionId(String val) {
        set(ContextConstant.HEADER_TRANSACTION_ID, val);
    }

    private static boolean isEmptyLong(String key) {
        String val = getLocalMap().get(key);
        return StrUtil.isEmpty(val) || StrPool.NULL.equals(val) || StrPool.ZERO.equals(val);
    }

    private static boolean isEmptyStr(String key) {
        String val = getLocalMap().get(key);
        return val == null || StrPool.NULL.equals(val);
    }

    public static void remove() {
        THREAD_LOCAL.remove();
    }
}
