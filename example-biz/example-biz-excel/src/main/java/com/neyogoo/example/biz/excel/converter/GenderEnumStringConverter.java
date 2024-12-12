package com.neyogoo.example.biz.excel.converter;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.converters.ReadConverterContext;
import com.alibaba.excel.converters.WriteConverterContext;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.neyogoo.example.biz.common.enumeration.sys.GenderEnum;

/**
 * M/W <-> 男/女
 */
public class GenderEnumStringConverter implements Converter<GenderEnum> {

    @Override
    public Class<?> supportJavaTypeKey() {
        return GenderEnum.class;
    }

    @Override
    public CellDataTypeEnum supportExcelTypeKey() {
        return CellDataTypeEnum.STRING;
    }

    @Override
    public GenderEnum convertToJavaData(ReadConverterContext<?> context) {
        return GenderEnum.descOf(context.getReadCellData().getStringValue());
    }

    @Override
    public WriteCellData<?> convertToExcelData(WriteConverterContext<GenderEnum> context) {
        return new WriteCellData<>(context.getValue().getDesc());
    }
}
