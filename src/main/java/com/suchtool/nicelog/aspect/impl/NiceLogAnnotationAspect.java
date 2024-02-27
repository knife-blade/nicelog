package com.suchtool.nicelog.aspect.impl;

import com.suchtool.nicelog.aspect.LogCommonAspectExecutor;
import com.suchtool.nicelog.aspect.LogAspectProcessor;
import com.suchtool.nicelog.constant.EntryTypeEnum;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.core.Ordered;

import java.lang.reflect.Method;

/**
 * @NiceLog注解日志
 */
@Aspect
public class NiceLogAnnotationAspect extends LogAspectProcessor implements Ordered {
    private final LogCommonAspectExecutor logCommonAspectExecutor;

    private final int order;

    public NiceLogAnnotationAspect(int order) {
        this.logCommonAspectExecutor = new LogCommonAspectExecutor(this);
        this.order = order;
    }

    @Override
    public int getOrder() {
        return order;
    }

    @Pointcut("@annotation(com.suchtool.nicelog.annotation.NiceLog) ||" +
            "@within(com.suchtool.nicelog.annotation.NiceLog)")
    public void pointcut() {
    }

    @Before("pointcut()")
    public void before(JoinPoint joinPoint) {
        logCommonAspectExecutor.before(joinPoint);
    }

    @AfterReturning(value = "pointcut()", returning = "returnValue")
    public void afterReturning(JoinPoint joinPoint, Object returnValue) {
        logCommonAspectExecutor.afterReturning(joinPoint, returnValue);
    }

    @AfterThrowing(value = "pointcut()", throwing = "throwingValue")
    public void afterThrowing(JoinPoint joinPoint, Throwable throwingValue) {
        logCommonAspectExecutor.afterThrowing(joinPoint, throwingValue);
    }

    @Override
    public void returningOrThrowingProcess() {

    }

    @Override
    public EntryTypeEnum provideEntryType() {
        return EntryTypeEnum.NICE_LOG_ANNOTATION;
    }

    @Override
    public String provideClassTag(Method method) {
        return null;
    }

}