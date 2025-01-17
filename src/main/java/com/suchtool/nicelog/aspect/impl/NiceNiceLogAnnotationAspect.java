package com.suchtool.nicelog.aspect.impl;

import com.suchtool.nicelog.aspect.NiceLogAspectProcessor;
import com.suchtool.nicelog.aspect.NiceLogLogCommonAspectExecutor;
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
public class NiceNiceLogAnnotationAspect extends NiceLogAspectProcessor implements Ordered {
    private final NiceLogLogCommonAspectExecutor niceLogLogCommonAspectExecutor;

    private final int order;

    public NiceNiceLogAnnotationAspect(int order) {
        this.niceLogLogCommonAspectExecutor = new NiceLogLogCommonAspectExecutor(this);
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
        NiceLogAspectProcessor processor = ApplicationContextHolder.getContext()
                .getBean(NiceLogAspectDispatcher.class).findMatched(joinPoint);
        if (processor != null) {
            processor.before(joinPoint);
        } else {
            niceLogLogCommonAspectExecutor.before(joinPoint);
        }
    }

    @AfterReturning(value = "pointcut()", returning = "returnValue")
    public void afterReturning(JoinPoint joinPoint, Object returnValue) {
        NiceLogAspectProcessor processor = ApplicationContextHolder.getContext()
                .getBean(NiceLogAspectDispatcher.class).findMatched(joinPoint);
        if (processor != null) {
            processor.afterReturning(joinPoint, returnValue);
        } else {
            niceLogLogCommonAspectExecutor.afterReturning(joinPoint, returnValue);
        }
    }

    @AfterThrowing(value = "pointcut()", throwing = "throwingValue")
    public void afterThrowing(JoinPoint joinPoint, Throwable throwingValue) {
        NiceLogAspectProcessor processor = ApplicationContextHolder.getContext()
                .getBean(NiceLogAspectDispatcher.class).findMatched(joinPoint);
        if (processor != null) {
            processor.afterThrowing(joinPoint, throwingValue);
        } else {
            niceLogLogCommonAspectExecutor.afterThrowing(joinPoint, throwingValue);
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