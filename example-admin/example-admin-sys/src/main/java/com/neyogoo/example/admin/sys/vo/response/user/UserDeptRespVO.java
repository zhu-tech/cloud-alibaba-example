package com.neyogoo.example.admin.sys.vo.response.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@NoArgsConstructor
@ApiModel(description = "人员部门信息")
public class UserDeptRespVO {

    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("工号")
    private String userCode;

    @ApiModelProperty("姓名")
    private String userName;

    @ApiModelProperty("部门id")
    private Long deptId;

}
