package com.neyogoo.example.biz.toolbox.repository;

import com.neyogoo.example.biz.toolbox.model.logging.SysOptLog;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * 系统操作日志
 */
@Repository
public interface SysOptLogRepository extends MongoRepository<SysOptLog, String>, SysOptLogCustom {

}
