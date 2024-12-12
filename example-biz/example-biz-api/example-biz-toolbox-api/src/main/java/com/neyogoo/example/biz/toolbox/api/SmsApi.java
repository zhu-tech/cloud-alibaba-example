package com.neyogoo.example.biz.toolbox.api;

import com.neyogoo.example.biz.toolbox.fallback.SmsApiFallbackFactory;
import com.neyogoo.example.biz.toolbox.vo.request.SmsSendReqVO;
import com.neyogoo.example.common.core.model.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 短信
 */
@FeignClient(name = "example-biz-toolbox", fallbackFactory = SmsApiFallbackFactory.class,
        path = "/v1/sms", qualifiers = "smsApi")
public interface SmsApi {

    /**
     * 发送短信
     */
    @PostMapping("/send")
    R<Boolean> send(@RequestBody @Validated SmsSendReqVO reqVO);
}
