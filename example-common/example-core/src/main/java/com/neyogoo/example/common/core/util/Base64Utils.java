package com.neyogoo.example.common.core.util;

import lombok.experimental.UtilityClass;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * BASE64编码与解码
 */
@UtilityClass
public class Base64Utils {

    public static String encode(byte[] bstr) {
        return new String(Base64.getEncoder().encode(bstr), StandardCharsets.UTF_8);
    }

    public static String encode(String str) {
        return new String(Base64.getEncoder().encode(str.getBytes(StandardCharsets.UTF_8)), StandardCharsets.UTF_8);
    }

    public static byte[] decode(String str) {
        return Base64.getDecoder().decode(str.getBytes(StandardCharsets.UTF_8));
    }

    public static String decode2Str(String str) {
        return new String(decode(str), StandardCharsets.UTF_8);
    }
}
