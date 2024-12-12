package com.neyogoo.example.common.core.constant;

/**
 * 基础常量
 */
public interface BasicConstant {

    /**
     * 项目前缀（自定义配置前缀）
     */
    String PROJECT_PREFIX = "example";

    String LOG_PREFIX = PROJECT_PREFIX + ".log";
    String REDIS_PREFIX = PROJECT_PREFIX + ".redis";
    String MQ_PREFIX = PROJECT_PREFIX + ".rabbitmq";
    String DATABASE_PREFIX = PROJECT_PREFIX + ".database";
    String WEBMVC_PREFIX = PROJECT_PREFIX + ".webmvc";
    String ALIYUN_PREFIX = PROJECT_PREFIX + ".aliyun";
    String API_VERIFY_PREFIX = PROJECT_PREFIX + ".api-verify";

    /**
     * 包扫描路径
     */
    String BASIC_SCAN_LOCATION = "com.neyogoo.example";
    String COMPONENT_SCAN_LOCATION = BASIC_SCAN_LOCATION;
    String MAPPER_SCAN_LOCATION = BASIC_SCAN_LOCATION + ".**.*.dao";
    String REPOSITORY_SCAN_LOCATION = BASIC_SCAN_LOCATION + ".**.*.repository";
    /**
     * 默认数字型id
     */
    Long DEFAULT_ID = 0L;
    /**
     * 默认表示不存在的数字型id
     */
    Long DEFAULT_NON_ID = -1L;
    /**
     * 默认字符串型id
     */
    String DEFAULT_ID_STR = "0";
    /**
     * 防重提交锁前缀
     */
    String REPEAT_SUBMIT_LOCK_PREFIX = "repeat:";
}
