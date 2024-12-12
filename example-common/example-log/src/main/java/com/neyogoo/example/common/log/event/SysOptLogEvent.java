package com.neyogoo.example.common.log.event;

import com.neyogoo.example.common.log.model.SysOptLogMsg;
import org.springframework.context.ApplicationEvent;

/**
 * 系统日志事件
 */
public class SysOptLogEvent extends ApplicationEvent {

    public SysOptLogEvent(SysOptLogMsg source) {
        super(source);
    }
}
