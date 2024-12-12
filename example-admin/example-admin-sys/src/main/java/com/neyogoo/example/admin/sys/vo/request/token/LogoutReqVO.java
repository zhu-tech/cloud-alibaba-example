package com.neyogoo.example.admin.sys.vo.request.token;

import com.neyogoo.example.common.token.enumeration.UserTypeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "登出请求入参")
public class LogoutReqVO {

    @ApiModelProperty("用户类型（枚举：UserType）")
    private UserTypeEnum userType;

    @ApiModelProperty("用户id")
    private Long userId;

    @ApiModelProperty("登录Token")
    private String loginToken;

    @ApiModelProperty("刷新Token")
    private String refreshToken;
}
