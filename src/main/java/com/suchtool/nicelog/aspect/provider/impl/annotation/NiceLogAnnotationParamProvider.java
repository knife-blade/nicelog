package com.suchtool.nicelog.aspect.provider.impl.annotation;

import com.suchtool.nicelog.aspect.provider.NiceLogParamProvider;
import com.suchtool.nicelog.constant.NiceLogEntryTypeEnum;
import com.suchtool.nicelog.property.NiceLogProperty;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;

/**
 * Controller参数提供者
 */
@Slf4j
public class NiceLogAnnotationParamProvider extends NiceLogParamProvider {
    public NiceLogAnnotationParamProvider(NiceLogProperty niceLogProperty) {
        super(niceLogProperty);
    }

    @Override
    public String provideEntryType() {
        return NiceLogEntryTypeEnum.NICE_LOG_ANNOTATION.name();
    }

    @Override
    public String provideClassTag(Method method) {
        return null;
    }
}