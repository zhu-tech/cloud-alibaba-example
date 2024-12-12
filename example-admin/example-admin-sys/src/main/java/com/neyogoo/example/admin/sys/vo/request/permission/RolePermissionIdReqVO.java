package com.neyogoo.example.admin.sys.vo.request.permission;

import com.neyogoo.example.admin.sys.model.RolePermission;
import com.neyogoo.example.biz.common.enumeration.sys.PermissionTypeEnum;
import com.neyogoo.example.common.core.context.ContextUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Accessors(chain = true)
@NoArgsConstructor
@ApiModel(description = "角色关联权限id列表")
public class RolePermissionIdReqVO {

    @ApiModelProperty("角色id")
    @NotNull(message = "角色id不能为空")
    private Long roleId;

    @ApiModelProperty("菜单id列表")
    @NotNull(message = "菜单id列表不能为空")
    private List<Long> menuIds;

    @ApiModelProperty("资源id列表")
    @NotNull(message = "资源id列表不能为空")
    private List<Long> resourceIds;

    public List<RolePermission> toPermissionList() {
        List<RolePermission> list = new ArrayList<>();
        Long userId = ContextUtils.getUserId();
        LocalDateTime time = LocalDateTime.now();
        for (Long menuId : menuIds) {
            list.add(RolePermission.builder()
                    .roleId(roleId)
                    .permissionType(PermissionTypeEnum.M)
                    .permissionId(menuId)
                    .createUserId(userId)
                    .createTime(time)
                    .build());
        }
        for (Long resourceId : resourceIds) {
            list.add(RolePermission.builder()
                    .roleId(roleId)
                    .permissionType(PermissionTypeEnum.R)
                    .permissionId(resourceId)
                    .createUserId(userId)
                    .createTime(time)
                    .build());
        }
        return list;
    }
}
