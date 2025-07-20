package com.suchtool.nicelog.enhance.logback;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.ThrowableProxy;
import ch.qos.logback.core.AppenderBase;
import com.suchtool.nicelog.property.NiceLogProperty;
import com.suchtool.nicelog.util.log.NiceLogUtil;
import com.suchtool.nicelog.util.log.bo.NiceLogBOBuilder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NiceLogLogbackAppender extends AppenderBase<ILoggingEvent> {
    private NiceLogProperty niceLogProperty;

    public NiceLogLogbackAppender(NiceLogProperty niceLogProperty) {
        this.niceLogProperty = niceLogProperty;
    }

    @Override
    protected void append(ILoggingEvent event) {
        try {
            processAppend(event);
        } catch (Throwable throwable) {
            System.err.println(throwable.getMessage());
        }
    }

    private void processAppend(ILoggingEvent event) {
        NiceLogBOBuilder niceLogBOBuilder = NiceLogUtil.createBuilder()
                .message(event.getFormattedMessage())
                .callerStackTrace(event.getCallerData())
                .stackTraceDepth(0)
                ;
        if (Boolean.TRUE.equals(getNiceLogProperty().getLogbackRecordCallerStackTrace())) {
            niceLogBOBuilder
                    .recordStackTrace(true);
        }

        if (event.getThrowableProxy() != null) {
            ThrowableProxy throwableProxy = (ThrowableProxy) event.getThrowableProxy();
            Throwable throwable = throwableProxy.getThrowable();

            niceLogBOBuilder
                    .throwable(throwable);
        }

        doRecord(event, niceLogBOBuilder);
    }

    private void doRecord(ILoggingEvent event,
                          NiceLogBOBuilder niceLogBOBuilder) {
        Level level = event.getLevel();
        if (Level.TRACE.equals(level)) {
            niceLogBOBuilder.trace();
        } else if (Level.DEBUG.equals(level)) {
            niceLogBOBuilder.debug();
        } else if (Level.INFO.equals(level)) {
            niceLogBOBuilder.info();
        } else if (Level.WARN.equals(level)) {
            niceLogBOBuilder.warn();
        } else if (Level.ERROR.equals(level)) {
            niceLogBOBuilder.error();
        } else {
            niceLogBOBuilder.trace();
        }
    }
}
