package com.neyogoo.example.biz.toolbox.repository;

import com.neyogoo.example.biz.toolbox.model.msg.SmsMsg;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * 短信
 */
@Repository
public interface SmsMsgRepository extends MongoRepository<SmsMsg, String> {

}
