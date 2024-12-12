package com.neyogoo.example.biz.excel.converter;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.converters.ReadConverterContext;
import com.alibaba.excel.converters.WriteConverterContext;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.neyogoo.example.common.core.util.DateUtils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * 日期 <-> 字符串（yyyy-MM-dd）
 */
public class LocalDateStringConverter implements Converter<LocalDate> {

    @Override
    public Class<?> supportJavaTypeKey() {
        return LocalDate.class;
    }

    @Override
    public CellDataTypeEnum supportExcelTypeKey() {
        return CellDataTypeEnum.STRING;
    }

    @Override
    public LocalDate convertToJavaData(ReadConverterContext<?> context) {
        return LocalDate.parse(context.getReadCellData().getStringValue(),
                DateTimeFormatter.ofPattern(DateUtils.DEFAULT_DATE_FORMAT));
    }

    @Override
    public WriteCellData<?> convertToExcelData(WriteConverterContext<LocalDate> context) {
        return new WriteCellData<>(
                DateTimeFormatter.ofPattern(DateUtils.DEFAULT_DATE_FORMAT).format(context.getValue())
        );
    }
}
