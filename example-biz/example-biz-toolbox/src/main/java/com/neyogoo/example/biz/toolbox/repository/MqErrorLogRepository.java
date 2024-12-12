package com.neyogoo.example.biz.toolbox.repository;

import com.neyogoo.example.biz.toolbox.model.logging.MqErrorLog;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * MQ 消费异常记录
 */
@Repository
public interface MqErrorLogRepository extends MongoRepository<MqErrorLog, String> {

}
