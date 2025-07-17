package com.suchtool.nicelog.aspect;

import com.suchtool.nicelog.annotation.NiceLog;
import com.suchtool.nicelog.constant.EntryTypeEnum;
import com.suchtool.nicelog.util.log.NiceLogUtil;
import com.suchtool.nicetool.util.base.JsonUtil;
import com.suchtool.nicetool.util.reflect.MethodUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.expression.MethodBasedEvaluationContext;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.core.io.InputStreamSource;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

/**
 * 日志参数提供者
 */
public interface NiceLogParamProvider {
    ExpressionParser PARSER = new SpelExpressionParser();
    ParameterNameDiscoverer NAME_DISCOVERER = new DefaultParameterNameDiscoverer();
    Collection<Class<?>> IGNORE_LOG_CLASS_LIST = Arrays.asList(
            Errors.class,             // 比如：BindingResult.class,
            InputStreamSource.class,  // 比如：MultipartFile
            ServletResponse.class,
            ServletRequest.class
    );

    String provideEntryType();

    default String provideEntry(Method method) {
        return provideClassTag(method);
    }

    default String provideEntryClassTag(Method method) {
        return provideClassTag(method);
    }

    default String provideEntryMethodTag(Method method) {
        return provideMethodTag(method);
    }

    default String provideClassName(Method method) {
        return method.getDeclaringClass().getName();
    }

    /**
     * 先取@NiceLog的value，若没有再从原注解拼接tag
     */
    default String provideClassTag(Method method) {
        String classTag = null;

        Class<?> declaringClass = method.getDeclaringClass();

        if (declaringClass.isAnnotationPresent(NiceLog.class)) {
            NiceLog niceLog = declaringClass.getAnnotation(NiceLog.class);
            classTag = niceLog.value();
        }

        if (!StringUtils.hasText(classTag)) {
            if (EntryTypeEnum.CONTROLLER.name().equals(provideEntryType())
                    && declaringClass.isAnnotationPresent(Api.class)) {
                Api api = declaringClass.getAnnotation(Api.class);
                String[] tags = api.tags();
                String value = api.value();
                String tagJoin = String.join("+", tags);
                if (tags.length > 0) {
                    classTag = tagJoin;
                } else {
                    classTag = value;
                }
            } else if (EntryTypeEnum.FEIGN.name().equals(provideEntryType())) {
                FeignClient feignClient = declaringClass.getAnnotation(FeignClient.class);
                classTag = feignClient.value();
            }
        }

        return classTag;
    }

    default String provideMethodName(Method method) {
        return method.getName();
    }

    /**
     * 先取@NiceLog的value，没有再从其他注解取tag
     */
    default String provideMethodTag(Method method) {
        String methodTag = null;

        if (method.isAnnotationPresent(NiceLog.class)) {
            methodTag = method.getAnnotation(NiceLog.class).value();
        }

        if (!StringUtils.hasText(methodTag)) {
            if (EntryTypeEnum.CONTROLLER.name().equals(provideEntryType())
                    && method.isAnnotationPresent(ApiOperation.class)) {
                methodTag = method.getAnnotation(ApiOperation.class).value();
            }
        }

        return methodTag;
    }

    /**
     * 如果param不为空则取它；否则根据method和args去解析
     */
    default String provideParam(String param, Method method, Object[] args) {
        String finalParam = null;

        if (StringUtils.hasText(param)) {
            return param;
        }

        if (args == null
                || args.length == 0) {
            return null;
        }

        if (args.length == 1) {
            Object arg = args[0];
            if (MethodUtil.requireParse(IGNORE_LOG_CLASS_LIST, arg)) {
                // 单个则直接序列化
                finalParam = JsonUtil.toJsonString(arg);
            }
        } else {
            // 如果是多个，则放到Map，再序列化
            try {
                Map<String, Object> map = MethodUtil.parseParam(method, args, IGNORE_LOG_CLASS_LIST);
                if (!CollectionUtils.isEmpty(map)) {
                    finalParam = JsonUtil.toJsonString(map);
                }
            } catch (Throwable e) {
                NiceLogUtil.createBuilder()
                        .mark("nicelog解析参数失败")
                        .throwable(e)
                        .error();
            }
        }

        return finalParam;
    }

    default String provideBusinessNo(Method method, Object[] args) {
        String businessNo = null;

        NiceLog niceLog = null;
        if (method.isAnnotationPresent(NiceLog.class)) {
            niceLog = method.getAnnotation(NiceLog.class);
        } else {
            Class<?> declaringClass = method.getDeclaringClass();
            if (declaringClass.isAnnotationPresent(NiceLog.class)) {
                niceLog = declaringClass.getAnnotation(NiceLog.class);
            }
        }

        if (niceLog != null) {
            String businessNoSpEL = niceLog.businessNoSpEL();

            if (StringUtils.hasText(businessNoSpEL)) {
                EvaluationContext context = new MethodBasedEvaluationContext(
                        null, method, args, NAME_DISCOVERER);
                businessNo = PARSER.parseExpression(businessNoSpEL).getValue(context, String.class);
            }
        }

        return businessNo;
    }
}
