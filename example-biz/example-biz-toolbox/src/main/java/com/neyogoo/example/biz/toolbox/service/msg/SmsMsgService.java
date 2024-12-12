package com.neyogoo.example.biz.toolbox.service.msg;

import com.neyogoo.example.biz.toolbox.vo.request.msg.SmsSendReqVO;

public interface SmsMsgService {
    /**
     * 发送短信 - 批量
     */
    boolean sendBatchMessage(SmsSendReqVO smsSendReqVO);

    /**
     * 发送短信
     */
    boolean sendMessage(SmsSendReqVO smsSendReqVO);
}
