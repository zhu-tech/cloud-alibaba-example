package com.neyogoo.example.admin.sys.vo.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@NoArgsConstructor
@ApiModel(description = "人员简易信息")
public class UserSimpleRespVO {

    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("工号")
    private String userCode;

    @ApiModelProperty("姓名")
    private String userName;

    @ApiModelProperty("手机号")
    private String userMobile;

}
