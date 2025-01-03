package com.suchtool.nicelog.aspect.impl;

import com.suchtool.nicelog.aspect.LogAspectProcessor;
import com.suchtool.nicelog.aspect.LogCommonAspectExecutor;
import com.suchtool.nicelog.constant.EntryTypeEnum;
import com.suchtool.nicelog.constant.NiceLogPointcutExpression;
import com.suchtool.nicetool.util.web.http.url.HttpUrlUtil;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.web.bind.annotation.RequestMapping;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

/**
 * Feign的日志
 */
@Aspect
public class FeignLogAspect extends LogAspectProcessor implements Ordered {
    private final LogCommonAspectExecutor logCommonAspectExecutor;

    private final int order;

    @Autowired
    private StandardEnvironment standardEnvironment;

    public FeignLogAspect(int order) {
        this.logCommonAspectExecutor = new LogCommonAspectExecutor(this);
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
            + " && !" + NiceLogPointcutExpression.NICE_LOG_ANNOTATION_ASPECT + ")")
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
        return EntryTypeEnum.FEIGN;
    }

    @Override
    public String provideEntry(Method method) {
        String finalUrl = null;

        Class<?> cls = method.getDeclaringClass();
        FeignClient feignClient = cls.getAnnotation(FeignClient.class);
        String url = standardEnvironment.resolvePlaceholders(feignClient.url());
        String path = standardEnvironment.resolvePlaceholders(feignClient.path());

        String urlPrefix = null;
        RequestMapping requestMapping = AnnotatedElementUtils.getMergedAnnotation(method, RequestMapping.class);
        if (requestMapping != null) {
            String[] value = requestMapping.value();
            urlPrefix = value[0];
        }

        List<String> urlList = Arrays.asList(url, path, urlPrefix);
        finalUrl = HttpUrlUtil.joinUrl(urlList, false);

        return finalUrl;
    }
}