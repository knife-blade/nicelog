package com.suchtool.betterlog.util.log.inner.util;

import com.suchtool.betterlog.util.log.context.LogContextThreadLocal;
import com.suchtool.betterlog.util.log.inner.bo.LogInnerBO;
import com.knife.util.JsonUtil;
import com.suchtool.betterlog.constant.LogLevelEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
public class LogInnerUtil {
    public static void record(LogInnerBO logInnerBO) {
        logInnerBO.setAppCode("ORDER");
        logInnerBO.setAppName("订单");

        fillCommonField(logInnerBO);

        // 本处只是将日志打印出来。实际项目可以将日志上传到ES。
        switch (logInnerBO.getLevel()) {
            case LogLevelEnum.INFO:
                log.info("日志：{}", JsonUtil.toJson(logInnerBO));
                break;
            case LogLevelEnum.WARNING:
                log.warn("日志：{}", JsonUtil.toJson(logInnerBO));
                break;
            case LogLevelEnum.ERROR:
                log.error("日志：{}", JsonUtil.toJson(logInnerBO));
                break;
        }
    }

    /**
     * 填充公共字段
     */
    private static void fillCommonField(LogInnerBO logInnerBO) {
        // 从用户工具类中获取操作人编码。本处省略
        // logInnerBO.setOperatorCode();
        // 从用户工具类中获取操作人名字。本处省略
        // logInnerBO.setOperatorName();

        logInnerBO.setEntry(LogContextThreadLocal.read().getEntry());
        logInnerBO.setTraceId(LogContextThreadLocal.read().getTraceId());

        // 通过堆栈获得调用方的类名、方法名、代码行号
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        StackTraceElement stackTraceElement = stackTrace[5];

        if (!StringUtils.hasText(logInnerBO.getClassName())
                && !StringUtils.hasText(logInnerBO.getMethodName())
                && !StringUtils.hasText(logInnerBO.getCodeLineNumber())) {
            logInnerBO.setClassName(stackTraceElement.getClassName());
            logInnerBO.setMethodName(stackTraceElement.getMethodName());
            logInnerBO.setCodeLineNumber(String.valueOf(stackTraceElement.getLineNumber()));
        }

        logInnerBO.setLogTime(LocalDateTime.now().format(
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
    }
}
