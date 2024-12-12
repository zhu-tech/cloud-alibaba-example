package com.fasterxml.jackson.databind.deser.std;

import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.cfg.CoercionAction;
import com.fasterxml.jackson.databind.cfg.CoercionInputShape;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.type.LogicalType;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.CompactStringObjectMap;
import com.fasterxml.jackson.databind.util.EnumResolver;
import com.neyogoo.example.common.core.converter.EnumSerializer;
import com.neyogoo.example.common.core.util.StrPool;

import java.io.IOException;
import java.util.Objects;

@SuppressWarnings("ALL")
@JacksonStdImpl
public class EnumDeserializer extends StdScalarDeserializer<Object> implements ContextualDeserializer {

    private static final long serialVersionUID = 1L;
    protected final CompactStringObjectMap lookupByName;
    protected final Boolean caseInsensitive;
    private final Enum<?> enumDefaultValue;
    protected Object[] enumsByIndex;
    protected CompactStringObjectMap lookupByToString;

    public EnumDeserializer(EnumResolver byNameResolver, Boolean caseInsensitive) {
        super(byNameResolver.getEnumClass());
        lookupByName = byNameResolver.constructLookup();
        enumsByIndex = byNameResolver.getRawEnums();
        enumDefaultValue = byNameResolver.getDefaultValue();
        this.caseInsensitive = caseInsensitive;
    }

    protected EnumDeserializer(EnumDeserializer base, Boolean caseInsensitive) {
        super(base);
        lookupByName = base.lookupByName;
        enumsByIndex = base.enumsByIndex;
        enumDefaultValue = base.enumDefaultValue;
        this.caseInsensitive = caseInsensitive;
    }

    @Deprecated
    public EnumDeserializer(EnumResolver byNameResolver) {
        this(byNameResolver, null);
    }

    @Deprecated
    public static JsonDeserializer<?> deserializerForCreator(DeserializationConfig config,
                                                             Class<?> enumClass, AnnotatedMethod factory) {
        return deserializerForCreator(config, enumClass, factory, null, null);
    }

    /**
     * Factory method used when Enum instances are to be deserialized
     * using a creator (static factory method)
     *
     * @return Deserializer based on given factory method
     */
    public static JsonDeserializer<?> deserializerForCreator(
            DeserializationConfig config, Class<?> enumClass, AnnotatedMethod factory,
            ValueInstantiator valueInstantiator, SettableBeanProperty[] creatorProps) {
        if (config.canOverrideAccessModifiers()) {
            ClassUtil.checkAndFixAccess(factory.getMember(),
                    config.isEnabled(MapperFeature.OVERRIDE_PUBLIC_ACCESS_MODIFIERS));
        }
        return new FactoryBasedEnumDeserializer(enumClass, factory,
                factory.getParameterType(0),
                valueInstantiator, creatorProps);
    }

    /**
     * Factory method used when Enum instances are to be deserialized
     * using a zero-/no-args factory method
     *
     * @return Deserializer based on given no-args factory method
     */
    public static JsonDeserializer<?> deserializerForNoArgsCreator(DeserializationConfig config,
                                                                   Class<?> enumClass, AnnotatedMethod factory) {
        if (config.canOverrideAccessModifiers()) {
            ClassUtil.checkAndFixAccess(factory.getMember(),
                    config.isEnabled(MapperFeature.OVERRIDE_PUBLIC_ACCESS_MODIFIERS));
        }
        return new FactoryBasedEnumDeserializer(enumClass, factory);
    }

    public EnumDeserializer withResolved(Boolean caseInsensitive) {
        if (Objects.equals(this.caseInsensitive, caseInsensitive)) {
            return this;
        }
        return new EnumDeserializer(this, caseInsensitive);
    }

    @Override
    public JsonDeserializer<?> createContextual(DeserializationContext ctxt,
                                                BeanProperty property) throws JsonMappingException {
        Boolean caseInsensitive = findFormatFeature(ctxt, property, handledType(),
                JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES);
        if (caseInsensitive == null) {
            caseInsensitive = this.caseInsensitive;
        }
        return withResolved(caseInsensitive);
    }

