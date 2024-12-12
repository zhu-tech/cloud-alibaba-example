package com.neyogoo.example.admin.sys.vo.response.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@NoArgsConstructor
@ApiModel(description = "账号分页返参")
public class UserPageRespVO {

    @ApiModelProperty("用户id")
    private Long id;

    @ApiModelProperty("工号")
    private String userCode;

    @ApiModelProperty("姓名")
    private String userName;

    @ApiModelProperty("手机号")
    private String userMobile;

    @ApiModelProperty("所属机构")
    private String orgNames;

    @ApiModelProperty("所属部门")
    private String deptNames;

    @ApiModelProperty("拥有角色")
    private String roleNames;

    @ApiModelProperty("是否启用")
    private Boolean usableFlag;
}
