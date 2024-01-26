package com.suchtool.betterlog.aspect;

import java.lang.reflect.Method;

public interface LogAspectProcessor extends LogParamProvider{
    /**
     * 判断是否需要处理
     */
    boolean requireProcess(Method method);

    /**
     * 正常返回或者抛异常的处理
     */
    void returningOrThrowingProcess();
}
