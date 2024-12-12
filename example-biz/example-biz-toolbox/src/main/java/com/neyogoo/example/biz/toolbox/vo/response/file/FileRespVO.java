package com.neyogoo.example.biz.toolbox.vo.response.file;

import cn.hutool.core.bean.BeanUtil;
import com.neyogoo.example.biz.toolbox.model.File;
import com.neyogoo.example.common.core.util.EntityUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Collection;
import java.util.List;

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

    @ApiModelProperty("压缩文件地址（例如缩略图）")
    private String compressUrl;

    public static FileRespVO fromModel(File model) {
        return BeanUtil.toBean(model, FileRespVO.class);
    }

    public static List<FileRespVO> fromModels(Collection<File> models) {
        return EntityUtils.toBeanList(models, FileRespVO.class);
    }
}
