package com.neyogoo.example.admin.sys.vo.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@NoArgsConstructor
@ApiModel(description = "管理员信息")
public class AdminUserRespVO {

    @ApiModelProperty("用户id")
    private Long id;

    @ApiModelProperty("用户手机号")
    private String userMobile;

    @ApiModelProperty("用户名称")
    private String userName;

}
