package com.neyogoo.example.biz.gateway.model;

import com.neyogoo.example.common.core.enumeration.BaseEnum;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "Option", description = "下拉、多选组件选项")
public class Option implements Serializable {

    private String label;
    private String text;
    private String value;


    public static List<Option> mapOptions(BaseEnum[] values) {
        return Arrays.stream(values).map(item -> Option.builder().label(item.getDesc())
                .text(item.getDesc()).value(item.getCode()).build()).collect(Collectors.toList());
    }
}
