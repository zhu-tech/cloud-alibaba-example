package com.neyogoo.example.admin.sys.mq.producer;

import com.neyogoo.example.admin.sys.vo.entity.mq.SysLoginLogEntity;
import com.neyogoo.example.biz.common.constant.BizMqConstant;
import com.neyogoo.example.common.core.util.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class LoginLogProducer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 发送登录日志
     */
    public void sendLoginLogMsg(SysLoginLogEntity param) {
        rabbitTemplate.convertAndSend(BizMqConstant.SYS_LOGIN_LOG_QUEUE, JsonUtils.toJson(param));
    }
}
