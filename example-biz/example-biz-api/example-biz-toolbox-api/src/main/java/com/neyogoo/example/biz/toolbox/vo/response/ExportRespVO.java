package com.neyogoo.example.biz.toolbox.vo.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "导出记录")
public class ExportRespVO {

    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("文件id")
    private Long fileId;

    @ApiModelProperty("文件名")
    private String fileName;
}
