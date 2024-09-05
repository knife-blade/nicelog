package com.suchtool.nicelog.aspect;

import com.suchtool.nicelog.constant.DirectionTypeEnum;
import com.suchtool.nicelog.constant.EntryTypeEnum;
import com.suchtool.nicelog.constant.LogLevelEnum;
import com.suchtool.nicelog.util.NiceLogTraceIdUtil;
import com.suchtool.nicelog.util.log.NiceLogUtil;
import com.suchtool.nicelog.util.log.context.NiceLogContext;
import com.suchtool.nicelog.util.log.context.NiceLogContextThreadLocal;
import com.suchtool.nicelog.util.log.context.feign.NiceLogFeignContext;
import com.suchtool.nicelog.util.log.context.feign.NiceLogFeignContextThreadLocal;
import com.suchtool.nicelog.util.log.inner.bo.NiceLogInnerBO;
import com.suchtool.nicelog.util.log.inner.util.NiceLogInnerUtil;
import com.suchtool.nicetool.util.base.JsonUtil;
import com.suchtool.nicetool.util.reflect.MethodUtil;
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

        if (!logAspectProcessor.requireProcess(method)) {
            return;
        }

        String param = null;
        try {
            param = logAspectProcessor.provideParam(null, method, args);
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

        String returnValueString = null;
        if (returnValue != null) {
            try {
                returnValueString = JsonUtil.toJsonString(returnValue);
            } catch (Throwable e) {
                NiceLogUtil.createBuilder()
                        .mark("nicelog将返回值序列化为json失败")
                        .throwable(e)
                        .error();
            }
        }
        logInnerBO.setReturnValue(returnValueString);

        NiceLogInnerUtil.record(logInnerBO);

        logAspectProcessor.returningOrThrowingProcess();

        // 清除，防止内存泄露
        checkAndClearContext();
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
        if (EntryTypeEnum.FEIGN.equals(logAspectProcessor.provideEntryType())) {
            NiceLogFeignContext niceLogFeignContext = new NiceLogFeignContext();
            NiceLogFeignContextThreadLocal.write(niceLogFeignContext);
        } else {
            NiceLogContext niceLogContext = NiceLogContextThreadLocal.read();
            if (niceLogContext == null) {
                niceLogContext = new NiceLogContext();
                niceLogContext.setTraceId(NiceLogTraceIdUtil.readTraceId());
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
        logInnerBO.setEntryType(logAspectProcessor.provideEntryType());
        logInnerBO.setEntry(logAspectProcessor.provideEntry(method));
        logInnerBO.setEntryClassTag(logAspectProcessor.provideEntryClassTag(method));
        logInnerBO.setEntryMethodTag(logAspectProcessor.provideEntryMethodTag(method));
        logInnerBO.setClassName(logAspectProcessor.provideClassName(method));
        logInnerBO.setClassTag(logAspectProcessor.provideClassTag(method));
        logInnerBO.setMethodName(logAspectProcessor.provideMethodName(method));
        logInnerBO.setMethodTag(logAspectProcessor.provideMethodTag(method));
        logInnerBO.setMethodDetail(MethodUtil.parseMethodDetail(method));
    }
}
