package com.neyogoo.example.biz.toolbox.repository;

import com.neyogoo.example.biz.toolbox.model.logging.SysOptLog;
import com.neyogoo.example.common.core.model.PageReq;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 系统操作日志
 */
@Repository
public interface SysOptLogCustom {

    long queryCount(Query query);

    List<SysOptLog> queryPage(Query query, PageReq pageReq);
}
