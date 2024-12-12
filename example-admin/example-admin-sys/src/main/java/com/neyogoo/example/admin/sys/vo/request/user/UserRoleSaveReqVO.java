package com.neyogoo.example.admin.sys.vo.request.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@Accessors(chain = true)
@NoArgsConstructor
@ApiModel(description = "用户分配角色入参")
public class UserRoleSaveReqVO {

    @ApiModelProperty("用户id")
    @NotNull(message = "用户账号不能为空")
    private Long id;

    @ApiModelProperty("角色id列表")
    @NotEmpty(message = "请选择角色")
    private List<Long> roleIds;

}
