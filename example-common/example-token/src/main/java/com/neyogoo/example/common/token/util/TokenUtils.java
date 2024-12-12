package com.neyogoo.example.common.token.util;

import cn.hutool.core.util.StrUtil;
import com.neyogoo.example.common.core.exception.BizException;
import com.neyogoo.example.common.core.util.Base64Utils;
import com.neyogoo.example.common.core.util.StrPool;
import com.neyogoo.example.common.token.constant.TokenConstant;
import com.neyogoo.example.common.token.enumeration.LoginPointEnum;
import com.neyogoo.example.common.token.enumeration.UserTypeEnum;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import static com.neyogoo.example.common.core.exception.code.ExceptionCode.UNAUTHORIZED;

@Slf4j
@UtilityClass
public class TokenUtils {

    /**
     * 解析请求头中存储的 Authorization 信息，获取登录端信息
     */
    public static LoginPointEnum extractLoginPoint(String basicHeader) {
        if (StrUtil.isEmpty(basicHeader)) {
            throw BizException.wrap(UNAUTHORIZED);
        }
        String token = Base64Utils.decode2Str(basicHeader);
        int index = token.indexOf(StrPool.COLON);
        if (index == -1) {
            throw BizException.wrap(UNAUTHORIZED);
        }
        String str1 = token.substring(0, index);
        String str2 = token.substring(index + 1);
        LoginPointEnum loginPoint = LoginPointEnum.get(str1);
        if (loginPoint == null) {
            throw BizException.wrap(UNAUTHORIZED);
        }
        if (!loginPoint.getSecret().equals(str2)) {
            throw BizException.wrap(UNAUTHORIZED);
        }
        return loginPoint;
    }

    /**
     * 登录 Token 缓存 key
     */
    public static String loginTokenCacheKey(String token) {
        return TokenConstant.LOGIN_TOKEN_CACHE + token;
    }

    /**
     * 刷新 Token 缓存 key
     */
    public static String refreshTokenCacheKey(String token) {
        return TokenConstant.REFRESH_TOKEN_CACHE + token;
    }

    /**
     * 用户 Token 缓存 key
     */
    public static String userTokenCacheKey(UserTypeEnum userType, Long userId) {
        return TokenConstant.USER_TOKEN_CACHE + userType.lowerUnderscoreCode() + StrPool.COLON + userId;
    }
}
