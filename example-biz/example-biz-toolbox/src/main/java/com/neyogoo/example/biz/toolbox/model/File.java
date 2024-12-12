package com.neyogoo.example.biz.toolbox.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.neyogoo.example.common.database.model.SavedEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

/**
 * 文件
 */
@Getter
@Setter
@SuperBuilder
@Accessors(chain = true)
@ToString(callSuper = true)
@NoArgsConstructor
@TableName("t_biz_file")
public class File extends SavedEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 上传时名称
     */
    private String uploadName;

    /**
     * 文件后缀，没有.
     */
    private String fileSuffix;

    /**
     * 文件大小
     */
    private Long fileSize;

    /**
     * 原文件地址
     */
    private String originalUrl;
}
