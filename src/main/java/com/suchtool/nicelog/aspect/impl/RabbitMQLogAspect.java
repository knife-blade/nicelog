package com.suchtool.nicelog.aspect.impl;

import com.suchtool.nicelog.aspect.LogCommonAspectExecutor;
import com.suchtool.nicelog.aspect.LogAspectProcessor;
import com.suchtool.nicelog.constant.EntryTypeEnum;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.core.Ordered;

import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;

/**
 * RabbitMQ日志
 */
@Aspect
public class RabbitMQLogAspect extends LogAspectProcessor implements Ordered {
    private final LogCommonAspectExecutor logCommonAspectExecutor;

    private final int order;

    public RabbitMQLogAspect(int order) {
        this.logCommonAspectExecutor = new LogCommonAspectExecutor(this);
        this.order = order;
    }

    @Override
    public int getOrder() {
        return order;
    }

    @Pointcut("@annotation(org.springframework.amqp.rabbit.annotation.RabbitListener)")
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
    public String provideParam(String param, Method method, Object[] args) {
        Message message = (Message) args[0];
        return new String(message.getBody(), StandardCharsets.UTF_8);
    }

    @Override
    public EntryTypeEnum provideEntryType() {
        return EntryTypeEnum.RABBIT_MQ;
    }

    @Override
    public String provideClassTag(Method method) {
        return null;
    }

    @Override
    public String provideMethodTag(Method method) {
        String methodTag = null;

        if (method.isAnnotationPresent(RabbitListener.class)) {
            String[] queues = method.getAnnotation(RabbitListener.class).queues();
            methodTag = String.join(",", queues);
        }

        return methodTag;
    }

    @Override
    public String provideEntry(Method method) {
        return provideMethodTag(method);
    }

}