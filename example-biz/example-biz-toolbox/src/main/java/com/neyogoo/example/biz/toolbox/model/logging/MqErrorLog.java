package com.neyogoo.example.biz.toolbox.model.logging;

import com.neyogoo.example.common.mq.model.MqErrorLogMsg;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * MQ 消费异常记录
 */
@Getter
@Setter
@Document("t_mq_error_log")
public class MqErrorLog extends MqErrorLogMsg {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;
}
