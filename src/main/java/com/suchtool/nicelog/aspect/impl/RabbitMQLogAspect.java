package com.suchtool.nicelog.aspect.impl;

import com.suchtool.nicelog.aspect.LogAspectExecutor;
import com.suchtool.nicelog.aspect.LogAspectProcessor;
import com.suchtool.nicelog.constant.AspectTypeEnum;
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
    private final LogAspectExecutor logAspectExecutor;

    private final int order;

    public RabbitMQLogAspect(int order) {
        this.logAspectExecutor = new LogAspectExecutor(this);
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
    public void returningOrThrowingProcess() {

    }

    @Override
    public Object provideParam(Object[] args) {
        Message message = (Message) args[0];
        return new String(message.getBody(), StandardCharsets.UTF_8);
    }

    @Override
    public AspectTypeEnum provideType() {
        return AspectTypeEnum.RABBIT_MQ;
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