package com.neyogoo.example.biz.common.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@Accessors(chain = true)
@AllArgsConstructor
public class FileIdUrl {

    @ApiModelProperty("文件id")
    private Long fileId;

    @ApiModelProperty("文件地址")
    private String fileUrl;
}