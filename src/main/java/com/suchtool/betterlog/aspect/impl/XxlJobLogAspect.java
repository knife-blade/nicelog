package com.suchtool.betterlog.aspect.impl;

import com.suchtool.betterlog.aspect.LogAspectExecutor;
import com.suchtool.betterlog.aspect.LogAspectProcessor;
import com.suchtool.betterlog.constant.AspectTypeEnum;
import com.xxl.job.core.handler.annotation.XxlJob;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.core.Ordered;

import java.lang.reflect.Method;

/**
 * XxlJob日志
 */
@Aspect
public class XxlJobLogAspect  implements LogAspectProcessor, Ordered {
    private final LogAspectExecutor logAspectExecutor;

    private final int order;

    public XxlJobLogAspect(int order) {
        this.logAspectExecutor = new LogAspectExecutor(this);
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
        logAspectExecutor.before(joinPoint);
    }

    @AfterReturning(value = "pointcut()", returning = "returnValue")
    public void afterReturning(JoinPoint joinPoint, Object returnValue) {
        logAspectExecutor.afterReturning(joinPoint, returnValue);
    }

    @AfterThrowing(value = "pointcut()", throwing = "throwingValue")
    public void afterThrowing(JoinPoint joinPoint, Throwable throwingValue) {
        logAspectExecutor.afterThrowing(joinPoint, throwingValue);
    }

    @Override
    public boolean requireProcess(Method method) {
        return true;
    }

    /**
     * 正常返回或者抛异常的处理
     */
    @Override
    public void returningOrThrowingProcess() {

    }

    @Override
    public AspectTypeEnum provideType() {
        return AspectTypeEnum.XXL_JOB;
    }

    @Override
    public String provideClassTag(Method method) {
        return null;
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
        String entry = null;

        if (method.isAnnotationPresent(XxlJob.class)) {
            entry = method.getAnnotation(XxlJob.class).value();
        }

        return entry;
    }

}