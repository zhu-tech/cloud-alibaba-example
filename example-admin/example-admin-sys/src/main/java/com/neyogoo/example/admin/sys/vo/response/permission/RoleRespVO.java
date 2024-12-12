package com.neyogoo.example.admin.sys.vo.response.permission;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.neyogoo.example.admin.sys.model.Role;
import com.neyogoo.example.biz.common.constant.BizRoleConstant;
import com.neyogoo.example.common.core.enumeration.DataScopeTypeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Data
@ApiModel(description = "角色信息")
public class RoleRespVO {

    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("角色编码")
    private String roleCode;

    @ApiModelProperty("角色名称")
    private String roleName;

    @ApiModelProperty("数据权限类型（枚举：DataScopeType）")
    private DataScopeTypeEnum dataScopeType;

    @ApiModelProperty("排序")
    private Integer sort;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("是否启用")
    private Boolean usableFlag;

    @ApiModelProperty("是否内置")
    private Boolean buildInFlag;

    public static RoleRespVO fromModel(Role model) {
        if (model == null) {
            return null;
        }
        RoleRespVO respVO = BeanUtil.toBean(model, RoleRespVO.class);
        respVO.setBuildInFlag(BizRoleConstant.BUILD_IN_ROLE_CODES.contains(respVO.roleCode));
        return respVO;
    }

    public static List<RoleRespVO> fromModels(List<Role> models) {
        if (CollUtil.isEmpty(models)) {
            return Collections.emptyList();
        }
        return models.stream().map(RoleRespVO::fromModel).collect(Collectors.toList());
    }
}
