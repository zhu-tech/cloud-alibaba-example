package com.neyogoo.example.admin.sys.service.impl.ext;

import com.neyogoo.example.admin.sys.constant.SysCacheConstant;
import com.neyogoo.example.admin.sys.service.ext.SmsCodeService;
import com.neyogoo.example.admin.sys.vo.request.token.LoginSmsCheckReqVO;
import com.neyogoo.example.admin.sys.vo.request.token.LoginSmsSendReqVO;
import com.neyogoo.example.biz.common.enumeration.toolbox.SmsTemplateEnum;
import com.neyogoo.example.biz.toolbox.api.SmsApi;
import com.neyogoo.example.biz.toolbox.vo.request.SmsSendReqVO;
import com.neyogoo.example.common.cache.model.CacheKey;
import com.neyogoo.example.common.cache.repository.CacheOps;
import com.neyogoo.example.common.core.util.ArgumentAssert;
import com.neyogoo.example.common.core.util.RUtils;
import com.neyogoo.example.common.core.util.StrPool;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class SmsCodeServiceImpl implements SmsCodeService {

    @Value("${spring.profiles.active:dev}")
    private String profiles;
    @Value("${example.sms.verification.enabled:false}")
    private Boolean verification;
    @Autowired
    private CacheOps cacheOps;
    @Autowired
    private SmsApi smsApi;

    /**
     * 发送短信验证码 - 用户登录
     */
    @Override
    public boolean sendForUserLogin(LoginSmsSendReqVO reqVO) {
        if (!enableVerification()) {
            return true;
        }
        String key = SysCacheConstant.SMS_CODE_USER_LOGIN + reqVO.userType().getCode()
                + StrPool.COLON + reqVO.getUserMobile();
        CacheKey cacheKey = new CacheKey(key, Duration.ofMinutes(5));
        // 生成验证码并缓存
        String smsCode = cacheOps.get(cacheKey, k -> RandomStringUtils.randomNumeric(6).toUpperCase());
        // 发送短信
        return sendSmsCode(smsCode, reqVO.getUserMobile());
    }

    /**
     * 验证短信验证码 - 用户登录
     */
    @Override
    public void checkForUserLogin(LoginSmsCheckReqVO reqVO) {
        if (!enableVerification()) {
            return;
        }
        // 获取缓存验证码
        String key = SysCacheConstant.SMS_CODE_USER_LOGIN + reqVO.userType().getCode()
                + StrPool.COLON + reqVO.getUserMobile();
        CacheKey cacheKey = new CacheKey(key);
        String cacheSmsCode = cacheOps.get(cacheKey, false);
        ArgumentAssert.notBlank(cacheSmsCode, "验证码已失效，请重新获取");
        // 验证码是否相等
        ArgumentAssert.equals(reqVO.getSmsCode(), cacheSmsCode, "验证码错误");
        // 删除缓存验证码
        cacheOps.del(cacheKey);
    }

    /**
     * 发送验证码
     */
    private boolean sendSmsCode(String smsCode, String userAccount) {
        Map<String, Object> params = new HashMap<>(16);
        params.put("code", smsCode);
        SmsSendReqVO reqVO = new SmsSendReqVO()
                .setTemplateCode(SmsTemplateEnum.VerificationCode.getTemplateCode())
                .setPhoneNumbers(Collections.singletonList(userAccount))
                .setParams(params);
        return RUtils.getDataThrowEx(smsApi.send(reqVO));
    }

    /**
     * 是否开启短信验证码校验，非生产环境可关闭
     */
    private boolean enableVerification() {
        // 生产环境必须打开
        if (StrPool.PROD.equalsIgnoreCase(profiles)) {
            return true;
        }
        return verification;
    }
}
