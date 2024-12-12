package com.neyogoo.example.common.core.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.BeanSerializerModifier;
import com.fasterxml.jackson.databind.type.MapType;
import com.neyogoo.example.common.core.annotation.SensitiveProperty;
import com.neyogoo.example.common.core.util.StrHelper;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SensitiveFieldModule extends SimpleModule {

    @Override
    public void setupModule(SetupContext context) {
        context.addBeanSerializerModifier(new SensitiveFieldModifier());
    }

    public static class SensitiveFieldModifier extends BeanSerializerModifier {

        @Override
        public List<BeanPropertyWriter> changeProperties(SerializationConfig config, BeanDescription beanDesc,
                                                         List<BeanPropertyWriter> beanProperties) {
            List<BeanPropertyWriter> newWriters = new ArrayList<>();
            for (BeanPropertyWriter writer : beanProperties) {
                if (writer.getAnnotation(SensitiveProperty.class) != null
                        && writer.getType().isTypeOrSubTypeOf(String.class)) {
                    // 如果带有 @SensitiveProperty 注解，并且是字符串，则使用自定义处理
                    JsonSerializer<Object> serializer = new SensitiveJsonSerializer(writer.getSerializer());
                    writer.assignSerializer(serializer);
                }
                newWriters.add(writer);
            }

            return newWriters;
        }

        @Override
        public JsonSerializer<?> modifyMapSerializer(SerializationConfig config, MapType valueType,
                                                     BeanDescription beanDesc, JsonSerializer<?> serializer) {
            return super.modifyMapSerializer(config, valueType, beanDesc, serializer);
        }
    }

    public static class SensitiveJsonSerializer extends JsonSerializer<Object> {

        private final JsonSerializer<Object> serializer;

        public SensitiveJsonSerializer(JsonSerializer<Object> serializer) {
            this.serializer = serializer;
        }

        @Override
        public void serialize(Object object, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
                throws IOException {
            if (object instanceof String) {
                String str = (String) object;
                if (StringUtils.isNotBlank(str)) {
                    object = StrHelper.processSensitiveField(str);
                }
            }

            if (this.serializer == null) {
                serializerProvider.defaultSerializeValue(object, jsonGenerator);
            } else {
                this.serializer.serialize(object, jsonGenerator, serializerProvider);
            }
        }
    }
}