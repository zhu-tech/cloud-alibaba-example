package com.neyogoo.example.admin.sys.vo.request.permission;

import cn.hutool.core.bean.BeanUtil;
import com.neyogoo.example.admin.sys.model.Resource;
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
@ApiModel(description = "资源新增入参")
public class ResourceSaveReqVO {

    @ApiModelProperty("所属菜单")
    @NotNull(message = "所属菜单不能为空")
    private Long menuId;

    @ApiModelProperty("资源编码")
    @NotBlank(message = "资源编码不能为空")
    @Length(max = 30, message = "资源编码长度不能超过30位")
    @Pattern(regexp = "^[0-9a-zA-Z:_]{1,30}$", message = "资源编码格式错误，请输入1至30位的数字字母冒号下划线组合")
    private String resourceCode;

    @ApiModelProperty("资源名称")
    @NotBlank(message = "资源名称不能为空")
    @Length(max = 20, message = "资源名称长度不能超过20位")
    private String resourceName;

    @ApiModelProperty("排序")
    @NotNull(message = "排序不能为空")
    private Integer sort;

    @ApiModelProperty("备注")
    @Length(max = 50, message = "备注长度不能超过50字")
    private String remark;

    public Resource toModel() {
        Resource model = BeanUtil.toBean(this, Resource.class);
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
