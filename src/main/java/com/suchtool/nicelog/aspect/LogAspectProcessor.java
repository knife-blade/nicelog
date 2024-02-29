package com.suchtool.nicelog.aspect;

import com.suchtool.nicelog.annotation.NiceLogIgnore;
import com.suchtool.nicelog.constant.EntryTypeEnum;
import com.suchtool.nicelog.property.NiceLogProperty;
import com.suchtool.niceutil.util.spring.ApplicationContextHolder;

import java.lang.reflect.Method;

public abstract class LogAspectProcessor implements LogParamProvider{

    /**
     * 判断是否需要处理
     */
    public boolean requireProcess(Method method) {
        NiceLogProperty niceLogProperty = ApplicationContextHolder.getContext()
                .getBean(NiceLogProperty.class);
        // 如果不是收集所有，又不是@NiceLog注解的，则不处理
        if (!niceLogProperty.getCollectAll()) {
            if (!EntryTypeEnum.NICE_LOG_ANNOTATION.equals(provideEntryType())) {
                return false;
            }
        }

        // 如果类或方法上有NiceLogIgnore，则不处理
        Class<?> declaringClass = method.getDeclaringClass();
        return !method.isAnnotationPresent(NiceLogIgnore.class)
                && !declaringClass.isAnnotationPresent(NiceLogIgnore.class);
    }

    /**
     * 正常返回或者抛异常的处理
     */
     public abstract void returningOrThrowingProcess();

    /**
     * 判断是否需要记录上下文
     */
    public boolean requireRecordContext() {
        return true;
    }
}