    /*
    /**********************************************************
    /* Default JsonDeserializer implementation
    /**********************************************************
     */

    /**
     * Because of costs associated with constructing Enum resolvers,
     * let's cache instances by default.
     */
    @Override
    public boolean isCachable() {
        return true;
    }

    @Override
    public LogicalType logicalType() {
        return LogicalType.Enum;
    }

    @Override
    public Object getEmptyValue(DeserializationContext ctxt) throws JsonMappingException {
        return enumDefaultValue;
    }

    @Override
    public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        JsonToken curr = p.currentToken();

        // Usually should just get string value:
        // 04-Sep-2020, tatu: for 2.11.3 / 2.12.0, removed "FIELD_NAME" as allowed;
        //   did not work and gave odd error message.
        if (curr == JsonToken.VALUE_STRING) {
            return fromString(p, ctxt, p.getText());
        }

        if (curr == JsonToken.START_OBJECT) {
            CompactStringObjectMap lookup = ctxt.isEnabled(DeserializationFeature.READ_ENUMS_USING_TO_STRING)
                    ? getToStringLookup(ctxt) : lookupByName;
            JsonNode node = p.getCodec().readTree(p);
            JsonNode code = node.get(EnumSerializer.ALL_ENUM_KEY_FIELD);
            String name = code != null ? code.asText() : node.asText();
            if (StrUtil.isBlank(name) || StrPool.NULL.equals(name)) {
                return null;
            }
            Object result = lookup.find(name);
            if (result == null) {
                return deserializeAltString(p, ctxt, lookup, name);
            }
            return result;
        }

        // But let's consider int acceptable as well (if within ordinal range)
        if (curr == JsonToken.VALUE_NUMBER_INT) {
            return fromInteger(p, ctxt, p.getIntValue());
        }

