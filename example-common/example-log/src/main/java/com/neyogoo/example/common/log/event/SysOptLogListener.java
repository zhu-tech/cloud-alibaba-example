package com.neyogoo.example.common.log.event;


import com.neyogoo.example.common.log.model.SysOptLogMsg;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Async;

import java.util.function.Consumer;


/**
 * 异步监听日志事件
 */
@Slf4j
@AllArgsConstructor
public class SysOptLogListener {

    private final Consumer<SysOptLogMsg> consumer;

    @Async
    @Order
    @EventListener(SysOptLogEvent.class)
    public void saveOperationLogMessage(SysOptLogEvent event) {
        SysOptLogMsg logMessage = (SysOptLogMsg) event.getSource();
        consumer.accept(logMessage);
    }
}
