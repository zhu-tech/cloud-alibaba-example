package com.neyogoo.example.biz.toolbox.mq.consumer;

import com.neyogoo.example.biz.common.constant.BizMqConstant;
import com.neyogoo.example.biz.toolbox.service.msg.SmsMsgService;
import com.neyogoo.example.biz.toolbox.vo.request.msg.SmsSendReqVO;
import com.neyogoo.example.common.core.context.ContextUtils;
import com.neyogoo.example.common.core.util.JsonUtils;
import com.neyogoo.example.common.mq.listener.ConsumerListenerBean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 短信发送
 */
@Slf4j
@Component
public class SmsSendConsumer extends ConsumerListenerBean<SmsSendReqVO> {

    @Autowired
    private SmsMsgService smsMsgService;

    @Override
    public String getQueueName() {
        return BizMqConstant.SMS_SEND_QUEUE;
    }

    @Override
    public Class<SmsSendReqVO> getJsonClass() {
        return SmsSendReqVO.class;
    }

    @Override
    public boolean onMessage(Message message, SmsSendReqVO param) {
        try {
            validateParam(param);
            param.setSendUserId(ContextUtils.getUserId());
            param.setSendUserName(ContextUtils.getUserName());
            if (param.getPhoneNumbers().size() == 1) {
                smsMsgService.sendMessage(param);
            } else {
                smsMsgService.sendBatchMessage(param);
            }
        } catch (Exception e) {
            log.error("consume {} error, reqVO = {}", getQueueName(), JsonUtils.toJson(param), e);
            sendErrorMsg(param, e, BizMqConstant.MQ_ERROR_MSG_QUEUE);
        }
        return true;
    }
}
