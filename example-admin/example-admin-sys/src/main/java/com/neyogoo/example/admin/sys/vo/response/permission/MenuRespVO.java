package com.neyogoo.example.admin.sys.vo.response.permission;

import cn.hutool.core.bean.BeanUtil;
import com.neyogoo.example.admin.sys.model.Menu;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Optional;

@Data
@ApiModel(description = "菜单信息")
public class MenuRespVO {

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

    public static MenuRespVO fromModel(Menu model) {
        return Optional.ofNullable(BeanUtil.toBean(model, MenuRespVO.class)).orElse(null);
    }
}
