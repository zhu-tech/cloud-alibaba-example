package com.neyogoo.example.biz.toolbox.model.msg;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.FieldNameConstants;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * 短信发送信息
 */
@Getter
@Setter
@FieldNameConstants
@Accessors(chain = true)
@Document("t_biz_sms_msg")
public class SmsMsg {

    @Id
    private String id;

    /**
     * 机构id
     */
    private Long orgId;

    /**
     * 模板编码
     */
    private String templateCode;

    /**
     * 手机号
     */
    private String phoneNumber;

    /**
     * 短信标题
     */
    private String smsTitle;

    /**
     * 模板所需参数
     */
    private Map<String, Object> templateParams;

    /**
     * 调用接口返回数据
     */
    private SendSmsResponse sendSmsResponse;

    /**
     * 短信发送时间
     */
    private LocalDateTime sendTime;

    /**
     * 发送人id
     */
    private Long sendUserId;

    /**
     * 发送人姓名
     */
    private String sendUserName;

    @Data
    @NoArgsConstructor
    public static class SendSmsResponse {

        private String requestId;

        private String bizId;

        private String code;

        private String message;
    }
}
