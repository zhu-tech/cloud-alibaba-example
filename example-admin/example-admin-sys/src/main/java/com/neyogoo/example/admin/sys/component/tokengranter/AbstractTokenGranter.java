package com.neyogoo.example.admin.sys.component.tokengranter;

import cn.hutool.core.text.CharSequenceUtil;
import com.neyogoo.example.admin.sys.config.properties.LoginProperties;
import com.neyogoo.example.admin.sys.constant.SysCacheConstant;
import com.neyogoo.example.admin.sys.service.UserLoginService;
import com.neyogoo.example.admin.sys.service.ext.CaptcheCheckService;
import com.neyogoo.example.admin.sys.service.ext.SmsCodeService;
import com.neyogoo.example.admin.sys.vo.entity.UserPwdCheckEntity;
import com.neyogoo.example.admin.sys.vo.request.token.LoginSmsCheckReqVO;
import com.neyogoo.example.admin.sys.vo.response.token.AuthInfoRespVO;
import com.neyogoo.example.common.cache.model.CacheHashKey;
import com.neyogoo.example.common.cache.model.CacheKey;
import com.neyogoo.example.common.cache.repository.CacheOps;
import com.neyogoo.example.common.core.exception.BizException;
import com.neyogoo.example.common.core.exception.UnauthorizedException;
import com.neyogoo.example.common.core.util.StrPool;
import com.neyogoo.example.common.core.util.UUidUtils;
import com.neyogoo.example.common.core.util.WebUtils;
import com.neyogoo.example.common.token.enumeration.UserTypeEnum;
import com.neyogoo.example.common.token.model.UserTokenCache;
import com.neyogoo.example.common.token.util.TokenUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;

import static com.neyogoo.example.common.token.constant.TokenConstant.REFRESH_TOKEN_CACHE;

/**
 * token 分发器
 */
@Slf4j
public abstract class AbstractTokenGranter<T extends AuthInfoRespVO> implements TokenGranter<T> {

    @Autowired
    protected CacheOps cacheOps;
    @Autowired
    protected LoginProperties loginProperties;
    @Autowired
    protected UserLoginService userLoginService;
    @Autowired
    private SmsCodeService smsCodeService;
    @Autowired
    protected CaptcheCheckService captcheCheckService;

    /**
     * 验证短信验证码
     */
    protected void checkSmsCode(LoginSmsCheckReqVO reqVO) {
        smsCodeService.checkForUserLogin(reqVO);
    }

    /**
     * 验证用户名密码是否正确
     */
    protected void checkUserPwd(UserPwdCheckEntity bo) {
        // 跳过密码验证
        if (!loginProperties.getVerifyPassword()) {
            return;
        }
        // 进行密码验证
        String pwdMD5 = LoginProperties.encodePwd(bo.getInputPwd(), bo.getUserSalt());
        CacheKey pwdErrorCacheKey = buildPwdErrorCacheKey(bo.getUserType(), bo.getUserId());
        if (pwdMD5.equals(bo.getUserPwd())) {
            // 密码正确，清除密码错误次数
            cacheOps.del(pwdErrorCacheKey);
            return;
        }
        // 记录错误次数 + 1
        Integer pwdErrorNum = cacheOps.get(pwdErrorCacheKey, false);
        if (pwdErrorNum == null) {
            pwdErrorNum = 0;
        }
        pwdErrorNum += 1;
        cacheOps.set(pwdErrorCacheKey, pwdErrorNum);

        // 不限制密码错误次数或锁定时间
        if (loginProperties.getMaxPwdErrorNum() < 0 || loginProperties.getPwdErrorLockTime() < 0) {
            throw UnauthorizedException.wrap("用户名或密码错误");
        }

        if (pwdErrorNum >= loginProperties.getMaxPwdErrorNum()) {
            lockUserById(bo.getUserId());
            // 次数达到上限，锁定用户
            String msg = CharSequenceUtil.format("密码错误次数已达到上限，请{}分钟后重试！",
                    (pwdErrorNum), loginProperties.getPwdErrorLockTime()
            );
            throw UnauthorizedException.wrap(msg);
        } else {
            String msg = CharSequenceUtil.format("用户名或密码错误{}次，连续输错{}次您将被锁定！",
                    (pwdErrorNum), loginProperties.getMaxPwdErrorNum()
            );
            throw UnauthorizedException.wrap(msg);
        }
    }

