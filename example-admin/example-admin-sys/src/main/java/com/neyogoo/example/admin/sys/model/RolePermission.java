package com.neyogoo.example.admin.sys.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.neyogoo.example.biz.common.enumeration.sys.PermissionTypeEnum;
import com.neyogoo.example.common.database.model.SavedEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 角色关联权限
 */
@Getter
@Setter
@SuperBuilder
@Accessors(chain = true)
@ToString(callSuper = true)
@NoArgsConstructor
@TableName("r_role_permission")
public class RolePermission extends SavedEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 角色id
     */
    private Long roleId;

    /**
     * 菜单/资源
     */
    private PermissionTypeEnum permissionType;

    /**
     * 菜单/资源id
     */
    private Long permissionId;

    public static List<Long> fetchPermissionIds(Collection<RolePermission> list, PermissionTypeEnum type) {
        return list.stream()
                .filter(o -> o.getPermissionType() == type)
                .map(RolePermission::getPermissionId)
                .collect(Collectors.toList());
    }
}
