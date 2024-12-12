package com.neyogoo.example.biz.toolbox.model.msg;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.FieldNameConstants;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * 站内信
 */
@Getter
@Setter
@FieldNameConstants
@Accessors(chain = true)
@Document("t_biz_mail_msg")
public class MailMsg {
    @Id
    private String id;

    /**
     * 发送机构id
     */
    private Long sendOrgId;

    /**
     * 发送人Id
     */
    private Long sendUserId;

    /**
     * 发送人姓名
     */
    private String sendUserName;

    /**
     * 接收机构id
     */
    private Long receiveOrgId;

    /**
     * 接收人id
     */
    private Long receiveUserId;

    /**
     * 接收人姓名
     */
    private String receiveUserName;

    /**
     * 消息标题
     */
    private String msgTitle;

    /**
     * 消息文本
     */
    private String msgContent;

    /**
     * 发送时间
     */
    private LocalDateTime sendTime;

    /**
     * 已读时间
     */
    private LocalDateTime readTime;

    /**
     * 创建人id
     */
    private Long createUserId;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 扩展内容
     */
    private Map<String, Object> ext;
}
