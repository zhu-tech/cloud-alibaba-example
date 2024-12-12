package com.neyogoo.example.biz.toolbox.vo.request.dict;

import com.neyogoo.example.biz.common.enumeration.toolbox.DictTypeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "字典分页查询入参")
public class DictPageReqVO {

    @ApiModelProperty("字典类型（枚举：DictType）")
    private DictTypeEnum dictType;
}
