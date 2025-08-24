package com.suchtool.nicelog.aspect.impl;

import com.suchtool.nicelog.aspect.NiceLogLogCommonAspectExecutor;
import com.suchtool.nicelog.aspect.NiceLogAspectProcessor;
import com.suchtool.nicelog.constant.EntryTypeEnum;
import com.suchtool.nicelog.constant.NiceLogPointcutExpression;
import com.suchtool.nicelog.constant.ProcessIgnoreUrl;
import com.suchtool.nicelog.property.NiceLogProperty;
import com.suchtool.nicetool.util.base.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.core.Ordered;
import org.springframework.util.CollectionUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.*;

/**
 * Controller的日志
 */
@Slf4j
@Aspect
public class NiceLogControllerLogAspect extends NiceLogAspectProcessor implements Ordered {
    private final NiceLogLogCommonAspectExecutor niceLogLogCommonAspectExecutor;

    private final int order;

    private final NiceLogProperty niceLogProperty;

    public NiceLogControllerLogAspect(int order, NiceLogProperty niceLogProperty) {
        this.niceLogLogCommonAspectExecutor =
                new NiceLogLogCommonAspectExecutor(this, niceLogProperty);
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

    }

    @Override
    public String provideEntryType() {
        return EntryTypeEnum.CONTROLLER.name();
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

    @Override
    public String provideRequestHeader() {
        ServletRequestAttributes servletRequestAttributes =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (servletRequestAttributes == null) {
            return null;
        }
        HttpServletRequest request = servletRequestAttributes.getRequest();
        return JsonUtil.toJsonString(buildRequestHeaders(request));
    }

    @Override
    public String provideResponseHeader() {
        ServletRequestAttributes servletRequestAttributes =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (servletRequestAttributes == null) {
            return null;
        }
        HttpServletResponse response = servletRequestAttributes.getResponse();
        return JsonUtil.toJsonString(buildResponseHeaders(response));
    }

    private Map<String, String> buildRequestHeaders(HttpServletRequest request) {
        Map<String, String> headersMap = new HashMap<>();

        // 组装请求头
        Enumeration<String> requestHeaderNames = request.getHeaderNames();
        while (requestHeaderNames.hasMoreElements()) {
            String headerName = requestHeaderNames.nextElement();
            Enumeration<String> headerValues = request.getHeaders(headerName);
            StringJoiner headerValuesJoiner = new StringJoiner(niceLogProperty.getControllerHeaderSeparator());
            while (headerValues.hasMoreElements()) {
                headerValuesJoiner.add(headerValues.nextElement());
            }
            headersMap.put(headerName, headerValuesJoiner.toString());
        }

        return headersMap;
    }

    private Map<String, String> buildResponseHeaders(HttpServletResponse response) {
        // 创建一个Map来存储请求和响应头
        Map<String, String> headersMap = new HashMap<>();

        Collection<String> headerNames = response.getHeaderNames();
        if (!CollectionUtils.isEmpty(headerNames)) {
            for (String headerName : headerNames) {
                Collection<String> headerValues = response.getHeaders(headerName);
                StringJoiner headerValuesJoiner = new StringJoiner(niceLogProperty.getControllerHeaderSeparator());
                if (!CollectionUtils.isEmpty(headerValues)) {
                    for (String headerValue : headerValues) {
                        headerValuesJoiner.add(headerValue);
                    }
                    headersMap.put(headerName, headerValuesJoiner.toString());
                }
            }
        }

        return headersMap;
    }
}