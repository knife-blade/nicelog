package com.suchtool.nicelog.util.log.inner.util;

import com.suchtool.nicelog.constant.DirectionTypeEnum;
import com.suchtool.nicelog.constant.EntryTypeEnum;
import com.suchtool.nicelog.process.NiceLogProcess;
import com.suchtool.nicelog.util.log.context.NiceLogContext;
import com.suchtool.nicelog.util.log.context.NiceLogContextThreadLocal;
import com.suchtool.nicelog.util.log.context.feign.NiceLogFeignContext;
import com.suchtool.nicelog.util.log.context.feign.NiceLogFeignContextThreadLocal;
import com.suchtool.nicelog.util.log.inner.bo.NiceLogInnerBO;
import com.suchtool.niceutil.util.base.ThrowableUtil;
import com.suchtool.niceutil.util.spring.ApplicationContextHolder;
import com.suchtool.niceutil.util.web.ip.ClientIpUtil;
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

        logInnerBO.setIp(ClientIpUtil.parseRemoteIP());
        logInnerBO.setClientIp(ClientIpUtil.parseClientIP());

        // 填充上下文
        fillContext(logInnerBO);

        // 填充栈信息
        if (logInnerBO.getThrowable() != null) {
            logInnerBO.setStackTrace(ThrowableUtil.getStackTrace(logInnerBO.getThrowable()));
        }

        // 通过堆栈获得调用方的类名、方法名、代码行号
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        StackTraceElement stackTraceElement = stackTrace[6];
        if (EntryTypeEnum.MANUAL.equals(logInnerBO.getEntryType())) {
            logInnerBO.setClassName(stackTraceElement.getClassName());
            logInnerBO.setMethodName(stackTraceElement.getMethodName());
            logInnerBO.setCodeLineNumber(String.valueOf(stackTraceElement.getLineNumber()));
            logInnerBO.setDirectionType(DirectionTypeEnum.INNER);
        }

        logInnerBO.setLogTime(LocalDateTime.now().format(
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")));
    }

    private static void fillContext(NiceLogInnerBO logInnerBO) {
        NiceLogContext niceLogContext = NiceLogContextThreadLocal.read();
        if (niceLogContext != null) {
            logInnerBO.setTraceId(niceLogContext.getTraceId());
        }

        if (EntryTypeEnum.FEIGN.equals(logInnerBO.getEntryType())) {
            NiceLogFeignContext niceLogFeignContext = NiceLogFeignContextThreadLocal.read();
            if (niceLogFeignContext != null) {
                logInnerBO.setEntry(niceLogFeignContext.getEntry());
                logInnerBO.setEntryClassTag(niceLogFeignContext.getEntryClassTag());
                logInnerBO.setEntryMethodTag(niceLogFeignContext.getEntryMethodTag());
                logInnerBO.setOriginReturnValue(niceLogFeignContext.getFeignOriginResponseBody());
            }
        } else {
            if (niceLogContext != null) {
                logInnerBO.setEntry(niceLogContext.getEntry());
                logInnerBO.setEntryClassTag(niceLogContext.getEntryClassTag());
                logInnerBO.setEntryMethodTag(niceLogContext.getEntryMethodTag());
            }
        }
    }
}
