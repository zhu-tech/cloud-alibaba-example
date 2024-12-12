package com.neyogoo.example.biz.toolbox.model.logging;

import com.neyogoo.example.common.token.enumeration.LoginPointEnum;
import com.neyogoo.example.common.token.enumeration.LoginTypeEnum;
import com.neyogoo.example.common.token.enumeration.UserTypeEnum;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

/**
 * 系统登录日志
 */
@Getter
@Setter
@Document("t_sys_login_log")
public class SysLoginLog {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    /**
     * 用户类型
     */
    private UserTypeEnum userType;
    /**
     * 用户id
     */
    private Long userId;
    /**
     * 用户姓名
     */
    private String userName;
    /**
     * 用户账号
     */
    private String userAccount;
    /**
     * 机构id
     */
    private Long orgId;
    /**
     * 登录点
     */
    private LoginPointEnum loginPoint;
    /**
     * 登录方式
     */
    private LoginTypeEnum loginType;
    /**
     * 登录时间
     */
    private LocalDateTime loginTime;
    /**
     * 是否登录成功
     */
    private Boolean successFlag;
    /**
     * 登录描述
     */
    private String description;
    /**
     * 登录IP
     */
    private String requestIp;
    /**
     * 登录地点
     */
    private String location;
    /**
     * 浏览器请求头
     */
    private String ua;
    /**
     * 浏览器名称
     */
    private String browserName;
    /**
     * 浏览器版本
     */
    private String browserVersion;
    /**
     * 操作系统
     */
    private String operatingSystem;
    /**
     * 链路追踪id
     */
    private String traceId;
}
