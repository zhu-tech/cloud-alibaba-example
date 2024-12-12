package com.neyogoo.example.admin.sys.config;

import com.anji.captcha.model.common.Const;
import com.anji.captcha.service.CaptchaCacheService;
import com.anji.captcha.service.CaptchaService;
import com.anji.captcha.service.impl.CaptchaServiceFactory;
import com.anji.captcha.util.ImageUtils;
import com.anji.captcha.util.StringUtils;
import com.neyogoo.example.admin.sys.config.properties.CaptchaProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.util.Base64Utils;
import org.springframework.util.FileCopyUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Slf4j
@Configuration
@EnableConfigurationProperties(CaptchaProperties.class)
public class SysCaptchaConfiguration {

    private final CaptchaProperties captchaProperties;

    public SysCaptchaConfiguration(final CaptchaProperties captchaProperties) {
        this.captchaProperties = captchaProperties;
    }

    @Bean
    @ConditionalOnMissingBean
    public CaptchaService captchaService() {
        log.info("滑动验证自定义配置项：{}", captchaProperties.toString());
        Properties config = new Properties();
        config.put(Const.CAPTCHA_CACHETYPE, captchaProperties.getCacheType().name());
        config.put(Const.CAPTCHA_WATER_MARK, captchaProperties.getWaterMark());
        config.put(Const.CAPTCHA_FONT_TYPE, captchaProperties.getFontType());
        config.put(Const.CAPTCHA_TYPE, captchaProperties.getType().getCodeValue());
        config.put(Const.CAPTCHA_INTERFERENCE_OPTIONS, captchaProperties.getInterferenceOptions());
        config.put(Const.ORIGINAL_PATH_JIGSAW, captchaProperties.getJigsaw());
        config.put(Const.ORIGINAL_PATH_PIC_CLICK, captchaProperties.getPicClick());
        config.put(Const.CAPTCHA_SLIP_OFFSET, captchaProperties.getSlipOffset());
        config.put(Const.CAPTCHA_AES_STATUS, String.valueOf(captchaProperties.getAesStatus()));
        config.put(Const.CAPTCHA_WATER_FONT, captchaProperties.getWaterFont());
        config.put(Const.CAPTCHA_CACAHE_MAX_NUMBER, captchaProperties.getCacheNumber());
        config.put(Const.CAPTCHA_TIMING_CLEAR_SECOND, captchaProperties.getTimingClear());

        config.put(Const.HISTORY_DATA_CLEAR_ENABLE, captchaProperties.isHistoryDataClearEnable() ? "1" : "0");

        config.put(Const.REQ_FREQUENCY_LIMIT_ENABLE, captchaProperties.isReqFrequencyLimitEnable() ? "1" : "0");
        config.put(Const.REQ_GET_LOCK_LIMIT, captchaProperties.getReqGetLockLimit() + "");
        config.put(Const.REQ_GET_LOCK_SECONDS, captchaProperties.getReqGetLockSeconds() + "");
        config.put(Const.REQ_GET_MINUTE_LIMIT, captchaProperties.getReqGetMinuteLimit() + "");
        config.put(Const.REQ_CHECK_MINUTE_LIMIT, captchaProperties.getReqCheckMinuteLimit() + "");
        config.put(Const.REQ_VALIDATE_MINUTE_LIMIT, captchaProperties.getReqVerifyMinuteLimit() + "");

        config.put(Const.CAPTCHA_FONT_SIZE, captchaProperties.getFontSize() + "");
        config.put(Const.CAPTCHA_FONT_STYLE, captchaProperties.getFontStyle() + "");
        config.put(Const.CAPTCHA_WORD_COUNT, captchaProperties.getClickWordCount() + "");

        if ((StringUtils.isNotBlank(captchaProperties.getJigsaw())
                && captchaProperties.getJigsaw().startsWith("classpath:"))
                || (StringUtils.isNotBlank(captchaProperties.getPicClick())
                && captchaProperties.getPicClick().startsWith("classpath:"))) {
            // 自定义resources目录下初始化底图
            config.put(Const.CAPTCHA_INIT_ORIGINAL, "true");
            initializeBaseMap(captchaProperties.getJigsaw(), captchaProperties.getPicClick());
        }
        return CaptchaServiceFactory.getInstance(config);
    }

    @Bean(name = "CaptchaCacheService")
    public CaptchaCacheService captchaCacheService(CaptchaProperties captchaProperties) {
        return CaptchaServiceFactory.getCache(captchaProperties.getCacheType().name());
    }

    private static void initializeBaseMap(String jigsaw, String picClick) {
        ImageUtils.cacheBootImage(getResourcesImagesFile(jigsaw + "/original/*.png"),
                getResourcesImagesFile(jigsaw + "/slidingBlock/*.png"),
                getResourcesImagesFile(picClick + "/*.png"));
    }

    private static Map<String, String> getResourcesImagesFile(String path) {
        Map<String, String> imgMap = new HashMap<>(16);
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        try {
            Resource[] resources = resolver.getResources(path);
            for (Resource resource : resources) {
                byte[] bytes = FileCopyUtils.copyToByteArray(resource.getInputStream());
                String string = Base64Utils.encodeToString(bytes);
                String filename = resource.getFilename();
                imgMap.put(filename, string);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return imgMap;
    }
}
