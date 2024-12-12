package com.neyogoo.example.biz.toolbox.repository;

import com.neyogoo.example.biz.toolbox.model.logging.SysLoginLog;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * 系统登录日志
 */
@Repository
public interface SysLoginLogRepository extends MongoRepository<SysLoginLog, String> {

}
