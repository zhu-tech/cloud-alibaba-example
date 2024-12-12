package com.neyogoo.example.admin.sys.vo.response.permission;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.neyogoo.example.admin.sys.model.Resource;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Data
@ApiModel(description = "资源信息")
public class ResourceRespVO {

    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("菜单id")
    private Long menuId;

    @ApiModelProperty("菜单名称")
    private String menuName;

    @ApiModelProperty("资源编码")
    private String resourceCode;

    @ApiModelProperty("资源名称")
    private String resourceName;

    @ApiModelProperty("排序")
    private Integer sort;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("是否启用")
    private Boolean usableFlag;

    public static ResourceRespVO fromModel(Resource model) {
        return Optional.ofNullable(BeanUtil.toBean(model, ResourceRespVO.class)).orElse(null);
    }

    public static List<ResourceRespVO> fromModels(List<Resource> models) {
        if (CollectionUtil.isEmpty(models)) {
            return Collections.emptyList();
        }
        return models.stream().map(ResourceRespVO::fromModel).collect(Collectors.toList());
    }
}
