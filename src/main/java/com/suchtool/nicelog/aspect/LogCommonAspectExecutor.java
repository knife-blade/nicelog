package com.suchtool.nicelog.aspect;

import com.suchtool.nicelog.constant.DirectionTypeEnum;
import com.suchtool.nicelog.constant.LogLevelEnum;
import com.suchtool.nicelog.util.NiceLogTraceIdUtil;
import com.suchtool.nicelog.util.log.NiceLogUtil;
import com.suchtool.nicelog.util.log.context.NiceLogContext;
import com.suchtool.nicelog.util.log.context.NiceLogContextThreadLocal;
import com.suchtool.nicelog.util.log.inner.bo.NiceLogInnerBO;
import com.suchtool.nicelog.util.log.inner.util.NiceLogInnerUtil;
import com.suchtool.niceutil.util.base.JsonUtil;
import com.suchtool.niceutil.util.reflect.MethodUtil;
import com.suchtool.niceutil.util.web.ip.IpUtil;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Method;

public class LogCommonAspectExecutor {
    private final LogAspectProcessor logAspectProcessor;

    public LogCommonAspectExecutor(LogAspectProcessor logAspectProcessor) {
        this.logAspectProcessor = logAspectProcessor;
    }

    public void before(JoinPoint joinPoint) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();

        Object[] args = joinPoint.getArgs();

        before(method, args, null);
    }

    public void before(Method method, Object[] args, String param) {
        if (!logAspectProcessor.requireProcess(method)) {
            return;
        }

        // 如果param不为空就取它，否则根据method和args拼接
        String finalParam = null;
        try {
            Object provideParam = logAspectProcessor.provideParam(param, method, args);
            if (provideParam != null) {
                finalParam = JsonUtil.toJsonString(provideParam);
            }
        } catch (Throwable t) {
            NiceLogUtil.createBuilder()
                    .errorInfo("参数转JSON字符串异常")
                    .throwable(t)
                    .error();
        }

        NiceLogInnerBO logInnerBO = new NiceLogInnerBO();
        fillCommonField(logInnerBO, method);

        // 这里无法获得代码所在行
        // logInnerBO.setCodeLineNumber(null);
        logInnerBO.setLevel(LogLevelEnum.INFO);
        logInnerBO.setDirectionType(DirectionTypeEnum.IN);
        logInnerBO.setParam(finalParam);

        recordContext(logInnerBO);

        NiceLogInnerUtil.record(logInnerBO);
    }

    public void afterReturning(JoinPoint joinPoint, Object returnValue) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();

        afterReturning(method, returnValue);
    }

    public void afterReturning(Method method,
                               Object returnValue) {
        if (!logAspectProcessor.requireProcess(method)) {
            return;
        }

        NiceLogInnerBO logInnerBO = new NiceLogInnerBO();
        fillCommonField(logInnerBO, method);
        // 这里无法获得代码所在行
        // logInnerBO.setCodeLineNumber(null);
        logInnerBO.setLevel(LogLevelEnum.INFO);
        logInnerBO.setDirectionType(DirectionTypeEnum.OUT);
        logInnerBO.setReturnValue(JsonUtil.toJsonString(returnValue));

        NiceLogInnerUtil.record(logInnerBO);

        logAspectProcessor.returningOrThrowingProcess();

        // 清除，防止内存泄露
        NiceLogContextThreadLocal.clear();
    }

    public void afterThrowing(JoinPoint joinPoint, Throwable throwable) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        afterThrowing(method, throwable);
    }

    public void afterThrowing(Method method, Throwable throwable) {
        if (!logAspectProcessor.requireProcess(method)) {
            return;
        }

        NiceLogInnerBO logInnerBO = new NiceLogInnerBO();
        fillCommonField(logInnerBO, method);
        // 这里无法获得代码所在行
        // logInnerBO.setCodeLineNumber(null);
        logInnerBO.setLevel(LogLevelEnum.ERROR);
        logInnerBO.setEntryType(logAspectProcessor.provideEntryType());
        logInnerBO.setThrowable(throwable);

        NiceLogInnerUtil.record(logInnerBO);

        logAspectProcessor.returningOrThrowingProcess();

        // 清除，防止内存泄露
        NiceLogContextThreadLocal.clear();
    }

    /**
     * 记录上下文信息
     */
    private void recordContext(NiceLogInnerBO logInnerBO) {
        NiceLogContext niceLogContext = new NiceLogContext();
        niceLogContext.setTraceId(NiceLogTraceIdUtil.readTraceId());
        niceLogContext.setEntry(logInnerBO.getEntry());
        niceLogContext.setEntryClassTag(logInnerBO.getEntryClassTag());
        niceLogContext.setEntryMethodTag(logInnerBO.getEntryMethodTag());
        NiceLogContextThreadLocal.write(niceLogContext);
    }

    private void fillCommonField(NiceLogInnerBO logInnerBO,
                                 Method method) {
        logInnerBO.setEntryType(logAspectProcessor.provideEntryType());
        logInnerBO.setEntry(logAspectProcessor.provideEntry(method));
        logInnerBO.setEntryClassTag(logAspectProcessor.provideEntryClassTag(method));
        logInnerBO.setEntryMethodTag(logAspectProcessor.provideEntryMethodTag(method));
        logInnerBO.setClassName(logAspectProcessor.provideClassName(method));
        logInnerBO.setClassTag(logAspectProcessor.provideClassTag(method));
        logInnerBO.setMethodName(logAspectProcessor.provideMethodName(method));
        logInnerBO.setMethodTag(logAspectProcessor.provideMethodTag(method));
        logInnerBO.setMethodDetail(MethodUtil.parseMethodDetail(method));
        logInnerBO.setIp(IpUtil.parseIP());
        logInnerBO.setClientIp(IpUtil.parseClientIP());
    }
}
