package com.neyogoo.example.admin.sys.vo.response.user;

import com.neyogoo.example.admin.sys.model.User;
import com.neyogoo.example.common.core.util.EntityUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
@NoArgsConstructor
@ApiModel(description = "人员简易信息")
public class UserSimpleRespVO {

    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("工号")
    private String userCode;

    @ApiModelProperty("姓名")
    private String userName;

    @ApiModelProperty("手机号")
    private String userMobile;

    /**
     * 从数据库对象转换
     */
    public static List<UserSimpleRespVO> fromModels(List<User> models) {
        return EntityUtils.toBeanList(models, UserSimpleRespVO.class);
    }
}
