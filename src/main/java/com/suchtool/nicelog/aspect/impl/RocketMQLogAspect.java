package com.suchtool.nicelog.aspect.impl;

import com.suchtool.nicelog.aspect.LogAspectProcessor;
import com.suchtool.nicelog.aspect.LogCommonAspectExecutor;
import com.suchtool.nicelog.constant.EntryTypeEnum;
import com.suchtool.nicelog.constant.NiceLogPointcutExpression;
import com.suchtool.nicelog.constant.ProcessIgnoreUrl;
import com.suchtool.nicelog.util.log.context.NiceLogContext;
import com.suchtool.nicelog.util.log.context.NiceLogContextThreadLocal;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.core.Ordered;
import org.springframework.util.Assert;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * RocketMQ的日志
 */
@Aspect
public class RocketMQLogAspect extends LogAspectProcessor implements Ordered {
    private final LogCommonAspectExecutor logCommonAspectExecutor;

    private final int order;

    public RocketMQLogAspect(int order) {
        this.logCommonAspectExecutor = new LogCommonAspectExecutor(this);
        this.order = order;
    }

    @Override
    public int getOrder() {
        return order;
    }

    @Override
    public String pointcutExpression() {
        return NiceLogPointcutExpression.ROCKET_MQ_LOG_ASPECT;
    }

    @Pointcut(NiceLogPointcutExpression.ROCKET_MQ_LOG_ASPECT + " && "
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

    /**
     * 正常返回或者抛异常的处理
     */
    @Override
    public void returningOrThrowingProcess() {

    }

    @Override
    public EntryTypeEnum provideEntryType() {
        return EntryTypeEnum.ROCKETMQ;
    }

    @Override
    public String provideEntry(Method method) {
        Class<?> declaringClass = method.getDeclaringClass();
        RocketMQMessageListener rocketMQMessageListener = declaringClass
                .getAnnotation(RocketMQMessageListener.class);
        return rocketMQMessageListener.topic();
    }
}