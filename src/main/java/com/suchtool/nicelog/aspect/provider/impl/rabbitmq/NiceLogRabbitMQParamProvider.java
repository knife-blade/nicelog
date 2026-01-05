package com.suchtool.nicelog.aspect.provider.impl.rabbitmq;

import com.suchtool.nicelog.aspect.provider.NiceLogParamProvider;
import com.suchtool.nicelog.constant.NiceLogEntryTypeEnum;
import com.suchtool.nicelog.property.NiceLogProperty;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;

/**
 * RabbitMQ参数提供者
 */
@Slf4j
public class NiceLogRabbitMQParamProvider extends NiceLogParamProvider {


    public NiceLogRabbitMQParamProvider(NiceLogProperty niceLogProperty) {
        super(niceLogProperty);
    }

    @Override
    public String provideParam(String param, Method method, Object[] args) {
        String resultParam = null;
        Object arg = args[0];
        if (arg instanceof Message) {
            Message message = (Message) arg;
            resultParam = new String(message.getBody(), StandardCharsets.UTF_8);
        } else if (arg instanceof String) {
            resultParam = (String) arg;
        }

        return resultParam;
    }

    @Override
    public String provideEntryType() {
        return NiceLogEntryTypeEnum.RABBIT_MQ.name();
    }

    @Override
    public String provideClassTag(Method method) {
        return null;
    }

    @Override
    public String provideMethodTag(Method method) {
        String methodTag = null;

        if (method.isAnnotationPresent(RabbitListener.class)) {
            String[] queues = method.getAnnotation(RabbitListener.class).queues();
            methodTag = String.join(",", queues);
        }

        return methodTag;
    }

    @Override
    public String provideEntry(Method method) {
        return provideMethodTag(method);
    }
}