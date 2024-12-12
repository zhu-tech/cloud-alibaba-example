package com.neyogoo.example.biz.toolbox.mq.consumer;

import com.neyogoo.example.biz.common.constant.BizMqConstant;
import com.neyogoo.example.biz.toolbox.model.logging.SysOptLog;
import com.neyogoo.example.biz.toolbox.repository.SysOptLogRepository;
import com.neyogoo.example.common.mq.listener.ConsumerListenerBean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 系统操作日志
 */
@Slf4j
@Component
public class SysOptLogConsumer extends ConsumerListenerBean<SysOptLog> {

    @Autowired
    private SysOptLogRepository sysOptLogRepository;

    @Override
    public String getQueueName() {
        return BizMqConstant.SYS_OPT_LOG_QUEUE;
    }

    @Override
    public Class<SysOptLog> getJsonClass() {
        return SysOptLog.class;
    }

    @Override
    public boolean onMessage(Message message, SysOptLog param) {
        try {
            sysOptLogRepository.insert(param);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return true;
    }
}
