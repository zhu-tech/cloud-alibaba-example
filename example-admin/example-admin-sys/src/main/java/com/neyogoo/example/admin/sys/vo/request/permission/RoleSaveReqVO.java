package com.neyogoo.example.admin.sys.vo.request.permission;

import cn.hutool.core.bean.BeanUtil;
import com.neyogoo.example.admin.sys.model.Role;
import com.neyogoo.example.common.core.context.ContextUtils;
import com.neyogoo.example.common.core.enumeration.DataScopeTypeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;

@Data
@ApiModel(description = "角色新增入参")
public class RoleSaveReqVO {

    @ApiModelProperty("角色编码")
    @NotBlank(message = "角色编码不能为空")
    @Length(max = 8, message = "角色编码长度不能超过8位")
    @Pattern(regexp = "^[0-9a-zA-Z]{1,8}$", message = "角色编码格式错误，请输入1至8位的数字字母组合")
    private String roleCode;

    @ApiModelProperty("角色名称")
    @NotBlank(message = "角色名称不能为空")
    @Length(max = 20, message = "角色名称长度不能超过20位")
    private String roleName;

    @ApiModelProperty("数据权限类型（枚举：DataScopeType）")
    @NotNull(message = "数据权限类型不能为空")
    private DataScopeTypeEnum dataScopeType;

    @ApiModelProperty("排序")
    @NotNull(message = "排序不能为空")
    private Integer sort;

    @ApiModelProperty("备注")
    @Length(max = 50, message = "备注长度不能超过50字")
    private String remark;

    /**
     * 转为数据库对象
     */
    public Role toModel() {
        Role model = BeanUtil.toBean(this, Role.class);
        Long userId = ContextUtils.getUserId();
        LocalDateTime time = LocalDateTime.now();

        model.setUsableFlag(true)
                .setDeleteFlag(false)
                .setUpdateUserId(userId)
                .setUpdateTime(time)
                .setCreateUserId(userId)
                .setCreateTime(time);

        if (StringUtils.isBlank(remark)) {
            model.setRemark("");
        }

        return model;
    }
}
