package com.suchtool.nicelog.util.log.inner.util;

import com.suchtool.nicelog.constant.EntryTypeEnum;
import com.suchtool.nicelog.process.NiceLogProcess;
import com.suchtool.nicelog.util.log.context.NiceLogContext;
import com.suchtool.nicelog.util.log.context.NiceLogContextThreadLocal;
import com.suchtool.nicelog.util.log.inner.bo.NiceLogInnerBO;
import com.suchtool.niceutil.util.spring.ApplicationContextHolder;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
public class NiceLogInnerUtil {
    public static void record(NiceLogInnerBO logInnerBO) {
        fillCommonField(logInnerBO);

        NiceLogProcess niceLogProcess = ApplicationContextHolder.getContext()
                .getBean(NiceLogProcess.class);
        niceLogProcess.process(logInnerBO);
    }

    /**
     * 填充公共字段
     */
    private static void fillCommonField(NiceLogInnerBO logInnerBO) {
        logInnerBO.setAppName(ApplicationContextHolder.getContext().getEnvironment()
                .getProperty("spring.application.name", ""));

        NiceLogContext niceLogContext = NiceLogContextThreadLocal.read();
        if (niceLogContext != null) {
            logInnerBO.setTraceId(niceLogContext.getTraceId());
            logInnerBO.setEntry(niceLogContext.getEntry());
            logInnerBO.setEntryClassTag(niceLogContext.getEntryClassTag());
            logInnerBO.setEntryMethodTag(niceLogContext.getEntryMethodTag());
        }

        // 通过堆栈获得调用方的类名、方法名、代码行号
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        StackTraceElement stackTraceElement = stackTrace[6];

        if (EntryTypeEnum.MANUAL.equals(logInnerBO.getEntryType())) {
            logInnerBO.setClassName(stackTraceElement.getClassName());
            logInnerBO.setMethodName(stackTraceElement.getMethodName());
            logInnerBO.setCodeLineNumber(String.valueOf(stackTraceElement.getLineNumber()));
        }

        logInnerBO.setLogTime(LocalDateTime.now().format(
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
    }
}
