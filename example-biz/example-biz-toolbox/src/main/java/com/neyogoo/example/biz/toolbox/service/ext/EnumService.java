package com.neyogoo.example.biz.toolbox.service.ext;

import java.util.List;
import java.util.Map;

/**
 * 枚举
 */
public interface EnumService {

    /**
     * 获取当前系统枚举所有名称
     */
    List<String> enumNames();

    /**
     * 获取当前系统指定枚举
     *
     * @param enumNames 枚举类名称
     */
    Map<String, Map<String, String>> enumsValue(List<String> enumNames);
}
