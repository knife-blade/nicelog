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

public class LogAspectExecutor {
    private final LogAspectProcessor logAspectProcessor;

    public LogAspectExecutor(LogAspectProcessor logAspectProcessor) {
        this.logAspectProcessor = logAspectProcessor;
    }

    public void before(JoinPoint joinPoint) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();

        if (!logAspectProcessor.requireProcess(method)) {
            return;
        }

        String param = null;
        try {
            Object provideParam = logAspectProcessor.provideParam(joinPoint.getArgs(), method);
            if (provideParam != null) {
                param = JsonUtil.toJsonString(provideParam);
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
        logInnerBO.setParam(param);

        recordContext(logInnerBO);

        NiceLogInnerUtil.record(logInnerBO);
    }

    public void afterReturning(JoinPoint joinPoint, Object returnValue) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();

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
        logInnerBO.setEntry(logAspectProcessor.provideEntry(method));
        logInnerBO.setEntryClassTag(logAspectProcessor.provideEntryClassTag(method));
        logInnerBO.setEntryMethodTag(logAspectProcessor.provideEntryMethodTag(method));
        logInnerBO.setClassName(logAspectProcessor.provideClassName(method));
        logInnerBO.setClassTag(logAspectProcessor.provideClassTag(method));
        logInnerBO.setMethodName(logAspectProcessor.provideMethodName(method));
        logInnerBO.setMethodTag(logAspectProcessor.provideMethodTag(method));
        logInnerBO.setMethodDetail(MethodUtil.parseMethodDetail(method));
        logInnerBO.setEntryType(logAspectProcessor.provideEntryType());
        logInnerBO.setIp(IpUtil.parseIP());
        logInnerBO.setClientIp(IpUtil.parseClientIP());
    }
}
