package com.neyogoo.example.admin.sys.vo.request.token;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.neyogoo.example.common.token.model.RefreshTokenCache;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Pattern;

@Data
@ApiModel(description = "刷新Token请求入参")
public class RefreshTokenReqVO {

    @ApiModelProperty("置换Token")
    @Pattern(regexp = "^[0-9a-z]{32}$", message = "置换Token格式不正确")
    private String loginToken;

    @ApiModelProperty("刷新Token")
    @Pattern(regexp = "^[0-9a-z]{32}$", message = "刷新Token格式不正确")
    private String refreshToken;

    @ApiModelProperty("机构id")
    private Long orgId;

    /**
     * 根据刷新Token查询出的缓存
     */
    @JsonIgnore
    @ApiModelProperty(hidden = true)
    private RefreshTokenCache cache;
}
