package com.neyogoo.example.admin.sys.vo.response.permission;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

@Data
@Accessors(chain = true)
@NoArgsConstructor
@ApiModel(description = "角色关联权限id列表")
public class RolePermissionIdRespVO {

    @ApiModelProperty("菜单id列表")
    private List<Long> menuIds = new ArrayList<>();

    @ApiModelProperty("资源id列表")
    private List<Long> resourceIds = new ArrayList<>();
}