    /**
     * 生成登录 Token 和刷新 Token
     */
    protected void generalToken(AuthInfoRespVO authInfo) {
        authInfo.getToken()
                .setValue(UUidUtils.uuid())
                .setExpire(LocalDateTime.now().plusMinutes(loginProperties.getTokenExpire()));
        authInfo.getRefresh()
                .setValue(UUidUtils.uuid())
                .setExpire(LocalDateTime.now().plusMinutes(loginProperties.getRefreshExpire()));
        authInfo.setRefreshToken(authInfo.getRefresh().getValue());
    }

    /**
     * 缓存登录 Token 和刷新 Token 信息
     */
    protected void cacheToken(AuthInfoRespVO authInfo) {
        String loginToken = authInfo.getToken().getValue();
        String refreshToken = authInfo.getRefresh().getValue();
        LocalDateTime now = LocalDateTime.now();

        // 登录 Token
        CacheKey loginTokenCacheKey = buildLoginTokenCacheKey(loginToken);
        cacheOps.set(loginTokenCacheKey, authInfo.toLoginTokenCache());

        // 刷新 Token
        CacheKey refreshTokenCacheKey = buildRefreshTokenCacheKey(refreshToken);
        cacheOps.set(refreshTokenCacheKey, authInfo.toRefreshTokeCache());

        CacheHashKey userTokenCacheKey = buildUserTokenCacheKey(
                authInfo.getUser().getUserType(), authInfo.getUser().getUserId()
        );

        // 用户 Token
        Map<String, UserTokenCache> userTokenCacheMap = cacheOps.hGetAll(userTokenCacheKey);

        Integer tokenConcurrency = loginProperties.getTokenConcurrency();
        // 同时生效数量达到上限
        if (tokenConcurrency > 0 && userTokenCacheMap.size() >= tokenConcurrency) {
            // 移除已过期的
            Iterator<Map.Entry<String, UserTokenCache>> iterator = userTokenCacheMap.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, UserTokenCache> entry = iterator.next();
                if (entry.getValue().getExpireTime().isAfter(now)) {
                    userTokenCacheKey.setField(entry.getKey());
                    cacheOps.hDel(userTokenCacheKey);
                    iterator.remove();
                }
            }
            // 移除创建时间最早的
            if (userTokenCacheMap.size() >= tokenConcurrency) {
                userTokenCacheMap.entrySet().stream()
                        .min(Comparator.comparing(o -> o.getValue().getCreateTime()))
                        .ifPresent(o -> {
                            userTokenCacheKey.setField(o.getKey());
                            cacheOps.hDel(userTokenCacheKey);
                        });
            }
        }

        userTokenCacheKey.setField(loginToken);
        cacheOps.hSet(userTokenCacheKey, authInfo.toUserTokeCache());
    }

    protected void removeRefreshToken(String refreshToken) {
        cacheOps.del(REFRESH_TOKEN_CACHE + refreshToken);
    }

    /**
     * 更新用户最后登录信息
     */
    protected void updateUserLogin(AuthInfoRespVO authInfo, HttpServletRequest request) {
        try {
            userLoginService.updateByAuthInfo(authInfo, WebUtils.getIpAddr(request));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    /**
     * 登录 Token 缓存
     */
    protected CacheKey buildLoginTokenCacheKey(String loginToken) {
        return new CacheKey(
                TokenUtils.loginTokenCacheKey(loginToken),
                Duration.ofMinutes(loginProperties.getTokenExpire())
        );
    }

    /**
     * 刷新 Token 缓存
     */
    protected CacheKey buildRefreshTokenCacheKey(String refreshToken) {
        return new CacheKey(
                TokenUtils.refreshTokenCacheKey(refreshToken),
                Duration.ofMinutes(loginProperties.getRefreshExpire())
        );
    }

    /**
     * 用户 Token 缓存
     */
    protected CacheHashKey buildUserTokenCacheKey(UserTypeEnum userType, Long userId) {
        return new CacheHashKey(
                TokenUtils.userTokenCacheKey(userType, userId), "",
                Duration.ofHours(12)
        );
    }

    /**
     * 密码错误次数缓存
     */
    protected CacheKey buildPwdErrorCacheKey(UserTypeEnum userType, Long userId) {
        return new CacheKey(
                SysCacheConstant.USER_PWD_ERROR + userType.lowerUnderscoreCode() + StrPool.COLON + userId,
                Duration.ofHours(12)
        );
    }

    /**
     * 根据用户id锁定用户账户
     */
    protected void lockUserById(Long userId) {
        throw new BizException("未实现该登录方式用户锁定方法");
    }
}
