package com.neyogoo.example.common.core.util;

import com.google.common.base.CaseFormat;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;

@UtilityClass
public class StringFormatUtils {

    /**
     * 大写驼峰 转 小写下划线
     */
    public static String upperCamel2LowerUnderscore(String s) {
        return CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, s);
    }

    /**
     * 小写驼峰 转 小写下划线
     */
    public static String lowerCamel2LowerUnderscore(String s) {
        return CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, s);
    }

    /**
     * unicode 转 中文
     */
    public static String unicodeToCn(String unicode) {
        if (StringUtils.isBlank(unicode)) {
            return unicode;
        }
        String[] strs = unicode.split("\\\\u");
        StringBuilder returnStr = new StringBuilder();
        for (int i = 1; i < strs.length; i++) {
            returnStr.append((char) Integer.valueOf(strs[i], 16).intValue());
        }
        return returnStr.toString();
    }

    /**
     * 中文转 unicode
     */
    public static String cnToUnicode(String cn) {
        if (StringUtils.isBlank(cn)) {
            return cn;
        }
        char[] chars = cn.toCharArray();
        StringBuilder returnStr = new StringBuilder();
        for (int i = 0; i < chars.length; i++) {
            returnStr.append("\\u").append(Integer.toString(chars[i], 16));
        }
        return returnStr.toString();
    }
}
