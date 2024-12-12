package com.neyogoo.example.admin.sys.vo.request.permission;

import cn.hutool.core.bean.BeanUtil;
import com.neyogoo.example.admin.sys.model.Menu;
import com.neyogoo.example.common.core.context.ContextUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;

@Data
@ApiModel(description = "菜单新增入参")
public class MenuSaveReqVO {

    @ApiModelProperty("菜单编码")
    @NotBlank(message = "菜单编码不能为空")
    @Length(max = 20, message = "菜单编码长度不能超过16位")
    @Pattern(regexp = "^[0-9a-zA-Z:_]{1,20}$", message = "菜单编码格式错误，请输入1至20位的数字字母冒号下划线组合")
    private String menuCode;

    @ApiModelProperty("菜单名称")
    @NotBlank(message = "菜单名称不能为空")
    @Length(max = 20, message = "菜单名称长度不能超过20位")
    private String menuName;

    @ApiModelProperty("菜单图标")
    @Length(max = 60, message = "菜单图标长度不能超过60位")
    private String menuIcon;

    @ApiModelProperty("菜单路径")
    @NotBlank(message = "菜单路径不能为空")
    private String menuHref;

    @ApiModelProperty("直接上级菜单id")
    @NotNull(message = "直接上级菜单id不能为空")
    private Long parentId;

    @ApiModelProperty("排序")
    @NotNull(message = "排序不能为空")
    private Integer sort;

    @ApiModelProperty("备注")
    @Length(max = 50, message = "备注长度不能超过50字")
    private String remark;

    @ApiModelProperty("是否展示")
    @NotNull(message = "是否展示不能为空")
    private Boolean showFlag;

    public Menu toModel() {
        Menu model = BeanUtil.toBean(this, Menu.class);
        Long userId = ContextUtils.getUserId();
        LocalDateTime time = LocalDateTime.now();

        model.setUsableFlag(true)
                .setDeleteFlag(false)
                .setUpdateUserId(userId)
                .setUpdateTime(time)
                .setCreateUserId(userId)
                .setCreateTime(time);

        return model;
    }
}
