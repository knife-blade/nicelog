package com.suchtool.nicelog.aspect.impl;

import com.suchtool.nicelog.aspect.NiceLogAspectProcessor;
import com.suchtool.nicelog.aspect.NiceLogLogCommonAspectExecutor;
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
public class NiceLogFeignLogAspect extends NiceLogAspectProcessor implements Ordered {
    private final NiceLogLogCommonAspectExecutor niceLogLogCommonAspectExecutor;

    private final int order;

    @Autowired
    private StandardEnvironment standardEnvironment;

    public NiceLogFeignLogAspect(int order) {
        this.niceLogLogCommonAspectExecutor = new NiceLogLogCommonAspectExecutor(this);
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

    /**
     * 正常返回或者抛异常的处理
     */
    @Override
    public void returningOrThrowingProcess() {

    }

    @Override
    public String provideEntryType() {
        return EntryTypeEnum.FEIGN.name();
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