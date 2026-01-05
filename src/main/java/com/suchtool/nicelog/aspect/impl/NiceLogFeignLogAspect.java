package com.suchtool.nicelog.aspect.impl;

import com.suchtool.nicelog.aspect.NiceLogAbstractAspect;
import com.suchtool.nicelog.aspect.NiceLogAspectExecutor;
import com.suchtool.nicelog.aspect.provider.NiceLogParamProvider;
import com.suchtool.nicelog.aspect.provider.impl.feign.NiceLogFeignParamProvider;
import com.suchtool.nicelog.constant.NiceLogPointcutExpression;
import com.suchtool.nicelog.property.NiceLogProperty;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.core.Ordered;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Method;
import java.util.List;


/**
 * Feign的日志
 */
@Aspect
public class NiceLogFeignLogAspect extends NiceLogAbstractAspect implements Ordered {
    private final NiceLogAspectExecutor niceLogAspectExecutor;

    private final NiceLogProperty niceLogProperty;

    private final int order;

    public NiceLogFeignLogAspect(int order,
                                 NiceLogProperty niceLogProperty,
                                 StandardEnvironment standardEnvironment
    ) {
        NiceLogParamProvider niceLogParamProvider =
                new NiceLogFeignParamProvider(niceLogProperty, standardEnvironment);

        this.niceLogProperty = niceLogProperty;
        this.niceLogAspectExecutor = new NiceLogAspectExecutor(
                this, niceLogParamProvider, niceLogProperty);
        this.order = order;
    }

    @Override
    public int getOrder() {
        return order;
    }

    @Override
    public String pointcutExpression() {
        return NiceLogPointcutExpression.FEIGN_LOG_ASPECT;
    }

    @Pointcut(NiceLogPointcutExpression.FEIGN_LOG_ASPECT
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

    @Override
    public boolean requireProcess(Method method) {
        boolean requireProcess = super.requireProcess(method);
        if (!requireProcess) {
            return requireProcess;
        }

        List<String> ignoreFeignLogPackageNameList = niceLogProperty.getIgnoreFeignLogPackageName();
        if (!CollectionUtils.isEmpty(ignoreFeignLogPackageNameList)) {
            for (String packageName : ignoreFeignLogPackageNameList) {
                if (method.getDeclaringClass().getName().startsWith(packageName.trim())) {
                    return false;
                }
            }
        }

        return true;
    }
}