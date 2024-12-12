package com.neyogoo.example.biz.toolbox.vo.response.dict;

import cn.hutool.core.bean.BeanUtil;
import com.neyogoo.example.biz.common.enumeration.toolbox.DictTypeEnum;
import com.neyogoo.example.biz.toolbox.model.Dict;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
@ApiModel(description = "字典信息")
public class DictRespVO {

    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("字典类型（枚举：DictType）")
    private DictTypeEnum dictType;

    @ApiModelProperty("类型名称")
    private String typeName;

    @ApiModelProperty("字典编码")
    private String dictCode;

    @ApiModelProperty("字典名称")
    private String dictName;

    @ApiModelProperty("排序")
    private Integer sort;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("是否只读")
    private Boolean readonlyFlag;

    @ApiModelProperty("是否可用")
    private Boolean usableFlag;

    public static DictRespVO fromModel(Dict model) {
        return BeanUtil.toBean(model, DictRespVO.class);
    }

    public static List<DictRespVO> fromModels(List<Dict> models) {
        return models.stream().map(DictRespVO::fromModel).collect(Collectors.toList());
    }
}
