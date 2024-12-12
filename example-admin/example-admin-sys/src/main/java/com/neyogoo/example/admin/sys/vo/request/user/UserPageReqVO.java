package com.neyogoo.example.admin.sys.vo.request.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.neyogoo.example.common.core.model.DataScope;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
@NoArgsConstructor
@ApiModel(description = "账号分页入参")
public class UserPageReqVO {

    @ApiModelProperty("姓名/手机号/工号")
    private String keyword;

    @ApiModelProperty("所属机构")
    private List<Long> orgIds;

    @ApiModelProperty("是否启用")
    private Boolean usableFlag;

    @ApiModelProperty(value = "数据权限", hidden = true)
    @JsonIgnore
    private DataScope dataScope;
}
