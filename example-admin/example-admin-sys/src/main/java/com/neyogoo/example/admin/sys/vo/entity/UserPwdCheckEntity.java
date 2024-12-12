package com.neyogoo.example.admin.sys.vo.entity;

import com.neyogoo.example.admin.sys.model.User;
import com.neyogoo.example.common.token.enumeration.UserTypeEnum;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Builder
@Accessors(chain = true)
public class UserPwdCheckEntity {

    /**
     * 用户类型
     */
    private UserTypeEnum userType;
    /**
     * 用户id
     */
    private Long userId;
    /**
     * 用户密码
     */
    private String userPwd;
    /**
     * 密码盐
     */
    private String userSalt;
    /**
     * 输入密码
     */
    private String inputPwd;

    public static UserPwdCheckEntity fromUser(User user) {
        return UserPwdCheckEntity.builder()
                .userType(UserTypeEnum.SysUser)
                .userId(user.getId())
                .userPwd(user.getLoginPwd())
                .userSalt(user.getLoginSalt())
                .build();
    }
}
