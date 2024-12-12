package com.neyogoo.example.common.core.util;

import lombok.experimental.UtilityClass;
import org.springframework.util.CollectionUtils;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.groups.Default;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * 校验器：利用正则表达式校验邮箱、手机号等
 */
@UtilityClass
public final class ValidatorUtils {

    private static Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    /**
     * 正则表达式：验证密码（MD5加密后）
     */
    public static final String REGEX_PASSWORD = "^[0-9a-z]{32}$";
    /**
     * 正则表达式：验证邮政编码
     */
    public static final String REGEX_POST_CODE = "^[0-9]{6}$";
    /**
     * 正则表达式：验证邮箱
     */
    public static final String REGEX_EMAIL =
            "^([a-z0-9A-Z]+[-_|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
    /**
     * 正则表达式：验证18位身份证
     */
    public static final String REGEX_ID_CARD_18 =
            "^[1-9]\\d{5}(18|19|([23]\\d))\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]$";
    /**
     * 正则表达式：验证15位身份证
     */
    public static final String REGEX_ID_CARD_15 =
            "^[1-9]\\d{5}\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{2}$";
    /**
     * 正则表达式：验证身份证
     */
    public static final String REGEX_ID_CARD = "(" + REGEX_ID_CARD_18 + ")|(" + REGEX_ID_CARD_15 + ")";
    /**
     * 正则表达式：验证身份证
     */
    public static final Pattern PATTERN_ID_CARD = Pattern.compile(REGEX_ID_CARD);
    /**
     * 正则表达式：验证社会统一信用代码
     */
    public static final String REGEX_UNIFORM_SOCIAL_CREDIT_CODE =
            "^([0-9A-Z]{15})|([0-9A-Z]{17})|([0-9A-Z]{18})|([0-9A-Z]{20})$";
    /**
     * 正则表达式：验证社会统一信用代码
     * 统一社会信用代码由18位数字或者大写字母组成，但是字母不包括 I、O、Z、S、V
     * 一共由五部分组成
     * 第一部分：登记管理部门代码1位 (数字或大写英文字母)
     * 第二部分：机构类别代码1位 (数字或大写英文字母)
     * 第三部分：登记管理机关行政区划码6位 (数字)
     * 第四部分：主体标识码（组织机构代码）9位 (数字或大写英文字母)
     * 第五部分：校验码1位 (数字或大写英文字母)
     * 同时支持18位和15位社会信用代码
     */
    public static final String REGEX_CREDIT_CODE =
            "^([0-9A-HJ-NPQRTUWXY]{2}\\d{6}[0-9A-HJ-NPQRTUWXY]{10}|[1-9]\\d{14})$";
    /**
     * 正则表达式：验证手机号
     */
    public static final String REGEX_MOBILE = "^1[3-9]\\d{9}$";
    /**
     * 正则表达式：验证手机号
     */
    public static final Pattern PATTERN_MOBILE = Pattern.compile(REGEX_MOBILE);

    /**
     * 是否是手机号
     */
    public static boolean isMobile(CharSequence input) {
        return PATTERN_MOBILE.matcher(input).matches();
    }

    /**
     * 是否是手机号
     */
    public static boolean isIdCard(CharSequence input) {
        return PATTERN_ID_CARD.matcher(input).matches();
    }

    /**
     * 验证对象全部属性
     */
    public static <T> List<String> validateEntity(T obj) {
        if (obj == null) {
            return new ArrayList<>();
        }
        Set<ConstraintViolation<T>> set = validator.validate(obj, Default.class);
        if (set == null || set.size() == 0) {
            return new ArrayList<>();
        }
        return set.stream().map(ConstraintViolation::getMessage).sorted().collect(Collectors.toList());
    }

    /**
     * 验证对象全部属性
     */
    public static <T> List<String> validateList(List<T> list) {
        if (CollectionUtils.isEmpty(list)) {
            return new ArrayList<>();
        }
        return list.stream().flatMap(o -> validateEntity(o).stream()).distinct().sorted().collect(Collectors.toList());
    }

    /**
     * 验证对象部分属性
     */
    public static <T> List<String> validateProperty(T obj, String... propertyNames) {
        if (obj == null) {
            return new ArrayList<>();
        }
        return Arrays.stream(propertyNames)
                .map(o -> {
                    Set<ConstraintViolation<T>> set = validator.validateProperty(
                            obj, o, Default.class
                    );
                    List<String> list;
                    if (set == null || set.size() == 0) {
                        list = Collections.EMPTY_LIST;
                    } else {
                        list = set.stream().map(ConstraintViolation::getMessage).sorted()
                                .collect(Collectors.toList());
                    }
                    return list;
                })
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }
}
