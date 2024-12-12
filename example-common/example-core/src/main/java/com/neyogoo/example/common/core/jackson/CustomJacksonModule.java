package com.neyogoo.example.common.core.jackson;

import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import com.neyogoo.example.common.core.converter.LocalDateTimeDeserializer;

import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import static com.neyogoo.example.common.core.util.DateUtils.*;


/**
 * jackson 自定义序列化 & 反序列化 规则
 */
public class CustomJacksonModule extends SimpleModule {

    public CustomJacksonModule() {
        super();
        this.addDeserializer(
                LocalDateTime.class, LocalDateTimeDeserializer.INSTANCE
        );
        this.addDeserializer(
                LocalDate.class, new LocalDateDeserializer(DateTimeFormatter.ofPattern(DEFAULT_DATE_FORMAT))
        );
        this.addDeserializer(
                LocalTime.class, new LocalTimeDeserializer(DateTimeFormatter.ofPattern(DEFAULT_TIME_FORMAT))
        );
        this.addSerializer(
                LocalDateTime.class, new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(DEFAULT_DATE_TIME_FORMAT))
        );
        this.addSerializer(
                LocalDate.class, new LocalDateSerializer(DateTimeFormatter.ofPattern(DEFAULT_DATE_FORMAT))
        );
        this.addSerializer(
                LocalTime.class, new LocalTimeSerializer(DateTimeFormatter.ofPattern(DEFAULT_TIME_FORMAT))
        );
        this.addSerializer(
                Long.class, ToStringSerializer.instance
        );
        this.addSerializer(
                Long.TYPE, ToStringSerializer.instance
        );
        this.addSerializer(
                BigInteger.class, ToStringSerializer.instance
        );
    }
}
