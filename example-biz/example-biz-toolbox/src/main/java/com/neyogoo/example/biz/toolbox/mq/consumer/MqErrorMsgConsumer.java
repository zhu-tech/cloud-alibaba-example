package com.neyogoo.example.biz.toolbox.mq.consumer;

import com.neyogoo.example.biz.common.constant.BizMqConstant;
import com.neyogoo.example.biz.toolbox.model.logging.MqErrorLog;
import com.neyogoo.example.biz.toolbox.repository.MqErrorLogRepository;
import com.neyogoo.example.common.mq.listener.ConsumerListenerBean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * MQ 消费异常记录
 */
@Slf4j
@Component
public class MqErrorMsgConsumer extends ConsumerListenerBean<MqErrorLog> {

    @Autowired
    private MqErrorLogRepository mqErrorLogRepository;

    @Override
    public String getQueueName() {
        return BizMqConstant.MQ_ERROR_MSG_QUEUE;
    }

    @Override
    public Class<MqErrorLog> getJsonClass() {
        return MqErrorLog.class;
    }

    @Override
    public boolean onMessage(Message message, MqErrorLog param) {
        try {
            mqErrorLogRepository.insert(param);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return true;
    }
}
