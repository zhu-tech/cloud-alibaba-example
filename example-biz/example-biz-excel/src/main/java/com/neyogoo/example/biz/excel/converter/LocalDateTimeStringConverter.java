package com.neyogoo.example.biz.excel.converter;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.converters.ReadConverterContext;
import com.alibaba.excel.converters.WriteConverterContext;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.neyogoo.example.common.core.util.DateUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 时间 <-> 字符串（yyyy-MM-dd HH:mm:ss）
 */
public class LocalDateTimeStringConverter implements Converter<LocalDateTime> {

    @Override
    public Class<?> supportJavaTypeKey() {
        return LocalDateTime.class;
    }

    @Override
    public CellDataTypeEnum supportExcelTypeKey() {
        return CellDataTypeEnum.STRING;
    }

    @Override
    public LocalDateTime convertToJavaData(ReadConverterContext<?> context) {
        return LocalDateTime.parse(context.getReadCellData().getStringValue(),
                DateTimeFormatter.ofPattern(DateUtils.DEFAULT_DATE_TIME_FORMAT));
    }

    @Override
    public WriteCellData<?> convertToExcelData(WriteConverterContext<LocalDateTime> context) {
        return new WriteCellData<>(DateTimeFormatter.ofPattern(DateUtils.DEFAULT_DATE_TIME_FORMAT)
                .format(context.getValue()));
    }
}
