package com.neyogoo.example.biz.toolbox.mq.consumer;

import cn.hutool.core.collection.CollectionUtil;
import com.neyogoo.example.biz.common.constant.BizMqConstant;
import com.neyogoo.example.biz.toolbox.service.msg.MailMsgService;
import com.neyogoo.example.biz.toolbox.vo.request.msg.MailMsgSaveReqVO;
import com.neyogoo.example.common.core.exception.BizException;
import com.neyogoo.example.common.core.util.ValidatorUtils;
import com.neyogoo.example.common.mq.listener.ConsumerListenerBean;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.core.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 发送站内信
 */
@Slf4j
@Component
public class MailMsgConsumer extends ConsumerListenerBean<MailMsgSaveReqVO> {

    @Autowired
    private MailMsgService mailMsgService;

    @Override
    public String getQueueName() {
        return BizMqConstant.MAIL_MSG_QUEUE;
    }

    @Override
    public Class<MailMsgSaveReqVO> getJsonClass() {
        return MailMsgSaveReqVO.class;
    }

    @Override
    public boolean onMessage(Message message, MailMsgSaveReqVO param) {
        try {
            List<String> errors = ValidatorUtils.validateEntity(param);
            if (CollectionUtil.isNotEmpty(errors)) {
                throw BizException.wrap("参数有误：" + StringUtils.join(errors, "；"));
            }
            mailMsgService.saveBatch(param);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            sendErrorMsg(param, e, BizMqConstant.MQ_ERROR_MSG_QUEUE);
        }
        return true;
    }
}
