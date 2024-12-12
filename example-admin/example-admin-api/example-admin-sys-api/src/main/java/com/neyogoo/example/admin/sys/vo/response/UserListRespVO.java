package com.neyogoo.example.admin.sys.vo.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "用户列表返回实体类")
public class UserListRespVO {

    @ApiModelProperty("人员id")
    private Long id;

    @ApiModelProperty("人员姓名")
    private String userName;

    @ApiModelProperty("机构id")
    private Long orgId;

    @ApiModelProperty("机构名称")
    private String orgName;

    @ApiModelProperty("部门名称")
    private String deptName;

}
