package com.neyogoo.example.biz.toolbox.constant;

import com.neyogoo.example.biz.common.constant.BizCacheConstant;

public interface ToolboxCacheConstant {

    /**
     * biz:toolbox:file:original_url:{fileId} -> 源文件地址
     */
    String FILE_ORIGINAL_URL = BizCacheConstant.TOOLBOX_MODULAR_PREFIX
            + "file:original_url:";

    /**
     * biz:toolbox:dict:{dictType} -> 字典列表
     */
    String DICT_TYPE_LIST = BizCacheConstant.TOOLBOX_MODULAR_PREFIX + "dict:";
}
