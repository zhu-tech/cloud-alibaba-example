package com.neyogoo.example.admin.sys.vo.request.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Collection;

@Data
@ApiModel(description = "人员列表请求实体类")
public class UserListReqVO {

    @ApiModelProperty("部门/人员")
    private String keyword;

    @ApiModelProperty("人员id列表")
    private Collection<Long> userIds;

}
