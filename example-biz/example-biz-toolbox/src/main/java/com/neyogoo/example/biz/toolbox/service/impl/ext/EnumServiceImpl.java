package com.neyogoo.example.biz.toolbox.service.impl.ext;

import cn.hutool.core.collection.CollectionUtil;
import com.google.common.collect.Maps;
import com.neyogoo.example.biz.common.enumeration.sys.GenderEnum;
import com.neyogoo.example.biz.common.enumeration.sys.OrgCategoryEnum;
import com.neyogoo.example.biz.common.enumeration.sys.OrgLevelEnum;
import com.neyogoo.example.biz.common.enumeration.sys.PermissionTypeEnum;
import com.neyogoo.example.biz.common.enumeration.toolbox.DictTypeEnum;
import com.neyogoo.example.biz.toolbox.service.ext.EnumService;
import com.neyogoo.example.common.core.enumeration.BaseEnum;
import com.neyogoo.example.common.core.enumeration.DataScopeTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 枚举
 */
@Slf4j
@Service
public class EnumServiceImpl implements EnumService {

    // 第一层key：枚举类名，第二层key：具体枚举值
    private static final Map<String, Map<String, String>> ENUM_VALUE_MAP = Maps.newHashMapWithExpectedSize(32);

    static {
        // common
        ENUM_VALUE_MAP.put(formatName(DataScopeTypeEnum.class.getSimpleName()),
                BaseEnum.getMap(DataScopeTypeEnum.values()));

        // sys
        ENUM_VALUE_MAP.put(formatName(GenderEnum.class.getSimpleName()),
                BaseEnum.getMap(GenderEnum.values()));
        ENUM_VALUE_MAP.put(formatName(OrgCategoryEnum.class.getSimpleName()),
                BaseEnum.getMap(OrgCategoryEnum.values()));
        ENUM_VALUE_MAP.put(formatName(OrgLevelEnum.class.getSimpleName()),
                BaseEnum.getMap(OrgLevelEnum.values()));
        ENUM_VALUE_MAP.put(formatName(PermissionTypeEnum.class.getSimpleName()),
                BaseEnum.getMap(PermissionTypeEnum.values()));

        // toolbox
        ENUM_VALUE_MAP.put(formatName(DictTypeEnum.class.getSimpleName()),
                BaseEnum.getMap(DictTypeEnum.values()));
        ENUM_VALUE_MAP.put(formatName(DictTypeEnum.class.getSimpleName()),
                BaseEnum.getMap(DictTypeEnum.values()));
    }

    /**
     * 获取当前系统枚举所有名称
     */
    @Override
    public List<String> enumNames() {
        return new ArrayList<>(ENUM_VALUE_MAP.keySet());
    }

    /**
     * 获取当前系统指定枚举
     *
     * @param enumNames 枚举类名称
     */
    @Override
    public Map<String, Map<String, String>> enumsValue(List<String> enumNames) {
        if (CollectionUtil.isEmpty(enumNames)) {
            return ENUM_VALUE_MAP;
        }
        Map<String, Map<String, String>> map = new HashMap<>(enumNames.size());
        for (String code : enumNames) {
            if (ENUM_VALUE_MAP.containsKey(code)) {
                map.put(code, ENUM_VALUE_MAP.get(code));
            }
        }
        return map;
    }

    private static String formatName(String s) {
        if (s.endsWith("Enum")) {
            s = s.substring(0, s.length() - 4);
        }
        return s;
    }
}
