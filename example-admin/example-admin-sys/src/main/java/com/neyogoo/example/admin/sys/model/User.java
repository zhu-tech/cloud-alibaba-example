package com.neyogoo.example.admin.sys.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.neyogoo.example.biz.common.enumeration.sys.GenderEnum;
import com.neyogoo.example.common.core.exception.BizException;
import com.neyogoo.example.common.core.exception.code.ExceptionCode;
import com.neyogoo.example.common.database.model.UpdatedEntity;
import com.neyogoo.example.common.database.mybatis.typehandler.EncryptTypeHandler;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 用户
 */
@Getter
@Setter
@SuperBuilder
@Accessors(chain = true)
@ToString(callSuper = true)
@NoArgsConstructor
@TableName("t_sys_user")
public class User extends UpdatedEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 工号
     */
    private String userCode;

    /**
     * 名称
     */
    private String userName;

    /**
     * 手机号
     */
    @TableField(typeHandler = EncryptTypeHandler.class)
    private String userMobile;

    /**
     * 性别
     */
    private GenderEnum userGender;

    /**
     * 出生日期
     */
    private LocalDate userBirthday;

    /**
     * 邮箱
     */
    @TableField(typeHandler = EncryptTypeHandler.class)
    private String userEmail;

    /**
     * 政治面貌（字典：Politic）
     */
    private String politic;

    /**
     * 学历（字典：Education）
     */
    private String education;

    /**
     * 民族（字典：Nationality）
     */
    private String nationality;

    /**
     * 籍贯
     */
    private String nativePlace;

    /**
     * 登录密码
     */
    private String loginPwd;

    /**
     * 密码盐
     */
    private String loginSalt;

    /**
     * 锁定结束时间
     */
    private LocalDateTime lockEndTime;

    /**
     * 是否启用
     */
    private Boolean usableFlag;

    /**
     * 是否删除
     */
    private Boolean deleteFlag;

    /**
     * 创建机构id
     */
    private Long createOrgId;

    /**
     * 创建人姓名
     */
    private String createUserName;

    /**
     * 登录前相关检查
     */
    public static User checkBeforeLogin(User user) {
        if (user == null) {
            throw BizException.wrap(ExceptionCode.NOT_FOUND, "用户名或密码错误");
        }
        if (user.lockEndTime != null && user.lockEndTime.isBefore(LocalDateTime.now())) {
            throw BizException.wrap(ExceptionCode.FORBIDDEN, "该用户已被锁定，请稍后再试");
        }
        if (!user.usableFlag) {
            throw BizException.wrap(ExceptionCode.FORBIDDEN, "该用户已被禁用，请联系管理员");
        }
        return user;
    }

    /**
     * 修改密码前相关检查
     */
    public static User checkBeforeChangePwd(User user) {
        if (user == null) {
            throw BizException.wrap(ExceptionCode.NOT_FOUND, "该用户不存在，请联系管理员");
        }
        if (!user.usableFlag) {
            throw BizException.wrap(ExceptionCode.FORBIDDEN, "该用户已被禁用，请联系管理员");
        }
        return user;
    }
}
