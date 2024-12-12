package com.neyogoo.example.admin.sys.vo.request.user;

import cn.hutool.core.bean.BeanUtil;
import com.neyogoo.example.admin.sys.model.User;
import com.neyogoo.example.common.core.context.ContextUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@ApiModel(description = "账号修改入参")
public class UserUpdateReqVO extends UserEditReqVO {

    @ApiModelProperty("用户id")
    @NotNull(message = "用户id不能为空")
    private Long id;

    /**
     * 转换成数据库对象
     */
    public void toModel(User model) {
        BeanUtil.copyProperties(this, model);
        model.setUpdateUserId(ContextUtils.getUserId()).setUpdateTime(LocalDateTime.now());
    }
}
