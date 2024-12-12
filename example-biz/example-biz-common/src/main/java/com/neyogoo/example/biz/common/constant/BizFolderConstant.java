package com.neyogoo.example.biz.common.constant;

import org.apache.commons.lang3.StringUtils;

import java.io.File;

/**
 * 文件夹常量（可使用小写应为字母、数字、中划线、下划线）
 */
public final class BizFolderConstant {

    private static String link(String... folders) {
        return File.separator + StringUtils.join(folders, File.separator);
    }

    private BizFolderConstant() {

    }
}
