package com.suchtool.nicelog.util.log.inner.util;

import com.suchtool.nicelog.constant.DirectionTypeEnum;
import com.suchtool.nicelog.constant.EntryTypeEnum;
import com.suchtool.nicelog.process.NiceLogProcess;
import com.suchtool.nicelog.util.log.context.NiceLogContext;
import com.suchtool.nicelog.util.log.context.NiceLogContextThreadLocal;
import com.suchtool.nicelog.util.log.context.feign.NiceLogFeignContext;
import com.suchtool.nicelog.util.log.context.feign.NiceLogFeignContextThreadLocal;
import com.suchtool.nicelog.util.log.inner.bo.NiceLogInnerBO;
import com.suchtool.nicetool.util.base.StackTraceUtil;
import com.suchtool.nicetool.util.base.ThrowableUtil;
import com.suchtool.nicetool.util.spring.ApplicationContextHolder;
import com.suchtool.nicetool.util.web.ip.ClientIpUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

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

        if (EntryTypeEnum.CONTROLLER.equals(logInnerBO.getEntryType())) {
            logInnerBO.setIp(ClientIpUtil.parseRemoteIP());
            logInnerBO.setClientIp(ClientIpUtil.parseClientIP());
        }

        // 填充上下文
        fillContext(logInnerBO);

        // 填充栈追踪
        if (logInnerBO.getThrowable() != null) {
            logInnerBO.setStackTrace(ThrowableUtil.stackTraceToString(logInnerBO.getThrowable()));
            if (!StringUtils.hasText(logInnerBO.getErrorInfo())) {
                logInnerBO.setErrorInfo(logInnerBO.getThrowable().getMessage());
            }
        } else {
            if (logInnerBO.getRecordStackTrace() != null
                    && logInnerBO.getRecordStackTrace()) {
                StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
                // 移除nicelog内的调用链路
                int removeLineCount = 6;
                StackTraceElement[] newStackTrace = new StackTraceElement[stackTrace.length - removeLineCount];
                for (int i = 0; i < stackTrace.length; i++) {
                    if (i < removeLineCount) {
                        continue;
                    }
                    newStackTrace[i - removeLineCount] = stackTrace[i];
                }
                logInnerBO.setStackTrace(StackTraceUtil.stackTraceToString(newStackTrace));
            }
        }

        // 通过堆栈获得调用方的类名、方法名、代码行号
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        StackTraceElement stackTraceElement = stackTrace[6];
        if (EntryTypeEnum.MANUAL.equals(logInnerBO.getEntryType())) {
            logInnerBO.setClassName(stackTraceElement.getClassName());
            logInnerBO.setMethodName(stackTraceElement.getMethodName());
            logInnerBO.setLineNumber(String.valueOf(stackTraceElement.getLineNumber()));
            logInnerBO.setClassNameAndLineNumber(logInnerBO.getClassName() + ":" + logInnerBO.getLineNumber());
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
                logInnerBO.setOriginReturnValue(niceLogFeignContext.getFeignOriginResponseBody());
            }
        }
    }
}
