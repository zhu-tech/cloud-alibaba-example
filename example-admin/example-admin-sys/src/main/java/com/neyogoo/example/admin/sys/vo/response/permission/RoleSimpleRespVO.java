package com.neyogoo.example.admin.sys.vo.response.permission;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.neyogoo.example.admin.sys.model.Role;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Data
@ApiModel(description = "角色信息")
public class RoleSimpleRespVO {

    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("角色名称")
    private String roleName;

    public static RoleSimpleRespVO fromModel(Role model) {
        return Optional.ofNullable(BeanUtil.toBean(model, RoleSimpleRespVO.class)).orElse(null);
    }

    public static List<RoleSimpleRespVO> fromModels(List<Role> models) {
        if (CollectionUtil.isEmpty(models)) {
            return Collections.emptyList();
        }
        return models.stream().map(RoleSimpleRespVO::fromModel).collect(Collectors.toList());
    }
}
