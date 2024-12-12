package com.neyogoo.example.admin.sys.service.impl.ext;

import cn.hutool.core.collection.CollUtil;
import com.anji.captcha.model.common.ResponseModel;
import com.anji.captcha.model.vo.CaptchaVO;
import com.anji.captcha.service.CaptchaService;
import com.neyogoo.example.admin.sys.component.tokengranter.AbstractTokenGranter;
import com.neyogoo.example.admin.sys.config.properties.LoginProperties;
import com.neyogoo.example.admin.sys.mq.producer.LoginLogProducer;
import com.neyogoo.example.admin.sys.service.ext.SmsCodeService;
import com.neyogoo.example.admin.sys.service.ext.TokenService;
import com.neyogoo.example.admin.sys.vo.entity.mq.SysLoginLogEntity;
import com.neyogoo.example.admin.sys.vo.request.token.LoginReqVO;
import com.neyogoo.example.admin.sys.vo.request.token.LoginSmsSendReqVO;
import com.neyogoo.example.admin.sys.vo.request.token.LogoutReqVO;
import com.neyogoo.example.admin.sys.vo.request.token.RefreshTokenReqVO;
import com.neyogoo.example.admin.sys.vo.response.token.AuthInfoRespVO;
import com.neyogoo.example.common.cache.model.CacheHashKey;
import com.neyogoo.example.common.cache.model.CacheKey;
import com.neyogoo.example.common.cache.repository.CacheOps;
import com.neyogoo.example.common.core.exception.BizException;
import com.neyogoo.example.common.core.exception.UnauthorizedException;
import com.neyogoo.example.common.core.util.ArgumentAssert;
import com.neyogoo.example.common.core.util.StrPool;
import com.neyogoo.example.common.token.enumeration.UserTypeEnum;
import com.neyogoo.example.common.token.model.RefreshTokenCache;
import com.neyogoo.example.common.token.util.TokenUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.neyogoo.example.common.token.constant.TokenConstant.*;

/**
 * 登录令牌
 */
@Slf4j
@Service
public class TokenServiceImpl implements TokenService {

    @Autowired
    private CacheOps cacheOps;
    @Autowired
    private LoginProperties loginProperties;
    @Autowired
    private CaptchaService captchaService;
    @Autowired
    private SmsCodeService smsCodeService;
    @Autowired
    private LoginLogProducer loginLogProducer;
    @Autowired
    private List<AbstractTokenGranter> tokenGranters;

    /**
     * 登入
     */
    @Override
    public AuthInfoRespVO login(LoginReqVO reqVO, HttpServletRequest request) {
        for (AbstractTokenGranter granter : tokenGranters) {
            AuthInfoRespVO respVO = granter.login(reqVO, request);
            if (respVO != null) {
                return doAfterGetTokenSuccess(respVO, request, false);
            }
        }
        throw BizException.wrap("该登录方式尚未开放或已关闭，请联系管理员");
    }

    /**
     * 刷新 Token
     */
    @Override
    public AuthInfoRespVO refresh(RefreshTokenReqVO reqVO, HttpServletRequest request) {
        CacheKey cacheKey = new CacheKey(REFRESH_TOKEN_CACHE + reqVO.getRefreshToken());
        RefreshTokenCache tokenCache = cacheOps.get(cacheKey, false);
        if (tokenCache == null) {
            throw UnauthorizedException.wrap("登录信息已过期，请重新登录！");
        }
        reqVO.setCache(tokenCache);
        if (reqVO.getOrgId() == null) {
            reqVO.setOrgId(tokenCache.getOrg().getOrgId());
        }
        for (AbstractTokenGranter granter : tokenGranters) {
            AuthInfoRespVO respVO = granter.refresh(reqVO, request);
            if (respVO != null) {
                return doAfterGetTokenSuccess(respVO, request, true);
            }
        }
        throw BizException.wrap("该登录方式尚未开放或已关闭，请联系管理员");
    }

    /**
     * 登出
     */
    @Override
    public boolean logout(LogoutReqVO reqVO) {
        // 登录 Token 失效
        if (StringUtils.isNotBlank(reqVO.getLoginToken())) {
            String loginTokenKey = LOGIN_TOKEN_CACHE + reqVO.getLoginToken();
            cacheOps.del(loginTokenKey);
        }
        // 刷新 Token 失效
        if (StringUtils.isNotBlank(reqVO.getRefreshToken())) {
            String refreshTokenKey = REFRESH_TOKEN_CACHE + reqVO.getRefreshToken();
            cacheOps.del(refreshTokenKey);
        }
        // 用户 Token 中移除登录 Token
        if (reqVO.getUserType() != null && reqVO.getUserId() != null && StringUtils.isNotBlank(reqVO.getLoginToken())) {
            String userTokenKey = USER_TOKEN_CACHE + reqVO.getUserType().getCode() + StrPool.COLON + reqVO.getUserId();
            cacheOps.hDel(new CacheHashKey(userTokenKey, reqVO.getLoginToken()));
        }
        return false;
    }

    /**
     * 发送登录短信验证码
     */
    @Override
    public boolean sendSms(LoginSmsSendReqVO reqVO) {
        // 验证校验码
        if (loginProperties.getVerifyCaptcha()) {
            CaptchaVO captchaVO = new CaptchaVO();
            captchaVO.setCaptchaVerification(reqVO.getCaptchaVerification());
            ResponseModel response = captchaService.verification(captchaVO);
            ArgumentAssert.isTrue(response.isSuccess(), response.getRepMsg());
        }
        return smsCodeService.sendForUserLogin(reqVO);
    }

    /**
     * 删除单个用户 Token 缓存
     */
    @Async
    @Override
    public void validUserToken(UserTypeEnum userType, Long userId) {
        if (userId == null) {
            return;
        }
        // 根据 userId 查询当前记录的生效 token
        Set<Object> tokens = cacheOps.hKeys(
                new CacheHashKey(TokenUtils.userTokenCacheKey(userType, userId), "")
        );
        if (CollUtil.isEmpty(tokens)) {
            return;
        }
        // 删除 token 缓存
        String[] tokenArray = tokens.stream().map(String::valueOf).collect(Collectors.toSet()).toArray(String[]::new);
        cacheOps.del(tokenArray);
    }

    /**
     * 删除多个用户 Token 缓存
     */
    @Async
    @Override
    public void validUsersToken(UserTypeEnum userType, Collection<Long> userIds) {
        if (CollUtil.isEmpty(userIds)) {
            return;
        }
        for (Long userId : userIds) {
            validUserToken(userType, userId);
        }
    }

    /**
     * 登录成功后，登录用户信息放入线程，记录登录日志
     *
     * @param refresh 是否是刷新 Token
     */
    private AuthInfoRespVO doAfterGetTokenSuccess(AuthInfoRespVO respVO, HttpServletRequest request, boolean refresh) {
        respVO.putToThreadLocal();
        SysLoginLogEntity entity = SysLoginLogEntity.success(respVO, request);
        if (refresh) {
            entity.setDescription("刷新成功");
        }
        loginLogProducer.sendLoginLogMsg(entity);
        respVO.setLogin(null);
        return respVO;
    }
}
