package com.suchtool.nicelog.runner;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import com.suchtool.nicelog.enhance.logback.NiceLogLogbackAppender;
import com.suchtool.nicelog.property.NiceLogProperty;
import org.slf4j.impl.StaticLoggerBinder;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;

public class NiceLogApplicationRunner implements ApplicationRunner {
    private NiceLogProperty niceLogProperty;

    public NiceLogApplicationRunner(NiceLogProperty niceLogProperty) {
        this.niceLogProperty = niceLogProperty;
    }

    @Override
    public void run(ApplicationArguments args) {
        if (Boolean.TRUE.equals(niceLogProperty.getLogbackEnabled())) {
            addLogbackAppender();
        }
    }

    private void addLogbackAppender() {
        try {
            LoggerContext loggerContext = (LoggerContext) StaticLoggerBinder.getSingleton().getLoggerFactory();
            NiceLogLogbackAppender appender = new NiceLogLogbackAppender(niceLogProperty);
            appender.setContext(loggerContext);
            appender.setName("NICE_LOG_LOGBACK_APPENDER");
            appender.start();

            Logger rootLogger = loggerContext.getLogger(Logger.ROOT_LOGGER_NAME);
            rootLogger.addAppender(appender);
        } catch (Exception e) {
        }
    }

}