package com.neyogoo.example.admin.sys.vo.response.permission;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.neyogoo.example.admin.sys.model.Menu;
import com.neyogoo.example.common.core.constant.BasicConstant;
import com.neyogoo.example.common.core.util.EntityUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Data
@ApiModel(description = "菜单信息")
public class MenuTreeRespVO {

    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("菜单编码")
    private String menuCode;

    @ApiModelProperty("菜单名称")
    private String menuName;

    @ApiModelProperty("菜单图标")
    private String menuIcon;

    @ApiModelProperty("菜单路径")
    private String menuHref;

    @ApiModelProperty("直接上级菜单id")
    private Long parentId;

    @ApiModelProperty("排序")
    private Integer sort;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("是否展示")
    private Boolean showFlag;

    @ApiModelProperty("是否启用")
    private Boolean usableFlag;

    private List<MenuTreeRespVO> children = new ArrayList<>();

    public static MenuTreeRespVO fromModel(Menu model) {
        return Optional.ofNullable(BeanUtil.toBean(model, MenuTreeRespVO.class)).orElse(null);
    }

    public static List<MenuTreeRespVO> fromModels(List<Menu> models) {
        if (CollectionUtil.isEmpty(models)) {
            return Collections.emptyList();
        }
        List<MenuTreeRespVO> respVOs = models.stream().map(MenuTreeRespVO::fromModel).collect(Collectors.toList());
        Map<Long, MenuTreeRespVO> respMap = EntityUtils.toMap(respVOs, MenuTreeRespVO::getId, Function.identity());
        for (MenuTreeRespVO respVO : respVOs) {
            if (BasicConstant.DEFAULT_ID.equals(respVO.getParentId())) {
                continue;
            }
            MenuTreeRespVO parent = respMap.get(respVO.getParentId());
            if (parent != null) {
                parent.getChildren().add(respVO);
            }
        }
        return respVOs.stream()
                .filter(o -> BasicConstant.DEFAULT_ID.equals(o.getParentId()))
                .collect(Collectors.toList());
    }
}
