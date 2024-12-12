package com.neyogoo.example.common.mq.listener;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.neyogoo.example.common.core.constant.ContextConstant;
import com.neyogoo.example.common.core.context.ContextUtils;
import com.neyogoo.example.common.core.util.StringFormatUtils;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.core.MessageProperties;

import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * 自定义的 Header 处理，用于添加 orgId、userId 等内置 ThreadLocal 变量
 */
public class CustomerHeaderProcess implements MessagePostProcessor {

    /**
     * 待添加的 Header
     */
    public static final List<String> HEADER_NAME_LIST = ImmutableList.of(
            ContextConstant.HEADER_ORG_ID,
            ContextConstant.HEADER_USER_TYPE,
            ContextConstant.HEADER_USER_ID,
            ContextConstant.HEADER_USER_ACCOUNT,
            ContextConstant.HEADER_USER_NAME,
            ContextConstant.HEADER_LOGIN_POINT,
            ContextConstant.HEADER_FEIGN,
            ContextConstant.HEADER_TRACE_ID
    );

    public static final Set<String> CN_HEADER_VALUE_SET = ImmutableSet.of(
            ContextConstant.HEADER_USER_NAME
    );

    @Override
    public Message postProcessMessage(Message message) throws AmqpException {
        MessageProperties messageProperties = message.getMessageProperties();
        if (Objects.isNull(messageProperties)) {
            return message;
        }

        for (String headerName : HEADER_NAME_LIST) {
            if (CN_HEADER_VALUE_SET.contains(headerName)) {
                // 中文传输可能存在问题，转为 unicode 传输
                messageProperties.setHeader(headerName, StringFormatUtils.cnToUnicode(ContextUtils.get(headerName)));
            } else {
                messageProperties.setHeader(headerName, ContextUtils.get(headerName));
            }
        }
        return message;
    }
}
