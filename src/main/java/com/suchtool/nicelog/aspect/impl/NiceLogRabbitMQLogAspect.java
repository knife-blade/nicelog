package com.suchtool.nicelog.aspect.impl;

import com.suchtool.nicelog.aspect.NiceLogAspectExecutor;
import com.suchtool.nicelog.aspect.NiceLogAbstractAspect;
import com.suchtool.nicelog.aspect.provider.impl.rabbitmq.NiceLogRabbitMQParamProvider;
import com.suchtool.nicelog.constant.NiceLogPointcutExpression;
import com.suchtool.nicelog.property.NiceLogProperty;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.core.Ordered;

/**
 * RabbitMQ日志
 */
@Aspect
public class NiceLogRabbitMQLogAspect extends NiceLogAbstractAspect implements Ordered {
    private final NiceLogAspectExecutor niceLogAspectExecutor;

    private final int order;

    public NiceLogRabbitMQLogAspect(int order, NiceLogProperty niceLogProperty) {
        this.niceLogAspectExecutor = new NiceLogAspectExecutor(
                this, new NiceLogRabbitMQParamProvider(niceLogProperty), niceLogProperty);
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