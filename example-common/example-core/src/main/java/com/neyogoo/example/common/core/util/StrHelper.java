package com.neyogoo.example.common.core.util;

import cn.hutool.core.util.StrUtil;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

/**
 * 字符串帮助类
 */
@Slf4j
@UtilityClass
public final class StrHelper {

    /**
     * 普通的英文半角空格Unicode编码
     */
    private static final int SPACE_32 = 32;
    /**
     * 中文全角空格Unicode编码(一个中文宽度)
     */
    private static final int SPACE_12288 = 12288;
    /**
     * 普通的英文半角空格但不换行 Unicode 编码(== &nbsp; == &#xA0; == no-break space)
     */
    private static final int SPACE_160 = 160;
    /**
     * 半个中文宽度(== &ensp; == en空格)
     */
    private static final int SPACE_8194 = 8194;
    /**
     * 一个中文宽度(== &emsp; == em空格)
     */
    private static final int SPACE_8195 = 8195;
    /**
     * 四分之一中文宽度(四分之一em空格)
     */
    private static final int SPACE_8197 = 8197;
    /**
     * 窄空格
     */
    private static final int SPACE_8201 = 8201;

    /**
     * mybatis plus like查询转换
     */
    public static String keywordConvert(String value) {
        if (StrUtil.isBlank(value)) {
            return StrPool.EMPTY;
        }
        value = value.replaceAll(StrPool.PERCENT, "\\\\%");
        value = value.replaceAll(StrPool.UNDERSCORE, "\\\\_");
        return value;
    }

    public static Object keywordConvert(Object value) {
        if (value instanceof String) {
            return keywordConvert(String.valueOf(value));
        }
        return value;
    }

    public static String replaceSpace(String s) {
        return s.replace((char) SPACE_32, ' ')
                .replace((char) SPACE_12288, ' ')
                .replace((char) SPACE_160, ' ')
                .replace((char) SPACE_8194, ' ')
                .replace((char) SPACE_8195, ' ')
                .replace((char) SPACE_8197, ' ')
                .replace((char) SPACE_8201, ' ')
                .replace(" ", "");
    }

    public static String processSensitiveField(String input) {
        if (StringUtils.isBlank(input)) {
            return input;
        }

        input = StringUtils.trim(input);
        int strLen = input.length();

        if (strLen <= 1) {
            return "*";
        }
        if (strLen == 2) {
            return String.format("%s*", input.charAt(0));
        }
        if (strLen == 3) {
            return String.format("%s*%s", input.charAt(0), input.charAt(strLen - 1));
        }
        if (strLen == 11 && ValidatorUtils.isMobile(input)) {
            // 手机号
            return String.format("%s*****%s", StringUtils.left(input, 3), StringUtils.right(input, 3));
        }
        if (strLen == 18 && ValidatorUtils.isIdCard(input)) {
            // 18位身份证号
            return String.format("%s***************", StringUtils.left(input, 3));
        }
        if (strLen == 15 && ValidatorUtils.isIdCard(input)) {
            // 15位身份证号
            return String.format("%s************", StringUtils.left(input, 3));
        }
        int left = strLen / 4;
        int right = strLen / 3;
        return String.format("%s%s%s",
                StringUtils.left(input, left),
                StringUtils.repeat('*', (strLen - left - right)),
                StringUtils.right(input, right));
    }
}
