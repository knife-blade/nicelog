package com.suchtool.nicelog.aspect;

import com.suchtool.nicelog.annotation.NiceLogIgnoreData;
import com.suchtool.nicelog.aspect.provider.NiceLogParamProvider;
import com.suchtool.nicelog.constant.NiceLogDirectionTypeEnum;
import com.suchtool.nicelog.constant.NiceLogEntryTypeEnum;
import com.suchtool.nicelog.constant.NiceLogLogLevelEnum;
import com.suchtool.nicelog.property.NiceLogProperty;
import com.suchtool.nicelog.util.NiceLogTraceIdUtil;
import com.suchtool.nicelog.util.log.NiceLogUtil;
import com.suchtool.nicelog.util.log.context.NiceLogContext;
import com.suchtool.nicelog.util.log.context.NiceLogContextThreadLocal;
import com.suchtool.nicelog.util.log.context.feign.NiceLogFeignContext;
import com.suchtool.nicelog.util.log.context.feign.NiceLogFeignContextThreadLocal;
import com.suchtool.nicelog.util.log.inner.bo.NiceLogInnerBO;
import com.suchtool.nicelog.util.log.inner.util.NiceLogInnerUtil;
import com.suchtool.nicetool.util.reflect.MethodUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Method;

@Slf4j
public class NiceLogAspectExecutor {
    private final NiceLogAbstractAspect logAspectProcessor;

    private final NiceLogParamProvider niceLogParamProvider;

    private final NiceLogProperty niceLogProperty;

    public NiceLogAspectExecutor(NiceLogAbstractAspect logAspectProcessor,
                                 NiceLogParamProvider niceLogParamProvider,
                                 NiceLogProperty niceLogProperty) {
        this.logAspectProcessor = logAspectProcessor;
        this.niceLogParamProvider = niceLogParamProvider;
        this.niceLogProperty = niceLogProperty;
    }

    public void before(JoinPoint joinPoint) {
        try {
            doBefore(joinPoint);
        } catch (Throwable t) {
            log.error("NiceLog的before异常", t);
        }
    }

    private void doBefore(JoinPoint joinPoint) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();

        Object[] args = joinPoint.getArgs();

        if (!logAspectProcessor.requireProcess(method)) {
            return;
        }

        String param = null;
        String businessNo = null;
        String requestHeader = null;

        if (requireRecord(method, true)) {
            try {
                param = niceLogParamProvider.provideParam(null, method, args);
                businessNo = niceLogParamProvider.provideBusinessNo(method, args);
            } catch (Throwable t) {
                NiceLogUtil.createBuilder()
                        .errorInfo("日志获取参数异常")
                        .throwable(t)
                        .error();
            }
        }

        if (Boolean.TRUE.equals(niceLogProperty.getEnableControllerHeaderLog())) {
            requestHeader = niceLogParamProvider.provideRequestHeader();
        }

        NiceLogInnerBO logInnerBO = new NiceLogInnerBO();
        fillCommonField(logInnerBO, method);

        // 这里无法获得代码所在行
        // logInnerBO.setCodeLineNumber(null);
        logInnerBO.setLevel(NiceLogLogLevelEnum.INFO);
        logInnerBO.setDirectionType(NiceLogDirectionTypeEnum.IN);
        logInnerBO.setParam(param);
        logInnerBO.setBusinessNo(businessNo);
        logInnerBO.setRequestHeader(requestHeader);

        recordContext(logInnerBO);

