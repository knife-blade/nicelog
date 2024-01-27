package com.suchtool.betterlog.aspect;

import com.suchtool.betterlog.constant.DirectionTypeEnum;
import com.suchtool.betterlog.constant.LogLevelEnum;
import com.suchtool.betterlog.util.TraceIdUtil;
import com.suchtool.betterlog.util.log.bo.LogBO;
import com.suchtool.betterlog.util.log.context.LogContext;
import com.suchtool.betterlog.util.log.context.LogContextThreadLocal;
import com.suchtool.betterlog.util.log.inner.bo.LogInnerBO;
import com.suchtool.betterlog.util.log.inner.util.LogInnerUtil;
import com.suchtool.betterutil.util.JsonUtil;
import com.suchtool.betterutil.util.ThrowableUtil;
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
            param = JsonUtil.toJson(logAspectProcessor.provideParam(joinPoint.getArgs()));
        } catch (Exception e) {
            LogBO.builder()
                    .exceptionInfo("JSON序列化异常")
                    .exceptionDetail(ThrowableUtil.getStackTrace(e))
                    .build()
                    .error();
        }

        LogInnerBO logInnerBO = new LogInnerBO();
        logInnerBO.setEntry(logAspectProcessor.provideEntry(method));
        logInnerBO.setClassName(logAspectProcessor.provideClassName(method));
        logInnerBO.setClassTag(logAspectProcessor.provideClassTag(method));
        logInnerBO.setMethodName(logAspectProcessor.provideMethodName(method));
        logInnerBO.setMethodTag(logAspectProcessor.provideMethodTag(method));
        // 这里无法获得代码所在行
        // logInnerBO.setCodeLineNumber(null);
        logInnerBO.setLevel(LogLevelEnum.INFO);
        logInnerBO.setDirectionType(DirectionTypeEnum.IN);
        logInnerBO.setType(logAspectProcessor.provideType());
        logInnerBO.setParam(param);

        recordContext(logInnerBO);

        LogInnerUtil.record(logInnerBO);
    }

    public void afterReturning(JoinPoint joinPoint, Object returnValue) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();

        if (!logAspectProcessor.requireProcess(method)) {
            return;
        }

        String response = JsonUtil.toJson(returnValue);

        LogInnerBO logInnerBO = new LogInnerBO();
        logInnerBO.setEntry(logAspectProcessor.provideEntry(method));
        logInnerBO.setClassName(logAspectProcessor.provideClassName(method));
        logInnerBO.setClassTag(logAspectProcessor.provideClassTag(method));
        logInnerBO.setMethodName(logAspectProcessor.provideMethodName(method));
        logInnerBO.setMethodTag(logAspectProcessor.provideMethodTag(method));
        // 这里无法获得代码所在行
        // logInnerBO.setCodeLineNumber(null);
        logInnerBO.setLevel(LogLevelEnum.INFO);
        logInnerBO.setType(logAspectProcessor.provideType());
        logInnerBO.setTypeDetail(logAspectProcessor.provideType().getName() + "返回");
        logInnerBO.setReturnValue(response);

        recordContext(logInnerBO);

        LogInnerUtil.record(logInnerBO);

        logAspectProcessor.returningOrThrowingProcess();

        // 清除，防止内存泄露
        LogContextThreadLocal.clear();
    }

    public void afterThrowing(JoinPoint joinPoint, Throwable throwingValue) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();

        if (!logAspectProcessor.requireProcess(method)) {
            return;
        }

        LogInnerBO logInnerBO = new LogInnerBO();
        logInnerBO.setEntry(logAspectProcessor.provideEntry(method));
        logInnerBO.setClassName(logAspectProcessor.provideClassName(method));
        logInnerBO.setClassTag(logAspectProcessor.provideClassTag(method));
        logInnerBO.setMethodName(logAspectProcessor.provideMethodName(method));
        logInnerBO.setMethodTag(logAspectProcessor.provideMethodTag(method));
        // 这里无法获得代码所在行
        // logInnerBO.setCodeLineNumber(null);
        logInnerBO.setLevel(LogLevelEnum.ERROR);
        logInnerBO.setType(logAspectProcessor.provideType());
        logInnerBO.setTypeDetail(logAspectProcessor.provideType().getName() + "报异常");
        logInnerBO.setExceptionInfo(throwingValue.getMessage());
        logInnerBO.setExceptionDetail(ThrowableUtil.getLastStackTrace(throwingValue, 5));
        // 如果是存到ES，这里可以把所有栈信息都获取到
        // logInnerBO.setExceptionDetail(ThrowableUtil.getStackTrace(throwingValue));

        LogInnerUtil.record(logInnerBO);

        recordContext(logInnerBO);

        logAspectProcessor.returningOrThrowingProcess();

        // 清除，防止内存泄露
        LogContextThreadLocal.clear();
    }

    /**
     * 记录上下文信息
     */
    private void recordContext(LogInnerBO logInnerBO) {
        LogContext logContext = new LogContext();
        logContext.setEntry(logInnerBO.getEntry());
        logContext.setTraceId(TraceIdUtil.createTraceId());
        LogContextThreadLocal.write(logContext);
    }
}
