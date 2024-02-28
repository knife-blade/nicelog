package com.suchtool.nicelog.aspect;

import com.suchtool.nicelog.annotation.NiceLog;
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
        if (niceLogProperty.getCollectAll()) {
            return true;
        }

        Class<?> declaringClass = method.getDeclaringClass();
        return method.isAnnotationPresent(NiceLog.class)
                || declaringClass.isAnnotationPresent(NiceLog.class);
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
