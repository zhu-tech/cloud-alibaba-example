package com.neyogoo.example.common.mongodb.convert;

import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.data.convert.WritingConverter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;

public class ReadingWritingConverter {

    @ReadingConverter
    public static class StringToLocalDateConverter implements Converter<String, LocalDate> {
        @Override
        public LocalDate convert(String source) {
            return LocalDate.parse(source);
        }
    }

    @WritingConverter
    public static class LocalDateToStringConverter implements Converter<LocalDate, String> {
        @Override
        public String convert(LocalDate source) {
            return source.toString();
        }
    }

    @ReadingConverter
    public static class StringToLocalTimeConverter implements Converter<String, LocalTime> {
        @Override
        public LocalTime convert(String source) {
            return LocalTime.parse(source, DateTimeFormatter.ofPattern("HH:mm:ss"));
        }
    }

    @WritingConverter
    public static class LocalTimeToStringConverter implements Converter<LocalTime, String> {
        @Override
        public String convert(LocalTime source) {
            return source.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        }
    }

    @ReadingConverter
    public static class StringToLocalDateTimeConverter implements Converter<String, LocalDateTime> {
        @Override
        public LocalDateTime convert(String source) {
            return LocalDateTime.parse(source, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        }
    }

    @WritingConverter
    public static class LocalDateTimeToStringConverter implements Converter<LocalDateTime, String> {
        @Override
        public String convert(LocalDateTime source) {
            return source.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        }
    }

    @ReadingConverter
    public static class StringToYearMonthReadingConverter implements Converter<String, YearMonth> {
        @Override
        public YearMonth convert(String source) {
            return YearMonth.parse(source);
        }
    }

    @WritingConverter
    public static class YearMonthToStringWritingConverter implements Converter<YearMonth, String> {
        @Override
        public String convert(YearMonth source) {
            return source.toString();
        }
    }

    @ReadingConverter
    public static class StringToStringReadingConverter implements Converter<String, String> {
        @Override
        public String convert(String source) {
            return source;
        }
    }

    @WritingConverter
    public static class StringToStringWritingConverter implements Converter<String, String> {
        @Override
        public String convert(String source) {
            return source;
        }
    }
}
