package com.neyogoo.example.common.mq.listener;

import cn.hutool.core.collection.CollectionUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.neyogoo.example.common.core.context.ContextUtils;
import com.neyogoo.example.common.core.util.ArgumentAssert;
import com.neyogoo.example.common.core.util.ExceptionUtils;
import com.neyogoo.example.common.core.util.JsonUtils;
import com.neyogoo.example.common.core.util.StringFormatUtils;
import com.neyogoo.example.common.core.util.ValidatorUtils;
import com.neyogoo.example.common.mq.model.MqErrorLogMsg;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Slf4j
public abstract class ConsumerListenerBean<T> extends SimpleMessageListenerContainer
        implements ChannelAwareMessageListener {

    @Autowired
    protected ObjectMapper objectMapper;
    @Autowired
    protected RabbitTemplate rabbitTemplate;

    /**
     * 现有的连接工厂
     */
    @Autowired
    private ConnectionFactory connectionFactory;

    /**
     * 获取队列的名字
     */
    public abstract String getQueueName();

    /**
     * 获取要Json 序列化的类 Class
     */
    public abstract Class<T> getJsonClass();

    /**
     * 获取确认的模式
     */
    @Override
    public AcknowledgeMode getAcknowledgeMode() {
        return AcknowledgeMode.MANUAL;
    }

    /**
     * 其他的处理（主要是应对其他的配置重写的问题）
     */
    protected void otherProcess() {

    }

    /**
     * 主要的消息处理
     *
     * @param message 消息的载荷
     * @param t       传递的类
     * @return true 会确认消息 false 不确认
     */
    public abstract boolean onMessage(Message message, T t);

    @Override
    public void onMessage(Message message, Channel channel) throws Exception {
        T t;
        boolean isConfirm = false;
        try {
            Class<T> clz = getJsonClass();
            if (clz == null) {
                throw new IllegalArgumentException("rabbitmq consumer json type is null");
            }
            MessageProperties messageProperties = message.getMessageProperties();
            for (String headerName : CustomerHeaderProcess.HEADER_NAME_LIST) {
                Object value = messageProperties.getHeader(headerName);
                if (Objects.nonNull(value)) {
                    if (CustomerHeaderProcess.CN_HEADER_VALUE_SET.contains(headerName)) {
                        ContextUtils.set(headerName, StringFormatUtils.unicodeToCn(String.valueOf(value)));
                    } else {
                        ContextUtils.set(headerName, value);
                    }
                }
            }

            String text = new String(message.getBody());
            if ("java.lang.String".equals(clz.getName())) {
                t = (T) text;
            } else if ("java.lang.Integer".equals(clz.getName())) {
                t = (T) Integer.valueOf(text);
            } else if ("java.lang.Long".equals(clz.getName())) {
                t = (T) Long.valueOf(text);
            } else {
                t = objectMapper.readValue(text, getJsonClass());
            }
            if (t == null) {
                throw new IllegalArgumentException("deserialization " + clz.getSimpleName() + " error");
            }

            String format = "receive rabbitmq message，queueName = %s，messageId = %s，text = %s";
            log.info(
                    String.format(
                            format,
                            getQueueName(),
                            messageProperties.getMessageId(),
                            text
                    )
            );
            isConfirm = onMessage(message, t);
        } catch (Exception e) {
            log.error("consume rabbitmq message error，queueName = {}", getQueueName(), e);
            throw e;
        } finally {
            if (getAcknowledgeMode() == AcknowledgeMode.MANUAL && isConfirm) {
                confirm(channel, message);
            }
        }
    }

    /**
     * 确认消息
     *
     * @param channel 通道
     * @param message 消息体
     * @throws IOException 确认异常
     */
    protected void confirm(Channel channel, Message message) throws IOException {
        MessageProperties messageProperties = message.getMessageProperties();
        channel.basicAck(messageProperties.getDeliveryTag(), false);
        log.debug("messageId {} is confirmed", messageProperties.getMessageId());
    }

    /**
     * 初始化当前的Bean
     */
    @PostConstruct
    public void init() {
        setConnectionFactory(connectionFactory);
        setQueueNames(getQueueName());
        setMessageListener(this);
        // 默认手动确认
        setAcknowledgeMode(getAcknowledgeMode());
        otherProcess();
    }

    @Override
    public boolean isAsyncReplies() {
        return ChannelAwareMessageListener.super.isAsyncReplies();
    }

    /**
     * 验证参数
     */
    public void validateParam(T param) {
        List<String> errors = ValidatorUtils.validateEntity(param);
        ArgumentAssert.isTrue(CollectionUtil.isEmpty(errors), StringUtils.join(errors, "；"));
    }

    /**
     * 发送异常记录
     *
     * @param param     消费队列入参
     * @param e         消费异常信息
     * @param queueName 发送队列名称
     */
    public void sendErrorMsg(T param, Exception e, String queueName) {
        MqErrorLogMsg errorMsg = createErrorMsg(param, e);
        String errorJson = JsonUtils.toJson(errorMsg);
        log.info("send rabbitmq error message, queueName = {}, errorJson = {}", queueName, errorJson);
        rabbitTemplate.convertAndSend(queueName, errorJson);
    }

    /**
     * 创建异常记录
     *
     * @param param 消费队列入参
     * @param e     消费异常信息
     */
    public MqErrorLogMsg createErrorMsg(T param, Exception e) {
        return MqErrorLogMsg
                .builder()
                .queueName(getQueueName())
                .paramObj(param)
                .localMap(ContextUtils.getLocalMap())
                .errorMessage(ExceptionUtils.resolveExInfoToList(e))
                .createTime(LocalDateTime.now())
                .build();
    }
}
