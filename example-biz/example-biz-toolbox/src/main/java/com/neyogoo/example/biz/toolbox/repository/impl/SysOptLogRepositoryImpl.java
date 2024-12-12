package com.neyogoo.example.biz.toolbox.repository.impl;

import com.neyogoo.example.biz.toolbox.model.logging.SysOptLog;
import com.neyogoo.example.biz.toolbox.repository.SysOptLogCustom;
import com.neyogoo.example.common.core.model.PageReq;
import com.neyogoo.example.common.core.util.StrPool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

public class SysOptLogRepositoryImpl implements SysOptLogCustom {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public long queryCount(Query query) {
        return mongoTemplate.count(query, SysOptLog.class);
    }

    @Override
    public List<SysOptLog> queryPage(Query query, PageReq pageReq) {
        Pageable pageable = PageRequest.of(
                Math.toIntExact(pageReq.getCurrent() - 1),
                Math.toIntExact(pageReq.getSize())
        );
        Sort sort = Sort.by(Sort.Direction.DESC, StrPool.MONGO_ID);
        query.with(sort).with(pageable);
        return mongoTemplate.find(query, SysOptLog.class);
    }
}
