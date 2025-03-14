package com.suchtool.nicelog.aspect.impl;

import com.suchtool.nicelog.aspect.NiceLogLogCommonAspectExecutor;
import com.suchtool.nicelog.aspect.NiceLogAspectProcessor;
import com.suchtool.nicelog.constant.EntryTypeEnum;
import com.suchtool.nicelog.constant.NiceLogPointcutExpression;
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
public class NiceLogRabbitMQLogAspect extends NiceLogAspectProcessor implements Ordered {
    private final NiceLogLogCommonAspectExecutor niceLogLogCommonAspectExecutor;

    private final int order;

    public NiceLogRabbitMQLogAspect(int order) {
        this.niceLogLogCommonAspectExecutor = new NiceLogLogCommonAspectExecutor(this);
        this.order = order;
    }

    @Override
    public int getOrder() {
        return order;
    }

    @Override
    public String pointcutExpression() {
        return NiceLogPointcutExpression.RABBIT_MQ_LOG_ASPECT;
    }

    @Pointcut(NiceLogPointcutExpression.RABBIT_MQ_LOG_ASPECT
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
    public String provideParam(String param, Method method, Object[] args) {
        String resultParam = null;
        Object arg = args[0];
        if (arg instanceof Message) {
            Message message = (Message) arg;
            resultParam = new String(message.getBody(), StandardCharsets.UTF_8);
        } else if (arg instanceof String) {
            resultParam = (String) arg;
        }

        return resultParam;
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