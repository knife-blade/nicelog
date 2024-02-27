package com.suchtool.nicelog.aspect.impl;

import com.suchtool.nicelog.aspect.LogCommonAspectExecutor;
import com.suchtool.nicelog.aspect.LogAspectProcessor;
import com.suchtool.nicelog.constant.EntryTypeEnum;
import com.xxl.job.core.handler.annotation.XxlJob;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.core.Ordered;

import java.lang.reflect.Method;

/**
 * XxlJob日志
 */
@Aspect
public class XxlJobLogAspect  extends LogAspectProcessor implements Ordered {
    private final LogCommonAspectExecutor logCommonAspectExecutor;

    private final int order;

    public XxlJobLogAspect(int order) {
        this.logCommonAspectExecutor = new LogCommonAspectExecutor(this);
        this.order = order;
    }

    @Override
    public int getOrder() {
        return order;
    }

    @Pointcut("@annotation(com.xxl.job.core.handler.annotation.XxlJob)")
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

    /**
     * 正常返回或者抛异常的处理
     */
    @Override
    public void returningOrThrowingProcess() {

    }

    @Override
    public EntryTypeEnum provideEntryType() {
        return EntryTypeEnum.XXL_JOB;
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