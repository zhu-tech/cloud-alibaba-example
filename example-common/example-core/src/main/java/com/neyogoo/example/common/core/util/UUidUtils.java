package com.neyogoo.example.common.core.util;

import java.util.UUID;

/**
 * 唯一性ID工具类
 */
public class UUidUtils {

    /**
     * 封装JDK自带的UUID, 通过Random数字生成, 中间无-分割.
     */
    public static String uuid() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }
}
