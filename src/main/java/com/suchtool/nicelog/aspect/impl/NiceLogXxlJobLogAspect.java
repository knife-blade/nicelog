package com.suchtool.nicelog.aspect.impl;

import com.suchtool.nicelog.aspect.NiceLogAspectExecutor;
import com.suchtool.nicelog.aspect.NiceLogAbstractAspect;
import com.suchtool.nicelog.aspect.provider.impl.xxljob.NiceLogXxlJobParamProvider;
import com.suchtool.nicelog.constant.NiceLogPointcutExpression;
import com.suchtool.nicelog.property.NiceLogProperty;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.core.Ordered;

/**
 * XxlJob日志
 */
@Aspect
public class NiceLogXxlJobLogAspect extends NiceLogAbstractAspect implements Ordered {
    private final NiceLogAspectExecutor niceLogAspectExecutor;

    private final int order;

    public NiceLogXxlJobLogAspect(int order, NiceLogProperty niceLogProperty) {
        this.niceLogAspectExecutor = new NiceLogAspectExecutor(
                this, new NiceLogXxlJobParamProvider(niceLogProperty), niceLogProperty);
        this.order = order;
    }

    @Override
    public int getOrder() {
        return order;
    }

    @Override
    public String pointcutExpression() {
        return NiceLogPointcutExpression.XXL_JOB_LOG_ASPECT;
    }

    @Pointcut(NiceLogPointcutExpression.XXL_JOB_LOG_ASPECT
            + " && !(" + NiceLogPointcutExpression.NICE_LOG_ANNOTATION_ASPECT + ")")
    public void pointcut() {
    }

    @Before("pointcut()")
    public void before(JoinPoint joinPoint) {
        niceLogAspectExecutor.before(joinPoint);
    }

    @AfterReturning(value = "pointcut()", returning = "returnValue")
    public void afterReturning(JoinPoint joinPoint, Object returnValue) {
        niceLogAspectExecutor.afterReturning(joinPoint, returnValue);
    }

    @AfterThrowing(value = "pointcut()", throwing = "throwingValue")
    public void afterThrowing(JoinPoint joinPoint, Throwable throwingValue) {
        niceLogAspectExecutor.afterThrowing(joinPoint, throwingValue);
    }
}