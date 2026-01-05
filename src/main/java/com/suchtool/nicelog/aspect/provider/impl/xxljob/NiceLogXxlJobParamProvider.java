package com.suchtool.nicelog.aspect.provider.impl.xxljob;

import com.suchtool.nicelog.aspect.provider.NiceLogParamProvider;
import com.suchtool.nicelog.constant.NiceLogEntryTypeEnum;
import com.suchtool.nicelog.property.NiceLogProperty;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;

/**
 * XxlJob参数提供者
 */
@Slf4j
public class NiceLogXxlJobParamProvider extends NiceLogParamProvider {
    public NiceLogXxlJobParamProvider(NiceLogProperty niceLogProperty) {
        super(niceLogProperty);
    }

    @Override
    public String provideEntryType() {
        return NiceLogEntryTypeEnum.XXL_JOB.name();
    }

    @Override
    public String provideMethodTag(Method method) {
        String methodTag = null;

        if (method.isAnnotationPresent(XxlJob.class)) {
            methodTag = method.getAnnotation(XxlJob.class).value();
        }

        return methodTag;
    }

    @Override
    public String provideEntry(Method method) {
        return provideMethodTag(method);
    }
}