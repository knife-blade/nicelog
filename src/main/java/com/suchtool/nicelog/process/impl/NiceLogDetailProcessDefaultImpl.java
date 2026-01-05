package com.suchtool.nicelog.process.impl;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.LoggingEvent;
import ch.qos.logback.core.Appender;
import ch.qos.logback.core.ConsoleAppender;
import com.suchtool.nicelog.constant.NiceLogEnhanceTypeEnum;
import com.suchtool.nicelog.process.NiceLogDetailProcess;
import com.suchtool.nicelog.property.NiceLogProperty;
import com.suchtool.nicelog.util.log.inner.bo.NiceLogInnerBO;
import com.suchtool.nicetool.util.base.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Iterator;

@Slf4j
public class NiceLogDetailProcessDefaultImpl implements NiceLogDetailProcess {
    private ConsoleAppender<ILoggingEvent> consoleAppender;

    @Autowired
    private NiceLogProperty niceLogProperty;

    @Override
    public void preProcess(NiceLogInnerBO logInnerBO) {
        // 这里可以修改字段，比如：
        // UserDTO userDTO = UserUtil.currentUser();
        // if (userDTO != null) {
        //     logInnerBO.setOperatorId(userDTO.getUserId());
        //     logInnerBO.setOperatorName(userDTO.getUserName());
        // }
    }

    @Override
    public void recordSync(NiceLogInnerBO logInnerBO) {
        if (!Boolean.TRUE.equals(niceLogProperty.getLogbackEnabled())) {
            print(logInnerBO);
        } else {
            if (!NiceLogEnhanceTypeEnum.LOGBACK.name().equals(logInnerBO.getEnhanceType())) {
                printByLogbackConsole(logInnerBO);
            }
        }
    }

    @Override
    public void recordAsync(NiceLogInnerBO logInnerBO) {
        // 这里可以记录异步日志，比如发送到MQ
    }

    private void print(NiceLogInnerBO logInnerBO) {
        switch (logInnerBO.getLevel()) {
            case TRACE:
                log.trace("nicelog日志：{}", JsonUtil.toJsonString(logInnerBO));
                break;
            case DEBUG:
                log.debug("nicelog日志：{}", JsonUtil.toJsonString(logInnerBO));
                break;
            case INFO:
                log.info("nicelog日志：{}", JsonUtil.toJsonString(logInnerBO));
                break;
            case WARN:
                log.warn("nicelog日志：{}", JsonUtil.toJsonString(logInnerBO));
                break;
            case ERROR:
                log.error("nicelog日志：{}", JsonUtil.toJsonString(logInnerBO));
                break;
        }
    }

    private void printByLogbackConsole(NiceLogInnerBO logInnerBO) {
        // 获取LoggerContext
        LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
        // 获取Logger对象
        Logger rootLogger = context.getLogger(Logger.ROOT_LOGGER_NAME);

        if (consoleAppender == null) {
            Iterator<Appender<ILoggingEvent>> appenderIterator = rootLogger.iteratorForAppenders();
            while (appenderIterator.hasNext()) {
                Appender<ILoggingEvent> appender = appenderIterator.next();
                if (appender instanceof ConsoleAppender) {
                    // 获取ConsoleAppender
                    consoleAppender = (ConsoleAppender<ILoggingEvent>) appender;
                    break;
                }
            }
        }

        Level level = null;

        switch (logInnerBO.getLevel()) {
            case TRACE:
                level = Level.TRACE;
                break;
            case DEBUG:
                level = Level.DEBUG;
                break;
            case INFO:
                level = Level.INFO;
                break;
            case WARN:
                level = Level.WARN;
                break;
            case ERROR:
                level = Level.ERROR;
                break;
        }

        Logger logger = (Logger) LoggerFactory.getLogger(logInnerBO.getClassName());
        // 模拟日志事件
        LoggingEvent event = new LoggingEvent(
                "ch.qos.logback.classic.Logger",
                logger,
                level,
                JsonUtil.toJsonString(logInnerBO),
                logInnerBO.getThrowable(),
                null
        );

        // 使用ConsoleAppender打印消息
        consoleAppender.doAppend(event);
    }
}
