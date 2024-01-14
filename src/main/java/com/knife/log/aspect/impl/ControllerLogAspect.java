package com.knife.log.aspect.impl;

import com.knife.log.aspect.LogAspectExecutor;
import com.knife.log.aspect.LogAspectProcessor;
import com.knife.log.constant.AspectTypeEnum;
import com.knife.log.util.log.context.LogContextThreadLocal;
import com.knife.util.constant.ProcessIgnoreUrl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * Controller的日志
 */
@Aspect
@Component
public class ControllerLogAspect implements LogAspectProcessor {
    private final LogAspectExecutor logAspectExecutor;

    public ControllerLogAspect() {
        logAspectExecutor = new LogAspectExecutor(this);
    }

    @Pointcut("@within(org.springframework.web.bind.annotation.RestController)")
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
    public boolean requireProcess() {
        String url = provideEntry();
        return !ProcessIgnoreUrl.isInWrapperIgnoreUrl(url);
    }

    /**
     * 正常返回或者抛异常的处理
     */
    @Override
    public void returningOrThrowingProcess() {
        ServletRequestAttributes servletRequestAttributes =
                (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
        Assert.notNull(servletRequestAttributes, "RequestAttributes不能为null");
        HttpServletResponse response = servletRequestAttributes.getResponse();

        // 将traceId返给前端，这样即可通过traceId查到所有日志信息
        response.addHeader("traceId", LogContextThreadLocal.read().getTraceId());
    }

    @Override
    public AspectTypeEnum provideType() {
        return AspectTypeEnum.CONTROLLER;
    }

    @Override
    public String provideClassTag(Method method) {
        String classTag = null;

        Class<?> declaringClass = method.getDeclaringClass();

        if (declaringClass.isAnnotationPresent(Api.class)) {
            Api api = declaringClass.getAnnotation(Api.class);
            String[] tags = api.tags();
            String value = api.value();
            String tagJoin = String.join("+", tags);
            if (tags.length > 0) {
                classTag = tagJoin;
            } else {
                classTag = value;
            }
        }

        return classTag;
    }

    @Override
    public String provideMethodTag(Method method) {
        String methodTag = null;

        if (method.isAnnotationPresent(ApiOperation.class)) {
            methodTag = method.getAnnotation(ApiOperation.class).value();
        }

        return methodTag;
    }

    @Override
    public String provideEntry() {
        String url = null;
        ServletRequestAttributes servletRequestAttributes =
                (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
        if (servletRequestAttributes != null) {
            HttpServletRequest request = servletRequestAttributes.getRequest();
            url = request.getRequestURI();
        }

        return url;
    }
}