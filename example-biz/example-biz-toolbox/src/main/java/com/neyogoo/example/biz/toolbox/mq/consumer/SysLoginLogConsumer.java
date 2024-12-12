package com.neyogoo.example.biz.toolbox.mq.consumer;

import com.neyogoo.example.biz.common.constant.BizMqConstant;
import com.neyogoo.example.biz.toolbox.model.logging.SysLoginLog;
import com.neyogoo.example.biz.toolbox.repository.SysLoginLogRepository;
import com.neyogoo.example.common.mq.listener.ConsumerListenerBean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 系统登录日志
 */
@Slf4j
@Component
public class SysLoginLogConsumer extends ConsumerListenerBean<SysLoginLog> {

    @Autowired
    private SysLoginLogRepository sysLoginLogRepository;

    @Override
    public String getQueueName() {
        return BizMqConstant.SYS_LOGIN_LOG_QUEUE;
    }

    @Override
    public Class<SysLoginLog> getJsonClass() {
        return SysLoginLog.class;
    }

    @Override
    public boolean onMessage(Message message, SysLoginLog param) {
        try {
            sysLoginLogRepository.insert(param);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return true;
    }
}
