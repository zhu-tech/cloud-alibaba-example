package com.neyogoo.example.biz.toolbox.fallback;

import com.neyogoo.example.biz.toolbox.api.SmsApi;
import com.neyogoo.example.biz.toolbox.vo.request.SmsSendReqVO;
import com.neyogoo.example.common.core.model.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SmsApiFallbackFactory implements FallbackFactory<SmsApi> {

    @Override
    public SmsApi create(Throwable cause) {

        return new SmsApi() {

            @Override
            public R<Boolean> send(SmsSendReqVO reqVO) {
                log.error("send sms message error, reqVO = {}, cause = {}", reqVO, cause.getMessage());
                return R.fail("发送短信失败");
            }
        };
    }
}
