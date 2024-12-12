package com.neyogoo.example.biz.toolbox.vo.response.area;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.neyogoo.example.biz.toolbox.model.Area;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Data
@ApiModel(description = "地区信息")
public class AreaRespVO {

    @ApiModelProperty("地区id")
    private Long id;

    @ApiModelProperty("上级id")
    private Long parentId;

    @ApiModelProperty("地区编码")
    private String areaCode;

    @ApiModelProperty("地区名称")
    private String areaName;

    @ApiModelProperty("地区全名")
    private String fullName;

    @ApiModelProperty("拼音首字母")
    private String pinyinHeader;

    @ApiModelProperty("拼音缩写")
    private String abbreviation;

    public static AreaRespVO fromModel(Area model) {
        if (model == null) {
            return null;
        }
        return BeanUtil.toBean(model, AreaRespVO.class);
    }

    public static List<AreaRespVO> fromModels(List<Area> models) {
        if (CollectionUtil.isEmpty(models)) {
            return Collections.emptyList();
        }
        return models.stream().map(AreaRespVO::fromModel).collect(Collectors.toList());
    }
}
