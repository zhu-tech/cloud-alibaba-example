package com.neyogoo.example.biz.toolbox.repository.impl;

import com.neyogoo.example.biz.toolbox.model.msg.MailMsg;
import com.neyogoo.example.biz.toolbox.repository.MailMsgCustom;
import com.neyogoo.example.common.core.model.PageReq;
import com.neyogoo.example.common.core.util.StrPool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.time.LocalDateTime;
import java.util.List;

public class MailMsgRepositoryImpl implements MailMsgCustom {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public long queryCount(Query query) {
        return mongoTemplate.count(query, MailMsg.class);
    }

    @Override
    public List<MailMsg> queryPage(Query query, PageReq pageReq) {
        Pageable pageable = PageRequest.of(
                Math.toIntExact(pageReq.getCurrent() - 1), Math.toIntExact(pageReq.getSize())
        );
        Sort sort = Sort.by(Sort.Direction.DESC, MailMsg.Fields.sendTime);
        query.with(sort).with(pageable);
        return mongoTemplate.find(query, MailMsg.class);
    }

    @Override
    public List<MailMsg> queryReminder(Query query) {
        query.with(Sort.by(Sort.Direction.DESC, MailMsg.Fields.sendTime));
        return mongoTemplate.find(query, MailMsg.class);
    }

    @Override
    public boolean deleteMsgByIds(List<String> ids) {
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").in(ids));
        mongoTemplate.remove(query, MailMsg.class);
        return true;
    }

    @Override
    public boolean updateMsgByIds(List<String> ids) {
        Query query = new Query();
        query.addCriteria(Criteria.where(StrPool.MONGO_ID).in(ids));
        Update update = new Update();
        update.set(MailMsg.Fields.readTime, LocalDateTime.now());
        mongoTemplate.updateMulti(query, update, MailMsg.class);
        return true;
    }

    @Override
    public MailMsg queryById(String id) {
        return mongoTemplate.findById(id, MailMsg.class);
    }

    @Override
    public boolean updateMsgById(String id) {
        Query query = new Query();
        query.addCriteria(Criteria.where(StrPool.MONGO_ID).is(id));
        Update update = new Update();
        update.set(MailMsg.Fields.readTime, LocalDateTime.now());
        mongoTemplate.updateFirst(query, update, MailMsg.class);
        return true;
    }
}
