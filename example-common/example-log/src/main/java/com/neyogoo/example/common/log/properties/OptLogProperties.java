package com.neyogoo.example.common.log.properties;

import com.neyogoo.example.common.core.constant.BasicConstant;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 操作日志配置类
 */
@Data
@NoArgsConstructor
@ConfigurationProperties(prefix = BasicConstant.LOG_PREFIX)
public class OptLogProperties {

    /**
     * 系统操作日志
     */
    private SysLog sysLog;

    /**
     * 机构操作日志
     */
    private OrgLog orgLog;

    public static class SysLog {

        /**
         * 是否启用
         */
        private Boolean enabled = true;

        /**
         * 日志存储类型
         */
        private OptLogType type = OptLogType.DB;

    }

    public static class OrgLog {

        /**
         * 是否启用
         */
        private Boolean enabled = true;

        /**
         * 日志存储类型
         */
        private OptLogType type = OptLogType.DB;

    }
}
