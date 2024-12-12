package com.neyogoo.example.admin.sys.vo.response.permission;

import com.neyogoo.example.admin.sys.model.Role;
import com.neyogoo.example.common.core.enumeration.DataScopeTypeEnum;
import com.neyogoo.example.common.core.util.EntityUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
@NoArgsConstructor
@ApiModel(description = "角色信息（租户视角）")
public class RoleOrgViewRespVO {

    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("角色编码")
    private String roleCode;

    @ApiModelProperty("角色名称")
    private String roleName;

    @ApiModelProperty("数据权限类型（枚举：DataScopeType）")
    private DataScopeTypeEnum dataScopeType;

    @ApiModelProperty("角色备注")
    private String remark;

    @ApiModelProperty("是否启用")
    private Boolean usableFlag;

    /**
     * 从数据库对象转换
     */
    public static List<RoleOrgViewRespVO> fromModels(List<Role> modesl) {
        return EntityUtils.toBeanList(modesl, RoleOrgViewRespVO.class);
    }
}
