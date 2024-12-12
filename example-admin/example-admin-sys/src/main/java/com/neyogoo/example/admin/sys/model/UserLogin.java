package com.neyogoo.example.admin.sys.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.neyogoo.example.common.core.context.ContextUtils;
import com.neyogoo.example.common.database.model.UpdatedEntity;
import com.neyogoo.example.common.token.enumeration.LoginPointEnum;
import com.neyogoo.example.common.token.enumeration.UserTypeEnum;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

/**
 * 用户登录相关记录汇总
 */
@Getter
@Setter
@SuperBuilder
@Accessors(chain = true)
@ToString(callSuper = true)
@NoArgsConstructor
@TableName("t_sys_user_login")
public class UserLogin extends UpdatedEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 用户类型
     */
    private UserTypeEnum userType;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 最近登录时间
     */
    private LocalDateTime latestLoginTime;

    /**
     * 最近登录ip
     */
    private String latestLoginIp;

    /**
     * 最近登录地址
     */
    private String latestLoginLocation;

    /**
     * 最近登录入口
     */
    private LoginPointEnum latestLoginPoint;

    /**
     * 最近更新密码时间
     */
    private LocalDateTime latestChangePwdTime;

    /**
     * 构造器
     */
    public UserLogin(UserTypeEnum userType, Long userId) {
        LocalDateTime now = LocalDateTime.now();
        Long loginUserId = ContextUtils.getUserId();
        if (loginUserId == null) {
            loginUserId = userId;
        }
        this.userType = userType;
        this.userId = userId;
        this.latestChangePwdTime = now;
        this.createUserId = loginUserId;
        this.createTime = now;
        this.updateUserId = loginUserId;
        this.updateTime = now;
    }
}
