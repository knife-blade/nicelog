package com.suchtool.nicelog.aspect.provider.impl.rocketmq;

import com.suchtool.nicelog.aspect.provider.NiceLogParamProvider;
import com.suchtool.nicelog.constant.NiceLogEntryTypeEnum;
import com.suchtool.nicelog.property.NiceLogProperty;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;

import java.lang.reflect.Method;

/**
 * RocketMQ参数提供者
 */
@Slf4j
public class NiceLogRocketMQParamProvider extends NiceLogParamProvider {
    public NiceLogRocketMQParamProvider(NiceLogProperty niceLogProperty) {
        super(niceLogProperty);
    }

    @Override
    public String provideEntryType() {
        return NiceLogEntryTypeEnum.ROCKETMQ.name();
    }

    @Override
    public String provideEntry(Method method) {
        Class<?> declaringClass = method.getDeclaringClass();
        RocketMQMessageListener rocketMQMessageListener = declaringClass
                .getAnnotation(RocketMQMessageListener.class);
        return rocketMQMessageListener.topic();
    }
}