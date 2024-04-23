package com.suchtool.nicelog.aspect.impl;

import com.suchtool.nicelog.aspect.LogAspectProcessor;
import com.suchtool.nicelog.aspect.LogCommonAspectExecutor;
import com.suchtool.nicelog.constant.EntryTypeEnum;
import com.suchtool.nicelog.constant.NiceLogPointcutExpression;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.core.Ordered;
import org.springframework.kafka.annotation.KafkaListener;

import java.lang.reflect.Method;

/**
 * Kafka日志
 */
@Aspect
public class KafkaLogAspect extends LogAspectProcessor implements Ordered {
    private final LogCommonAspectExecutor logCommonAspectExecutor;

    private final int order;

    public KafkaLogAspect(int order) {
        this.logCommonAspectExecutor = new LogCommonAspectExecutor(this);
        this.order = order;
    }

    @Override
    public int getOrder() {
        return order;
    }

    @Override
    public String pointcutExpression() {
        return NiceLogPointcutExpression.KAFKA_LOG_ASPECT;
    }

    @Pointcut(NiceLogPointcutExpression.KAFKA_LOG_ASPECT + " && "
            + "!" + NiceLogPointcutExpression.NICE_LOG_ANNOTATION_ASPECT)
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
        return EntryTypeEnum.KAFKA;
    }

    @Override
    public String provideClassTag(Method method) {
        return null;
    }

    @Override
    public String provideMethodTag(Method method) {
        String methodTag = null;

        if (method.isAnnotationPresent(KafkaListener.class)) {
            String[] queues = method.getAnnotation(KafkaListener.class).topics();
            methodTag = String.join(",", queues);
        }

        return methodTag;
    }

    @Override
    public String provideEntry(Method method) {
        return provideMethodTag(method);
    }

}