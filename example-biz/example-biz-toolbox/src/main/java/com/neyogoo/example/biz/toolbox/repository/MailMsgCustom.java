package com.neyogoo.example.biz.toolbox.repository;

import com.neyogoo.example.biz.toolbox.model.msg.MailMsg;
import com.neyogoo.example.common.core.model.PageReq;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 站内信
 */
@Repository
public interface MailMsgCustom {

    long queryCount(Query query);

    List<MailMsg> queryPage(Query query, PageReq pageReq);

    List<MailMsg> queryReminder(Query query);

    boolean deleteMsgByIds(List<String> ids);

    boolean updateMsgByIds(List<String> ids);

    MailMsg queryById(String id);

    boolean updateMsgById(String id);

}
