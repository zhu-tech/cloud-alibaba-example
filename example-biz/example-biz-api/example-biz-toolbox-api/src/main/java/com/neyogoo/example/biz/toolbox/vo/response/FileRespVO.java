package com.neyogoo.example.biz.toolbox.vo.response;

import com.neyogoo.example.biz.common.model.FileIdUrl;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 文件
 */
@Data
@ApiModel(description = "文件")
public class FileRespVO {

    @ApiModelProperty("文件主键")
    private Long id;

    @ApiModelProperty("上传时名称")
    private String uploadName;

    @ApiModelProperty("文件后缀，没有.")
    private String fileSuffix;

    @ApiModelProperty("原文件地址")
    private String originalUrl;

    public FileIdUrl toFileIdUrl() {
        return new FileIdUrl(id, originalUrl);
    }
}
