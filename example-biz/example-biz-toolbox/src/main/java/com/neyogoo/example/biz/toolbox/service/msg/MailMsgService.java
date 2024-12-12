package com.neyogoo.example.biz.toolbox.service.msg;

import com.neyogoo.example.biz.toolbox.model.msg.MailMsg;
import com.neyogoo.example.biz.toolbox.vo.request.msg.MailMsgPageReqVO;
import com.neyogoo.example.biz.toolbox.vo.request.msg.MailMsgRemindReqVO;
import com.neyogoo.example.biz.toolbox.vo.request.msg.MailMsgSaveReqVO;
import com.neyogoo.example.biz.toolbox.vo.response.msg.MailMsgRespVO;
import com.neyogoo.example.common.core.model.PageReq;
import com.neyogoo.example.common.core.model.PageResp;

import java.util.List;

public interface MailMsgService {

    /**
     * 查询站内信列表（分页）
     */
    PageResp<MailMsgRespVO> queryPage(PageReq<MailMsgPageReqVO> pageReq);

    /**
     * 根据id查询站内信
     */
    MailMsg getById(String id);

    /**
     * 查询站内信消息提醒
     */
    List<MailMsg> queryMsgReminder(MailMsgRemindReqVO bizMailRemindReqVO);

    /**
     * 批量保存站内信
     */
    boolean saveBatch(MailMsgSaveReqVO saveReqVO);

    /**
     * 根据id列表批量修改为已读
     */
    boolean updateReadAllByIds(List<String> ids);

    /**
     * 根据id修改为已读
     */
    boolean updateReadOneById(String id);

    /**
     * 根据id删除站内信
     */
    boolean deleteByIds(List<String> ids);

}
