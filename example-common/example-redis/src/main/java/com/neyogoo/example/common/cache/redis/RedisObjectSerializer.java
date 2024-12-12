package com.neyogoo.example.common.cache.redis;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import com.neyogoo.example.common.core.util.JsonUtils;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;

public class RedisObjectSerializer extends Jackson2JsonRedisSerializer<Object> {

    public RedisObjectSerializer() {
        super(Object.class);
        ObjectMapper objectMapper = JsonUtils.newInstance();
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        objectMapper.activateDefaultTyping(LaissezFaireSubTypeValidator.instance,
                ObjectMapper.DefaultTyping.NON_FINAL,
                JsonTypeInfo.As.WRAPPER_ARRAY);
        this.setObjectMapper(objectMapper);
    }
}
