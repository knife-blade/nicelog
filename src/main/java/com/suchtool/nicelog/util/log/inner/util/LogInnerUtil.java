package com.suchtool.nicelog.util.log.inner.util;

import com.suchtool.nicelog.constant.AspectTypeEnum;
import com.suchtool.nicelog.process.NiceLogProcess;
import com.suchtool.nicelog.util.log.context.LogContext;
import com.suchtool.nicelog.util.log.context.LogContextThreadLocal;
import com.suchtool.nicelog.util.log.inner.bo.LogInnerBO;
import com.suchtool.niceutil.util.spring.ApplicationContextHolder;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
public class LogInnerUtil {
    public static void record(LogInnerBO logInnerBO) {
        logInnerBO.setAppName(ApplicationContextHolder.getContext().getEnvironment()
                .getProperty("spring.application.name", ""));

        fillCommonField(logInnerBO);

        NiceLogProcess niceLogProcess = ApplicationContextHolder.getContext()
                .getBean(NiceLogProcess.class);
        niceLogProcess.process(logInnerBO);
    }

    /**
     * 填充公共字段
     */
    private static void fillCommonField(LogInnerBO logInnerBO) {
        LogContext logContext = LogContextThreadLocal.read();
        if (logContext != null) {
            logInnerBO.setTraceId(logContext.getTraceId());
            logInnerBO.setEntry(logContext.getEntry());
            logInnerBO.setEntryClassTag(logContext.getEntryClassTag());
            logInnerBO.setEntryMethodTag(logContext.getEntryMethodTag());
        }

        // 通过堆栈获得调用方的类名、方法名、代码行号
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        StackTraceElement stackTraceElement = stackTrace[6];

        if (AspectTypeEnum.MANUAL.equals(logInnerBO.getAspectType())) {
            logInnerBO.setClassName(stackTraceElement.getClassName());
            logInnerBO.setMethodName(stackTraceElement.getMethodName());
            logInnerBO.setCodeLineNumber(String.valueOf(stackTraceElement.getLineNumber()));
        }

        logInnerBO.setLogTime(LocalDateTime.now().format(
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
    }
}
