package com.neyogoo.example.common.xss.converter;

import cn.hutool.core.text.CharSequenceUtil;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.google.common.collect.ImmutableList;
import com.neyogoo.example.common.xss.util.XssUtils;

import java.io.IOException;
import java.util.List;

/**
 * 过滤跨站脚本的 反序列化工具
 */
public class XssStringJsonDeserializer extends JsonDeserializer<String> {

    private static final List<String> LIST = ImmutableList.of(
            "<script>", "</script>", "<iframe>", "</iframe>", "<noscript>", "</noscript>",
            "<frameset>", "</frameset>", "<frame>", "</frame>", "<noframes>", "</noframes>",
            "<embed>", "</embed>", "<object>", "</object>", "<meta>", "</meta>", "<link>", "</link>"
    );

    @Override
    public String deserialize(JsonParser p, DeserializationContext dc) throws IOException {
        if (!p.hasToken(JsonToken.VALUE_STRING)) {
            return null;
        }
        String value = p.getValueAsString();
        if (CharSequenceUtil.isEmpty(value)) {
            return value;
        }
        if (LIST.stream().anyMatch(value::contains)) {
            return XssUtils.xssClean(value, null);
        }
        return value;
    }
}
