package com.neyogoo.example.biz.toolbox.model.logging;

import com.neyogoo.example.common.log.model.SysOptLogMsg;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 系统操作日志
 */
@Getter
@Setter
@FieldNameConstants
@Document("t_sys_opt_log")
public class SysOptLog extends SysOptLogMsg {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;
}
