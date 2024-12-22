package com.suchtool.nicelog.aspect.impl;

import com.suchtool.nicelog.aspect.LogAspectProcessor;
import com.suchtool.nicelog.aspect.LogCommonAspectExecutor;
import com.suchtool.nicelog.aspect.NiceLogAspectDispatcher;
import com.suchtool.nicelog.constant.EntryTypeEnum;
import com.suchtool.nicelog.constant.NiceLogPointcutExpression;
import com.suchtool.nicetool.util.spring.ApplicationContextHolder;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.core.Ordered;

import java.lang.reflect.Method;

/**
 * NiceLog注解日志
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

    @Override
    public String pointcutExpression() {
        return NiceLogPointcutExpression.NICE_LOG_ANNOTATION_ASPECT;
    }

    @Pointcut(NiceLogPointcutExpression.NICE_LOG_ANNOTATION_ASPECT)
    public void pointcut() {
    }

    @Before("pointcut()")
    public void before(JoinPoint joinPoint) {
        LogAspectProcessor processor = ApplicationContextHolder.getContext()
                .getBean(NiceLogAspectDispatcher.class).findMatched(joinPoint);
        if (processor != null) {
            processor.before(joinPoint);
        } else {
            logCommonAspectExecutor.before(joinPoint);
        }
    }

    @AfterReturning(value = "pointcut()", returning = "returnValue")
    public void afterReturning(JoinPoint joinPoint, Object returnValue) {
        LogAspectProcessor processor = ApplicationContextHolder.getContext()
                .getBean(NiceLogAspectDispatcher.class).findMatched(joinPoint);
        if (processor != null) {
            processor.afterReturning(joinPoint, returnValue);
        } else {
            logCommonAspectExecutor.afterReturning(joinPoint, returnValue);
        }
    }

    @AfterThrowing(value = "pointcut()", throwing = "throwingValue")
    public void afterThrowing(JoinPoint joinPoint, Throwable throwingValue) {
        LogAspectProcessor processor = ApplicationContextHolder.getContext()
                .getBean(NiceLogAspectDispatcher.class).findMatched(joinPoint);
        if (processor != null) {
            processor.afterThrowing(joinPoint, throwingValue);
        } else {
            logCommonAspectExecutor.afterThrowing(joinPoint, throwingValue);
        }
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