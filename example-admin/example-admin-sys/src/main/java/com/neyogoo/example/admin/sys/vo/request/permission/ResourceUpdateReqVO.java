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
@ApiModel(description = "菜单修改入参")
public class ResourceUpdateReqVO {

    @ApiModelProperty("id")
    @NotNull(message = "id不能为空")
    private Long id;

    @ApiModelProperty("资源编码")
    @NotBlank(message = "资源编码不能为空")
    @Length(max = 30, message = "资源编码长度不能超过30位")
    @Pattern(regexp = "^[0-9a-zA-Z::_]{1,30}$", message = "资源编码格式错误，请输入1至30位的数字字母组合")
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

    public void toModel(Resource model) {
        if (model == null) {
            return;
        }
        BeanUtil.copyProperties(this, model);
        model.setUpdateUserId(ContextUtils.getUserId()).setUpdateTime(LocalDateTime.now());
    }
}
