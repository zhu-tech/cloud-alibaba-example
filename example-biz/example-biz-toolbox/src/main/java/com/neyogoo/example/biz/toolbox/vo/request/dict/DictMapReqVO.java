package com.neyogoo.example.biz.toolbox.vo.request.dict;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel(description = "字典入参")
public class DictMapReqVO {

    @ApiModelProperty("字典编码列表（枚举：DictType，字符串形式）")
    private List<String> dictTypes;

    @ApiModelProperty("是否可用")
    private Boolean usableFlag;
}
