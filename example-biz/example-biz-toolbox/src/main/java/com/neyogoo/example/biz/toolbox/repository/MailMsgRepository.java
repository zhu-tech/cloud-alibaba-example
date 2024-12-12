package com.neyogoo.example.biz.toolbox.repository;


import com.neyogoo.example.biz.toolbox.model.msg.MailMsg;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * 站内信
 */
@Repository
public interface MailMsgRepository extends MongoRepository<MailMsg, String>, MailMsgCustom {
}
