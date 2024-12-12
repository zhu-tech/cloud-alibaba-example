package com.neyogoo.example.biz.excel.converter;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.converters.ReadConverterContext;
import com.alibaba.excel.converters.WriteConverterContext;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.neyogoo.example.common.core.util.StrPool;
import org.apache.commons.lang3.StringUtils;

/**
 * true/false <-> 是/否
 */
public class BooleanStringConverter implements Converter<Boolean> {

    @Override
    public Class<?> supportJavaTypeKey() {
        return Boolean.class;
    }

    @Override
    public CellDataTypeEnum supportExcelTypeKey() {
        return CellDataTypeEnum.STRING;
    }

    @Override
    public Boolean convertToJavaData(ReadConverterContext<?> context) {
        String value = context.getReadCellData().getStringValue();
        if (StringUtils.isBlank(value)) {
            return null;
        }
        if (StrPool.YES_CH.equals(value)) {
            return true;
        }
        if (StrPool.NO_CH.equals(value)) {
            return false;
        }
        return null;
    }

    @Override
    public WriteCellData<?> convertToExcelData(WriteConverterContext<Boolean> context) {
        if (context.getValue() == null) {
            return new WriteCellData<>("");
        }
        return new WriteCellData<>(context.getValue() ? StrPool.YES_CH : StrPool.NO_CH);
    }
}
