package com.suchtool.nicelog.aspect.impl;

import com.suchtool.nicelog.aspect.NiceLogAbstractAspect;
import com.suchtool.nicelog.aspect.NiceLogAspectExecutor;
import com.suchtool.nicelog.aspect.provider.NiceLogParamProvider;
import com.suchtool.nicelog.aspect.provider.impl.controller.NiceLogControllerParamProvider;
import com.suchtool.nicelog.aspect.provider.impl.controller.NiceLogControllerParamProviderSwagger2;
import com.suchtool.nicelog.aspect.provider.impl.controller.NiceLogControllerParamProviderSwagger3;
import com.suchtool.nicelog.constant.NiceLogPointcutExpression;
import com.suchtool.nicelog.property.NiceLogProperty;
import com.suchtool.nicelog.util.servlet.NiceLogServletUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.core.Ordered;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.ClassUtils;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Method;

/**
 * Controller的日志
 */
@Slf4j
@Aspect
public class NiceLogControllerLogAspect extends NiceLogAbstractAspect implements Ordered {
    private final NiceLogAspectExecutor niceLogAspectExecutor;

    private final int order;

    private final NiceLogProperty niceLogProperty;

    private final NiceLogParamProvider niceLogParamProvider;

    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    public NiceLogControllerLogAspect(int order,
                                      NiceLogProperty niceLogProperty,
                                      NiceLogServletUtil niceLogServletUtil
    ) {
        if (ClassUtils.isPresent("io.swagger.annotations.Tag", getClass().getClassLoader())) {
            niceLogParamProvider = new NiceLogControllerParamProviderSwagger2(niceLogProperty, niceLogServletUtil);
        } else if (ClassUtils.isPresent("io.swagger.v3.oas.annotations.tags.Tag", getClass().getClassLoader())) {
            niceLogParamProvider = new NiceLogControllerParamProviderSwagger3(niceLogProperty, niceLogServletUtil);
        } else {
            niceLogParamProvider = new NiceLogControllerParamProvider(niceLogProperty, niceLogServletUtil);
        }
        this.niceLogAspectExecutor = new NiceLogAspectExecutor(
                this,
                niceLogParamProvider,
                niceLogProperty);
        this.order = order;
        this.niceLogProperty = niceLogProperty;
    }

    @Override
    public int getOrder() {
        return order;
    }

    @Override
    public String pointcutExpression() {
        return NiceLogPointcutExpression.CONTROLLER_LOG_ASPECT;
    }

    @Pointcut(NiceLogPointcutExpression.CONTROLLER_LOG_ASPECT
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
        String url = niceLogParamProvider.provideEntry(method);

        if (!CollectionUtils.isEmpty(niceLogProperty.getIgnoreLogUrls())) {
            for (String s : niceLogProperty.getIgnoreLogUrls()) {
                if (pathMatcher.match(s, url)) {
                    return false;
                }
            }
        }

        return super.requireProcess(method);
    }
}