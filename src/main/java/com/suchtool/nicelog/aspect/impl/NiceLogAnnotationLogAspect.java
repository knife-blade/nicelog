package com.suchtool.nicelog.aspect.impl;

import com.suchtool.nicelog.aspect.NiceLogAbstractAspect;
import com.suchtool.nicelog.aspect.NiceLogAspectExecutor;
import com.suchtool.nicelog.aspect.NiceLogAspectDispatcher;
import com.suchtool.nicelog.aspect.provider.impl.annotation.NiceLogAnnotationParamProvider;
import com.suchtool.nicelog.constant.NiceLogPointcutExpression;
import com.suchtool.nicelog.property.NiceLogProperty;
import com.suchtool.nicetool.util.spring.ApplicationContextHolder;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.core.Ordered;

/**
 * NiceLog注解日志
 */
@Aspect
public class NiceLogAnnotationLogAspect extends NiceLogAbstractAspect implements Ordered {
    private final NiceLogAspectExecutor niceLogAspectExecutor;

    private final int order;

    public NiceLogAnnotationLogAspect(int order, NiceLogProperty niceLogProperty) {
        this.niceLogAspectExecutor = new NiceLogAspectExecutor(
                this, new NiceLogAnnotationParamProvider(niceLogProperty), niceLogProperty);
        this.order = order;
    }

    @Override
    public int getOrder() {
        return order;
    }

    @Override
    public String pointcutExpression() {
        // 这里要返回不匹配。否则只有@NiceLog切面时，会死循环
        return NiceLogPointcutExpression.NOTHING_ASPECT;
    }

    @Pointcut(NiceLogPointcutExpression.NICE_LOG_ANNOTATION_ASPECT)
    public void pointcut() {
    }

    @Before("pointcut()")
    public void before(JoinPoint joinPoint) {
        NiceLogAbstractAspect processor = ApplicationContextHolder.getContext()
                .getBean(NiceLogAspectDispatcher.class)
                .findMatched(joinPoint);
        if (processor != null) {
            processor.before(joinPoint);
        } else {
            niceLogAspectExecutor.before(joinPoint);
        }
    }

    @AfterReturning(value = "pointcut()", returning = "returnValue")
    public void afterReturning(JoinPoint joinPoint, Object returnValue) {
        NiceLogAbstractAspect processor = ApplicationContextHolder.getContext()
                .getBean(NiceLogAspectDispatcher.class).findMatched(joinPoint);
        if (processor != null) {
            processor.afterReturning(joinPoint, returnValue);
        } else {
            niceLogAspectExecutor.afterReturning(joinPoint, returnValue);
        }
    }

    @AfterThrowing(value = "pointcut()", throwing = "throwingValue")
    public void afterThrowing(JoinPoint joinPoint, Throwable throwingValue) {
        NiceLogAbstractAspect processor = ApplicationContextHolder.getContext()
                .getBean(NiceLogAspectDispatcher.class).findMatched(joinPoint);
        if (processor != null) {
            processor.afterThrowing(joinPoint, throwingValue);
        } else {
            niceLogAspectExecutor.afterThrowing(joinPoint, throwingValue);
        }
    }
}