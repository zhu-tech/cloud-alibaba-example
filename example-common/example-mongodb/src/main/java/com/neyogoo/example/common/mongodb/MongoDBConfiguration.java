package com.neyogoo.example.common.mongodb;

import com.mongodb.client.MongoClient;
import com.neyogoo.example.common.mongodb.convert.ReadingWritingConverter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.convert.DbRefResolver;
import org.springframework.data.mongodb.core.convert.DefaultDbRefResolver;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.neyogoo.example.common.core.constant.BasicConstant.REPOSITORY_SCAN_LOCATION;

@ConditionalOnClass(MongoClient.class)
@EnableMongoRepositories(basePackages = {REPOSITORY_SCAN_LOCATION})
public abstract class MongoDBConfiguration {

    /**
     * Mongo 转换配置.
     */
    @Bean
    public MongoCustomConversions buildMongoCustomConversions(ApplicationContext context) {
        List<Converter> converters = new ArrayList<>(buildJavaBeanConverters());
        Map<String, Converter> converterMap = context.getBeansOfType(Converter.class);
        converters.addAll(converterMap.values());
        return new MongoCustomConversions(converters);
    }

    /**
     * 转换器.
     */
    @Bean
    public MappingMongoConverter buildMappingMongoConverter(
            MongoDatabaseFactory mongoDbFactory, MongoMappingContext mongoMappingContext,
            MongoCustomConversions customConversions) {
        // 关闭自动创建索引
        mongoMappingContext.setAutoIndexCreation(false);
        DbRefResolver dbRefResolver = new DefaultDbRefResolver(mongoDbFactory);
        MappingMongoConverter converter = new MappingMongoConverter(dbRefResolver, mongoMappingContext);
        converter.setCustomConversions(customConversions);
        converter.afterPropertiesSet();
        return converter;
    }

    protected List<Converter> buildJavaBeanConverters() {
        List<Converter> list = new ArrayList<>();
        list.add(new ReadingWritingConverter.StringToLocalDateConverter());
        list.add(new ReadingWritingConverter.LocalDateToStringConverter());
        list.add(new ReadingWritingConverter.StringToLocalTimeConverter());
        list.add(new ReadingWritingConverter.LocalTimeToStringConverter());
        list.add(new ReadingWritingConverter.StringToLocalDateTimeConverter());
        list.add(new ReadingWritingConverter.LocalDateTimeToStringConverter());
        list.add(new ReadingWritingConverter.StringToYearMonthReadingConverter());
        list.add(new ReadingWritingConverter.YearMonthToStringWritingConverter());
        list.add(new ReadingWritingConverter.StringToStringReadingConverter());
        list.add(new ReadingWritingConverter.StringToStringWritingConverter());
        return list;
    }
}
