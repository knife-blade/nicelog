package com.suchtool.nicelog.aspect.provider.impl.kafka;

import com.suchtool.nicelog.aspect.provider.NiceLogParamProvider;
import com.suchtool.nicelog.constant.NiceLogEntryTypeEnum;
import com.suchtool.nicelog.property.NiceLogProperty;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;

import java.lang.reflect.Method;

/**
 * Kafka参数提供者
 */
@Slf4j
public class NiceLogKafkaParamProvider extends NiceLogParamProvider {

    public NiceLogKafkaParamProvider(NiceLogProperty niceLogProperty) {
        super(niceLogProperty);
    }

    @Override
    public String provideEntryType() {
        return NiceLogEntryTypeEnum.KAFKA.name();
    }

    @Override
    public String provideClassTag(Method method) {
        return null;
    }

    @Override
    public String provideMethodTag(Method method) {
        String methodTag = null;

        if (method.isAnnotationPresent(KafkaListener.class)) {
            String[] queues = method.getAnnotation(KafkaListener.class).topics();
            methodTag = String.join(",", queues);
        }

        return methodTag;
    }

    @Override
    public String provideEntry(Method method) {
        return provideMethodTag(method);
    }
}