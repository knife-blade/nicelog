package com.suchtool.betterlog.aspect;

public interface LogAspectProcessor extends LogParamProvider{
    /**
     * 判断是否需要处理
     */
    boolean requireProcess();

    /**
     * 正常返回或者抛异常的处理
     */
    void returningOrThrowingProcess();
}
