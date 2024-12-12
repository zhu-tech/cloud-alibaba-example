package com.neyogoo.example.biz.excel.util;

import cn.hutool.core.collection.CollectionUtil;
import com.neyogoo.example.common.core.util.ArgumentAssert;
import com.neyogoo.example.common.core.util.JsonUtils;
import com.neyogoo.example.common.core.util.ValidatorUtils;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@UtilityClass
public class ExcelUtils {

    /**
     * 验证 Excel 数据
     */
    public static <T> void validateRowData(List<T> rows) {
        List<String> allErrors = new ArrayList<>();
        List<String> rowErrors;
        for (int i = 0, l = rows.size(); i < l; i++) {
            rowErrors = ValidatorUtils.validateEntity(rows.get(i));
            if (CollectionUtil.isNotEmpty(rowErrors)) {
                log.info("row = {}", JsonUtils.toJson(rows.get(i)));
                allErrors.add(String.format("第%s行数据有误：%s", i, StringUtils.join(rowErrors, "，")));
            }
        }
        ArgumentAssert.isTrue(CollectionUtil.isEmpty(allErrors), StringUtils.join(allErrors, "；"));
    }
}
