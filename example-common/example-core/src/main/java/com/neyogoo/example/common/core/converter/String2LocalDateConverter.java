package com.neyogoo.example.common.core.converter;

import org.springframework.core.convert.converter.Converter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.Map;

import static com.neyogoo.example.common.core.util.DateUtils.*;

public class String2LocalDateConverter extends BaseDateConverter<LocalDate> implements Converter<String, LocalDate> {

    protected static final Map<String, String> FORMAT = new LinkedHashMap(5);

    static {
        FORMAT.put(DEFAULT_DATE_FORMAT, DEFAULT_DATE_FORMAT_MATCHES);
        FORMAT.put(SLASH_DATE_FORMAT, SLASH_DATE_FORMAT_MATCHES);
        FORMAT.put(DEFAULT_DATE_FORMAT_EN, DEFAULT_DATE_FORMAT_EN_MATCHES);
    }

    @Override
    protected Map<String, String> getFormat() {
        return FORMAT;
    }

    @Override
    public LocalDate convert(String source) {
        return super.convert(source, (key) -> LocalDate.parse(source, DateTimeFormatter.ofPattern(key)));
    }
}