        // 29-Jun-2020, tatu: New! "Scalar from Object" (mostly for XML)
        if (p.isExpectedStartObjectToken()) {
            return fromString(p, ctxt,
                    ctxt.extractScalarFromObject(p, this, _valueClass));
        }
        return deserializeOther(p, ctxt);
    }

    protected Object fromString(JsonParser p, DeserializationContext ctxt,
                                String text)
            throws IOException {
        CompactStringObjectMap lookup = ctxt.isEnabled(DeserializationFeature.READ_ENUMS_USING_TO_STRING)
                ? getToStringLookup(ctxt) : lookupByName;
        if (CharSequenceUtil.isBlank(text) || StrPool.NULL.equals(text)) {
            return null;
        }
        Object result = lookup.find(text);
        if (result == null) {
            String trimmed = text.trim();
            if (Objects.equals(trimmed, text)) {
                return deserializeAltString(p, ctxt, lookup, trimmed);
            } else {
                result = lookup.find(trimmed);
                if (result == null) {
                    return deserializeAltString(p, ctxt, lookup, trimmed);
                }
            }
        }
        return result;
    }

    protected Object fromInteger(JsonParser p, DeserializationContext ctxt, int index)
            throws IOException {
        final CoercionAction act = ctxt.findCoercionAction(logicalType(), handledType(),
                CoercionInputShape.Integer);

        // First, check legacy setting for slightly different message
        if (act == CoercionAction.Fail) {
            if (ctxt.isEnabled(DeserializationFeature.FAIL_ON_NUMBERS_FOR_ENUMS)) {
                return ctxt.handleWeirdNumberValue(enumClass(), index,
                        "not allowed to deserialize Enum value out of number: disable "
                                + "DeserializationConfig.DeserializationFeature.FAIL_ON_NUMBERS_FOR_ENUMS to allow"
                );
            }
            // otherwise this will force failure with new setting
            _checkCoercionFail(ctxt, act, handledType(), index,
                    "Integer value (" + index + ")");
        }
        switch (act) {
            case AsNull:
                return null;
            case AsEmpty:
                return getEmptyValue(ctxt);
            case TryConvert:
            default:
        }
        if (index >= 0 && index < enumsByIndex.length) {
            return enumsByIndex[index];
        }
        if ((enumDefaultValue != null)
                && ctxt.isEnabled(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_USING_DEFAULT_VALUE)) {
            return enumDefaultValue;
        }
        if (!ctxt.isEnabled(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL)) {
            return ctxt.handleWeirdNumberValue(enumClass(), index,
                    "index value outside legal index range [0..%s]",
                    enumsByIndex.length - 1);
        }
        return null;
    }

    /*
    /**********************************************************
    /* Internal helper methods
    /**********************************************************
     */

    private Object deserializeAltString(JsonParser p, DeserializationContext ctxt,
                                        CompactStringObjectMap lookup, String nameOrig) throws IOException {
        String name = nameOrig.trim();
        if (name.isEmpty()) {
            // 07-Jun-2021, tatu: [databind#3171] Need to consider Default value first
            //   (alas there's bit of duplication here)
            if ((enumDefaultValue != null)
                    && ctxt.isEnabled(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_USING_DEFAULT_VALUE)) {
                return enumDefaultValue;
            }
            if (ctxt.isEnabled(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL)) {
                return null;
            }

            CoercionAction act;
            if (nameOrig.isEmpty()) {
                act = _findCoercionFromEmptyString(ctxt);
                act = _checkCoercionFail(ctxt, act, handledType(), nameOrig,
                        "empty String (\"\")");
            } else {
                act = _findCoercionFromBlankString(ctxt);
                act = _checkCoercionFail(ctxt, act, handledType(), nameOrig,
                        "blank String (all whitespace)");
            }
            switch (act) {
                case AsEmpty:
                case TryConvert:
                    return getEmptyValue(ctxt);
                case AsNull:
                default: // Fail already handled earlier
            }
            return null;
            // if (ctxt.isEnabled(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT)) {
        } else {
            // [databind#1313]: Case insensitive enum deserialization
            if (Boolean.TRUE.equals(caseInsensitive)) {
                Object match = lookup.findCaseInsensitive(name);
                if (match != null) {
                    return match;
                }
            } else if (!ctxt.isEnabled(DeserializationFeature.FAIL_ON_NUMBERS_FOR_ENUMS)) {
                // [databind#149]: Allow use of 'String' indexes as well -- unless prohibited (as per above)
                char c = name.charAt(0);
                if (c >= '0' && c <= '9') {
                    try {
                        int index = Integer.parseInt(name);
                        if (!ctxt.isEnabled(MapperFeature.ALLOW_COERCION_OF_SCALARS)) {
                            return ctxt.handleWeirdStringValue(enumClass(), name,
                                    "value looks like quoted Enum index, but "
                                            + "`MapperFeature.ALLOW_COERCION_OF_SCALARS` prevents use"
                            );
                        }
                        if (index >= 0 && index < enumsByIndex.length) {
                            return enumsByIndex[index];
                        }
                    } catch (NumberFormatException e) {
                        // fine, ignore, was not an integer
                    }
                }
            }
        }
        if ((enumDefaultValue != null)
                && ctxt.isEnabled(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_USING_DEFAULT_VALUE)) {
            return enumDefaultValue;
        }
        if (ctxt.isEnabled(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL)) {
            return null;
        }
        return ctxt.handleWeirdStringValue(enumClass(), name,
                "not one of the values accepted for Enum class: %s", lookup.keys());
    }

    protected Object deserializeOther(JsonParser p, DeserializationContext ctxt) throws IOException {
        // [databind#381]
        if (p.hasToken(JsonToken.START_ARRAY)) {
            return _deserializeFromArray(p, ctxt);
        }
        return ctxt.handleUnexpectedToken(enumClass(), p);
    }

    protected Class<?> enumClass() {
        return handledType();
    }

    protected CompactStringObjectMap getToStringLookup(DeserializationContext ctxt) {
        CompactStringObjectMap lookup = lookupByToString;
        // note: exact locking not needed; all we care for here is to try to
        // reduce contention for the initial resolution
        if (lookup == null) {
            synchronized (this) {
                lookup = EnumResolver.constructUsingToString(ctxt.getConfig(), enumClass())
                        .constructLookup();
            }
            lookupByToString = lookup;
        }
        return lookup;
    }
}