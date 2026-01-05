package com.suchtool.nicelog.aspect.provider;

import com.suchtool.nicelog.annotation.NiceLog;
import com.suchtool.nicelog.annotation.NiceLogIgnoreData;
import com.suchtool.nicelog.property.NiceLogProperty;
import com.suchtool.nicelog.util.log.NiceLogUtil;
import com.suchtool.nicetool.util.base.JsonUtil;
import com.suchtool.nicetool.util.reflect.MethodUtil;
import org.springframework.context.expression.MethodBasedEvaluationContext;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * 日志参数提供者
 */
public abstract class NiceLogParamProvider {
    ExpressionParser PARSER = new SpelExpressionParser();
    ParameterNameDiscoverer NAME_DISCOVERER = new DefaultParameterNameDiscoverer();

    private final NiceLogProperty niceLogProperty;

    public NiceLogParamProvider(NiceLogProperty niceLogProperty) {
        this.niceLogProperty = niceLogProperty;
    }

    public abstract String provideEntryType();

    public String provideEntry(Method method) {
        return provideClassTag(method);
    }

    public String provideEntryClassTag(Method method) {
        return provideClassTag(method);
    }

    public String provideEntryMethodTag(Method method) {
        return provideMethodTag(method);
    }

    public String provideClassName(Method method) {
        return method.getDeclaringClass().getName();
    }

    /**
     * 先取@NiceLog的value，若没有再从原注解拼接tag
     */
    public String provideClassTag(Method method) {
        String classTag = null;

        Class<?> declaringClass = method.getDeclaringClass();

        if (declaringClass.isAnnotationPresent(NiceLog.class)) {
            NiceLog niceLog = declaringClass.getAnnotation(NiceLog.class);
            classTag = niceLog.value();
        }

        return classTag;
    }

    public String provideMethodName(Method method) {
        return method.getName();
    }

    /**
     * 先取@NiceLog的value，没有再从其他注解取tag
     */
    public String provideMethodTag(Method method) {
        String methodTag = null;

        if (method.isAnnotationPresent(NiceLog.class)) {
            methodTag = method.getAnnotation(NiceLog.class).value();
        }

        return methodTag;
    }

    /**
     * 如果param不为空则取它；否则根据method和args去解析
     */
    public String provideParam(String param, Method method, Object[] args) {
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
            if (MethodUtil.requireParseForClassName(niceLogProperty.getIgnoreClassNames(), arg)) {
                // 单个则直接序列化
                finalParam = JsonUtil.toJsonString(arg);
            }
        } else {
            // 如果是多个，则放到Map，再序列化
            try {
                Map<String, Object> map = MethodUtil.parseParamForClassName(method, args, niceLogProperty.getIgnoreClassNames());
                if (!CollectionUtils.isEmpty(map)) {
                    finalParam = JsonUtil.toJsonString(map);
                }
            } catch (Throwable t) {
                NiceLogUtil.createBuilder()
                        .mark("nicelog解析参数失败")
                        .throwable(t)
                        .error();
            }
        }

        return finalParam;
    }

    public String provideReturnValue(Method method, Object returnValue) {
        String returnValueString = null;

        if (returnValue != null) {
            Class<?> declaringClass = method.getDeclaringClass();
            if (!MethodUtil.requireParseForClassName(niceLogProperty.getIgnoreClassNames(), returnValue)) {
                return returnValueString;
            }

            NiceLogIgnoreData classIgnoreData = declaringClass.getAnnotation(NiceLogIgnoreData.class);
            NiceLogIgnoreData methodIgnoreData = method.getAnnotation(NiceLogIgnoreData.class);
            if ((classIgnoreData == null || !classIgnoreData.ignoreReturnValue())
                    && (methodIgnoreData == null || !methodIgnoreData.ignoreReturnValue())) {
                try {
                    returnValueString = JsonUtil.toJsonString(returnValue);
                } catch (Throwable t) {
                    NiceLogUtil.createBuilder()
                            .mark("nicelog将返回值序列化为json失败")
                            .throwable(t)
                            .error();
                }
            }
        }

        return returnValueString;
    }

    public String provideBusinessNo(Method method, Object[] args) {
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

    public String provideRequestHeader() {
        return null;
    }

    public String provideResponseHeader() {
        return null;
    }
}
