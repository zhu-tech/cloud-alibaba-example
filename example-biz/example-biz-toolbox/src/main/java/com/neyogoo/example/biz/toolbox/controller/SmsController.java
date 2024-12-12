package com.neyogoo.example.biz.toolbox.controller;

import com.neyogoo.example.biz.toolbox.service.msg.SmsMsgService;
import com.neyogoo.example.biz.toolbox.vo.request.msg.SmsSendReqVO;
import com.neyogoo.example.common.core.context.ContextUtils;
import com.neyogoo.example.common.core.model.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/sms")
@Api(value = "Sms", tags = "短信通知")
public class SmsController {

    @Autowired
    private SmsMsgService smsSendService;

    @ApiOperation("批量发送短信")
    @PostMapping("/send")
    public R<Boolean> send(@RequestBody @Validated SmsSendReqVO reqVO) {
        reqVO.setSendUserId(ContextUtils.getUserId());
        reqVO.setSendUserName(ContextUtils.getUserName());
        if (reqVO.getPhoneNumbers().size() == 1) {
            return R.success(smsSendService.sendMessage(reqVO));
        } else {
            return R.success(smsSendService.sendBatchMessage(reqVO));
        }
    }
}
