package com.suchtool.nicelog.aspect.impl;

import com.suchtool.nicelog.aspect.NiceLogLogCommonAspectExecutor;
import com.suchtool.nicelog.aspect.NiceLogAspectProcessor;
import com.suchtool.nicelog.constant.EntryTypeEnum;
import com.suchtool.nicelog.constant.NiceLogPointcutExpression;
import com.suchtool.nicelog.constant.ProcessIgnoreUrl;
import com.suchtool.nicelog.property.NiceLogProperty;
import com.suchtool.nicelog.util.log.context.NiceLogContext;
import com.suchtool.nicelog.util.log.context.NiceLogContextThreadLocal;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * Controller的日志
 */
@Slf4j
@Aspect
public class NiceLogControllerLogAspect extends NiceLogAspectProcessor implements Ordered {
    private final NiceLogLogCommonAspectExecutor niceLogLogCommonAspectExecutor;

    @Autowired
    private NiceLogProperty niceLogProperty;

    private final int order;

    public NiceLogControllerLogAspect(int order) {
        this.niceLogLogCommonAspectExecutor = new NiceLogLogCommonAspectExecutor(this);
        this.order = order;
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
    public boolean requireProcess(Method method) {
        String url = provideEntry(method);
        if (ProcessIgnoreUrl.isInWrapperIgnoreUrl(url)) {
            return false;
        }

        return super.requireProcess(method);
    }

    /**
     * 正常返回或者抛异常的处理
     */
    @Override
    public void returningOrThrowingProcess() {
        ServletRequestAttributes servletRequestAttributes =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (servletRequestAttributes == null) {
            log.warn("ServletRequestAttributes为null，不处理");
            return;
        }
        HttpServletResponse httpServletResponse = servletRequestAttributes.getResponse();

        if (httpServletResponse == null) {
            log.warn("HttpServletResponse为null，不处理");
            return;
        }

        if (niceLogProperty.getEnableControllerTraceIdResponseHeader() != null
            && niceLogProperty.getEnableControllerTraceIdResponseHeader()) {
            NiceLogContext niceLogContext = NiceLogContextThreadLocal.read();
            if (niceLogContext != null) {
                String controllerTraceIdHeader = niceLogProperty.getControllerResponseTraceIdHeader();
                if (StringUtils.hasText(controllerTraceIdHeader)) {
                    // 将traceId返给前端，这样即可通过traceId查到所有日志信息
                    httpServletResponse.addHeader(controllerTraceIdHeader, niceLogContext.getTraceId());
                }
            }
        }
    }

    @Override
    public EntryTypeEnum provideEntryType() {
        return EntryTypeEnum.CONTROLLER;
    }

    @Override
    public String provideEntry(Method method) {
        String url = null;
        ServletRequestAttributes servletRequestAttributes =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (servletRequestAttributes != null) {
            HttpServletRequest request = servletRequestAttributes.getRequest();
            url = request.getRequestURI();
        }

        return url;
    }
}