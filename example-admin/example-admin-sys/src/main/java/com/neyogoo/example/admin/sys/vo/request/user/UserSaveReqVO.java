package com.neyogoo.example.admin.sys.vo.request.user;

import cn.hutool.core.bean.BeanUtil;
import com.neyogoo.example.admin.sys.config.properties.LoginProperties;
import com.neyogoo.example.admin.sys.model.User;
import com.neyogoo.example.common.core.context.ContextUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
@ApiModel(description = "账号新增入参")
public class UserSaveReqVO extends UserEditReqVO {

    @ApiModelProperty("工号")
    @NotBlank(message = "工号不能为空")
    @Pattern(regexp = "^[0-9]{4}$", message = "工号格式不正确")
    private String userCode;

    /**
     * 转为数据库对象
     */
    public User toModel() {
        Long userId = ContextUtils.getUserId();
        LocalDateTime time = LocalDateTime.now();
        User user = BeanUtil.toBean(this, User.class);
        user.setLoginSalt(LoginProperties.generalSalt());
        user.setUsableFlag(true).setDeleteFlag(false);
        user.setCreateOrgId(ContextUtils.getOrgId());
        user.setCreateUserName(ContextUtils.getUserName());
        user.setCreateUserId(userId).setCreateTime(time);
        user.setUpdateUserId(userId).setUpdateTime(time);
        return user;
    }
}
