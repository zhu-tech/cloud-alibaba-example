package com.neyogoo.example.admin.sys.vo.request.token;

import com.neyogoo.example.common.token.enumeration.UserTypeEnum;
import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper = true)
@ApiModel(description = "移动端（家长）手机验证码登录请求入参")
public class H5SmsSendReqVO extends LoginSmsSendReqVO {

    /**
     * 用户类型
     */
    @Override
    public UserTypeEnum userType() {
        return UserTypeEnum.CstUser;
    }
}
