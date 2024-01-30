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
 * @NiceLog注解日志
 */
@Aspect
public class NiceLogAnnotationAspect extends LogAspectProcessor implements Ordered {
    private final LogAspectExecutor logAspectExecutor;

    private final int order;

    public NiceLogAnnotationAspect(int order) {
        this.logAspectExecutor = new LogAspectExecutor(this);
        this.order = order;
    }

    @Override
    public int getOrder() {
        return order;
    }

    @Pointcut("@annotation(com.suchtool.nicelog.annotation.NiceLog) ||" +
            "@within(com.suchtool.nicelog.annotation.NiceLog)")
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
    public AspectTypeEnum provideType() {
        return AspectTypeEnum.NICE_LOG_ANNOTATION;
    }

    @Override
    public String provideClassTag(Method method) {
        return null;
    }

}