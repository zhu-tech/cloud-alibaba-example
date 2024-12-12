package com.neyogoo.example.admin.sys.vo.response.token;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
@ToString(callSuper = true)
@ApiModel(description = "认证信息")
public class CstUserAuthInfoRespVO extends AuthInfoRespVO {

    private static final long serialVersionUID = 1L;

}
