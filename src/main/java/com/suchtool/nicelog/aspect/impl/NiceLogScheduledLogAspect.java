package com.suchtool.nicelog.aspect.impl;

import com.suchtool.nicelog.aspect.NiceLogAspectProcessor;
import com.suchtool.nicelog.aspect.NiceLogLogCommonAspectExecutor;
import com.suchtool.nicelog.constant.EntryTypeEnum;
import com.suchtool.nicelog.constant.NiceLogPointcutExpression;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.core.Ordered;

/**
 * Scheduled注解的日志
 */
@Aspect
public class NiceLogScheduledLogAspect extends NiceLogAspectProcessor implements Ordered {
    private final NiceLogLogCommonAspectExecutor niceLogLogCommonAspectExecutor;

    private final int order;

    public NiceLogScheduledLogAspect(int order) {
        this.niceLogLogCommonAspectExecutor = new NiceLogLogCommonAspectExecutor(this);
        this.order = order;
    }

    @Override
    public int getOrder() {
        return order;
    }

    @Override
    public String pointcutExpression() {
        return NiceLogPointcutExpression.SCHEDULED_LOG_ASPECT;
    }

    @Pointcut(NiceLogPointcutExpression.SCHEDULED_LOG_ASPECT
            + " && !(" + NiceLogPointcutExpression.NICE_LOG_ANNOTATION_ASPECT + ")")
    public void pointcut() {
    }

    @Before("pointcut()")
    public void before(JoinPoint joinPoint) {
        niceLogLogCommonAspectExecutor.before(joinPoint);
    }

    @AfterReturning(value = "pointcut()", returning = "returnValue")
    public void afterReturning(JoinPoint joinPoint, Object returnValue) {
        niceLogLogCommonAspectExecutor.afterReturning(joinPoint, returnValue);
    }

    @AfterThrowing(value = "pointcut()", throwing = "throwingValue")
    public void afterThrowing(JoinPoint joinPoint, Throwable throwingValue) {
        niceLogLogCommonAspectExecutor.afterThrowing(joinPoint, throwingValue);
    }

    /**
     * 正常返回或者抛异常的处理
     */
    @Override
    public void returningOrThrowingProcess() {

    }

    @Override
    public EntryTypeEnum provideEntryType() {
        return EntryTypeEnum.SCHEDULED;
    }

}