        NiceLogInnerUtil.record(logInnerBO);
    }

    public void afterReturning(JoinPoint joinPoint, Object returnValue) {
        try {
            doAfterReturning(joinPoint, returnValue);
        } catch (Throwable t) {
            log.error("NiceLog的afterReturning异常", t);
        }
    }

    private void doAfterReturning(JoinPoint joinPoint, Object returnValue) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();

        if (!logAspectProcessor.requireProcess(method)) {
            return;
        }

        NiceLogInnerBO logInnerBO = new NiceLogInnerBO();
        fillCommonField(logInnerBO, method);
        // 这里无法获得代码所在行
        // logInnerBO.setCodeLineNumber(null);
        logInnerBO.setLevel(NiceLogLogLevelEnum.INFO);
        logInnerBO.setDirectionType(NiceLogDirectionTypeEnum.OUT);

        if (requireRecord(method, false)) {
            logInnerBO.setReturnValue(niceLogParamProvider.provideReturnValue(method, returnValue));
        }

        if (Boolean.TRUE.equals(niceLogProperty.getEnableControllerHeaderLog())) {
            logInnerBO.setResponseHeader(niceLogParamProvider.provideResponseHeader());
        }

        NiceLogInnerUtil.record(logInnerBO);

        logAspectProcessor.returningOrThrowingProcess();

        // 清除，防止内存泄露
        checkAndClearContext();
    }


    public void afterThrowing(JoinPoint joinPoint, Throwable throwable) {
        try {
            doAfterThrowing(joinPoint, throwable);
        } catch (Throwable t) {
            log.error("NiceLog的afterThrowing异常", t);
        }
    }

    private void doAfterThrowing(JoinPoint joinPoint, Throwable throwable) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();

        if (!logAspectProcessor.requireProcess(method)) {
            return;
        }

        NiceLogInnerBO logInnerBO = new NiceLogInnerBO();
        fillCommonField(logInnerBO, method);
        // 这里无法获得代码所在行
        // logInnerBO.setCodeLineNumber(null);
        logInnerBO.setLevel(NiceLogLogLevelEnum.ERROR);
        logInnerBO.setEntryType(niceLogParamProvider.provideEntryType());
        logInnerBO.setThrowable(throwable);

        if (Boolean.TRUE.equals(niceLogProperty.getEnableControllerHeaderLog())) {
            logInnerBO.setResponseHeader(niceLogParamProvider.provideResponseHeader());
        }

        NiceLogInnerUtil.record(logInnerBO);

        logAspectProcessor.returningOrThrowingProcess();

        // 清除，防止内存泄露
        checkAndClearContext();
    }

    private void checkAndClearContext() {
        NiceLogContext context = NiceLogContextThreadLocal.read();
        if (context == null) {
            return;
        }

        int entryCount = context.getEntryCount();
        entryCount--;
        if (entryCount <= 0) {
            NiceLogContextThreadLocal.clear();
        } else {
            context.setEntryCount(entryCount);
        }
    }

    /**
     * 记录上下文信息
     */
    private void recordContext(NiceLogInnerBO logInnerBO) {
        if (NiceLogEntryTypeEnum.FEIGN.name().equals(niceLogParamProvider.provideEntryType())) {
            NiceLogFeignContext niceLogFeignContext = new NiceLogFeignContext();
            NiceLogFeignContextThreadLocal.write(niceLogFeignContext);
        } else {
            NiceLogContext niceLogContext = NiceLogContextThreadLocal.read();
            if (niceLogContext == null) {
                niceLogContext = new NiceLogContext();
                niceLogContext.setTraceId(NiceLogTraceIdUtil.readTraceId());
                niceLogContext.setEntry(logInnerBO.getEntry());
                niceLogContext.setEntryClassTag(logInnerBO.getEntryClassTag());
                niceLogContext.setEntryMethodTag(logInnerBO.getEntryMethodTag());
                niceLogContext.setEntryCount(1);
                NiceLogContextThreadLocal.write(niceLogContext);
            } else {
                int entryCount = niceLogContext.getEntryCount();
                entryCount++;
                niceLogContext.setEntryCount(entryCount);
            }
        }
    }

    private void fillCommonField(NiceLogInnerBO logInnerBO,
                                 Method method) {
        logInnerBO.setEntryType(niceLogParamProvider.provideEntryType());
        logInnerBO.setEntry(niceLogParamProvider.provideEntry(method));
        logInnerBO.setEntryClassTag(niceLogParamProvider.provideEntryClassTag(method));
        logInnerBO.setEntryMethodTag(niceLogParamProvider.provideEntryMethodTag(method));
        logInnerBO.setClassName(niceLogParamProvider.provideClassName(method));
        logInnerBO.setClassTag(niceLogParamProvider.provideClassTag(method));
        logInnerBO.setMethodName(niceLogParamProvider.provideMethodName(method));
        logInnerBO.setMethodTag(niceLogParamProvider.provideMethodTag(method));
        logInnerBO.setMethodDetail(MethodUtil.parseMethodDetail(method));
    }

    private boolean requireRecord(Method method, boolean isParam) {
        Class<?> declaringClass = method.getDeclaringClass();

        NiceLogIgnoreData classIgnoreData = declaringClass.getAnnotation(NiceLogIgnoreData.class);
        NiceLogIgnoreData methodIgnoreData = method.getAnnotation(NiceLogIgnoreData.class);

        if (isParam) {
            return (classIgnoreData == null || !classIgnoreData.ignoreParam())
                    && (methodIgnoreData == null || !methodIgnoreData.ignoreParam());
        } else {
            return (classIgnoreData == null || !classIgnoreData.ignoreReturnValue())
                    && (methodIgnoreData == null || !methodIgnoreData.ignoreReturnValue());
        }
    }
}
