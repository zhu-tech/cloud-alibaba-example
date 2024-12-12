package com.neyogoo.example.biz.toolbox.vo.response;

import com.neyogoo.example.biz.common.enumeration.toolbox.DictTypeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "字典信息")
public class DictRespVO {

    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("字典类型（枚举：DictType）")
    private DictTypeEnum dictType;

    @ApiModelProperty("字典编码")
    private String dictCode;

    @ApiModelProperty("字典名称")
    private String dictName;

    @ApiModelProperty("排序")
    private Integer sort;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("是否可用")
    private Boolean usableFlag;
}
