package com.suchtool.nicelog.util.log.inner.util;

import com.suchtool.nicelog.constant.DirectionTypeEnum;
import com.suchtool.nicelog.constant.EntryTypeEnum;
import com.suchtool.nicelog.constant.LogLevelEnum;
import com.suchtool.nicelog.process.NiceLogProcess;
import com.suchtool.nicelog.property.NiceLogProperty;
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
import org.springframework.context.ApplicationContext;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.OffsetDateTime;

@Slf4j
public class NiceLogInnerUtil {
    private static String appName;

    private static NiceLogProperty niceLogProperty;

    static {
        ApplicationContext context = ApplicationContextHolder.getContext();
        if (context == null) {
            log.error("nicelog initialization error：ApplicationContext is null");
        } else {
            niceLogProperty = context.getBean(NiceLogProperty.class);
            appName = context.getEnvironment()
                    .getProperty("spring.application.name", "");
        }
    }

    public static void record(NiceLogInnerBO logInnerBO) {
        LogLevelEnum logLevelConfig = niceLogProperty.getLogLevel();
        if (logInnerBO.getLevel().compareTo(logLevelConfig) < 0) {
            return;
        }

        fillCommonField(logInnerBO);

        cutString(logInnerBO);

        NiceLogProcess niceLogProcess = ApplicationContextHolder.getContext()
                .getBean(NiceLogProcess.class);
        niceLogProcess.process(logInnerBO);
    }

    /**
     * 填充公共字段
     */
    private static void fillCommonField(NiceLogInnerBO logInnerBO) {
        logInnerBO.setAppName(appName);

        if (EntryTypeEnum.CONTROLLER.name().equals(logInnerBO.getEntryType())) {
            logInnerBO.setCallerIp(ClientIpUtil.parseRemoteIP());
            logInnerBO.setClientIp(ClientIpUtil.parseClientIP());
        }

        try {
            InetAddress localHost = InetAddress.getLocalHost();
            if (localHost != null) {
                logInnerBO.setHostIp(localHost.getHostAddress());
            }
        } catch (UnknownHostException e) {
        }

        // 填充上下文
        fillContext(logInnerBO);

        StackTraceElement[] callerStackTraceArray = logInnerBO.getStackTrace() != null
                ? logInnerBO.getStackTrace()
                :Thread.currentThread().getStackTrace();

        Integer stackTraceDepth = logInnerBO.getStackTraceDepth() != null
                ? logInnerBO.getStackTraceDepth()
                : niceLogProperty.getCallerStackTraceDepth();

        // 移除nicelog内的调用链路
        StackTraceElement[] newStackTrace = new StackTraceElement[callerStackTraceArray.length - stackTraceDepth];
        for (int i = 0; i < callerStackTraceArray.length; i++) {
            if (i < stackTraceDepth) {
                continue;
            }
            newStackTrace[i - stackTraceDepth] = callerStackTraceArray[i];
        }
        logInnerBO.setStackTrace(newStackTrace);

        if (logInnerBO.getRecordStackTrace() != null
                && logInnerBO.getRecordStackTrace()) {
            logInnerBO.setCallerStackTrace(StackTraceUtil.stackTraceToString(
                    newStackTrace, niceLogProperty.getStackTracePackageName()));
        }

        // 填充栈追踪
        if (logInnerBO.getThrowable() != null) {
            logInnerBO.setErrorStackTrace(ThrowableUtil.stackTraceToString(
                    logInnerBO.getThrowable(), niceLogProperty.getStackTracePackageName()));
            if (!StringUtils.hasText(logInnerBO.getErrorInfo())) {
                logInnerBO.setErrorInfo(logInnerBO.getThrowable().getMessage());
            }
        }

        // 通过堆栈获得调用方的类名、方法名、代码行号
        StackTraceElement stackTraceElement = newStackTrace[0];
        if (EntryTypeEnum.MANUAL.name().equals(logInnerBO.getEntryType())) {
            logInnerBO.setClassName(stackTraceElement.getClassName());
            logInnerBO.setMethodName(stackTraceElement.getMethodName());
            logInnerBO.setLineNumber(String.valueOf(stackTraceElement.getLineNumber()));
            logInnerBO.setClassNameAndLineNumber(logInnerBO.getClassName() + ":" + logInnerBO.getLineNumber());
            logInnerBO.setDirectionType(DirectionTypeEnum.INNER);
        }

        logInnerBO.setLogTime(OffsetDateTime.now());
    }

    private static void fillContext(NiceLogInnerBO logInnerBO) {
        NiceLogContext niceLogContext = NiceLogContextThreadLocal.read();
        if (niceLogContext != null) {
            logInnerBO.setTraceId(niceLogContext.getTraceId());
            logInnerBO.setEntry(StringUtils.hasText(logInnerBO.getEntry())
                    ? logInnerBO.getEntry()
                    : niceLogContext.getEntry()
            );
            logInnerBO.setEntryClassTag(StringUtils.hasText(logInnerBO.getEntryClassTag())
                    ? logInnerBO.getEntryClassTag()
                    : niceLogContext.getEntryClassTag()
            );
            logInnerBO.setEntryMethodTag(StringUtils.hasText(logInnerBO.getEntryMethodTag())
                    ? logInnerBO.getEntryMethodTag()
                    : niceLogContext.getEntryMethodTag()
            );
        }

        if (EntryTypeEnum.FEIGN.name().equals(logInnerBO.getEntryType())) {
            NiceLogFeignContext niceLogFeignContext = NiceLogFeignContextThreadLocal.read();
            if (niceLogFeignContext != null) {
                logInnerBO.setOriginReturnValue(niceLogFeignContext.getFeignOriginResponseBody());
            }
        }
    }

    /**
     * 截断字符串字段
     */
    private static void cutString(Object obj) {
        // 获取该对象的所有字段
        Field[] fields = obj.getClass().getDeclaredFields();

        // 遍历字段
        for (Field field : fields) {
            // 如果字段是String类型
            if (field.getType().equals(String.class)) {
                // 使字段可访问
                field.setAccessible(true);
                Integer stringMaxLength = niceLogProperty.getStringMaxLength();
                if (stringMaxLength != null
                    && stringMaxLength >= 0) {
                    try {
                        String value = (String) field.get(obj);
                        if (value != null) {
                            if (value.length() > stringMaxLength) {
                                value = value.substring(0, stringMaxLength);
                            }
                            field.set(obj, value);
                        }
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
    }
}
