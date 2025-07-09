package com.suchtool.nicelog.aspect.impl;

import com.suchtool.nicelog.aspect.NiceLogAspectProcessor;
import com.suchtool.nicelog.aspect.NiceLogLogCommonAspectExecutor;
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
public class NiceLogKafkaLogAspect extends NiceLogAspectProcessor implements Ordered {
    private final NiceLogLogCommonAspectExecutor niceLogLogCommonAspectExecutor;

    private final int order;

    public NiceLogKafkaLogAspect(int order) {
        this.niceLogLogCommonAspectExecutor = new NiceLogLogCommonAspectExecutor(this);
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

    @Pointcut(NiceLogPointcutExpression.KAFKA_LOG_ASPECT
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

    @Override
    public void returningOrThrowingProcess() {

    }

    @Override
    public String provideEntryType() {
        return EntryTypeEnum.KAFKA.name();
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