package com.neyogoo.example.biz.common.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.neyogoo.example.common.core.model.DataScope;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DataScopeParam {

    @ApiModelProperty(value = "数据权限", hidden = true)
    @JsonIgnore
    protected DataScope dataScope;
}
