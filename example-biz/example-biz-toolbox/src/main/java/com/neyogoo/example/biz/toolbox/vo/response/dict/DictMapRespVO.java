package com.neyogoo.example.biz.toolbox.vo.response.dict;

import cn.hutool.core.bean.BeanUtil;
import com.neyogoo.example.biz.toolbox.model.Dict;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "字典信息")
public class DictMapRespVO {

    @ApiModelProperty("字典编码")
    private String dictCode;

    @ApiModelProperty("字典名称")
    private String dictName;

    @ApiModelProperty("是否可用")
    private Boolean usableFlag;

    public static DictMapRespVO fromModel(Dict model) {
        return BeanUtil.toBean(model, DictMapRespVO.class);
    }
}
