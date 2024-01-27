package com.suchtool.nicelog.util.log.bo;

import com.suchtool.nicelog.util.log.LogUtil;

public final class LogBOBuilder {
    private final LogBO logBO;

    private LogBOBuilder() {
        this.logBO = new LogBO();
    }

    public static LogBOBuilder create() {
        return new LogBOBuilder();
    }

    public LogBOBuilder param(String param) {
        logBO.setParam(param);
        return this;
    }

    public LogBOBuilder returnValue(String returnValue) {
        logBO.setReturnValue(returnValue);
        return this;
    }

    public LogBOBuilder mark(String mark) {
        logBO.setMark(mark);
        return this;
    }

    public LogBOBuilder errorInfo(String errorInfo) {
        logBO.setErrorInfo(errorInfo);
        return this;
    }

    public LogBOBuilder throwable(Throwable throwable) {
        logBO.setThrowable(throwable);
        return this;
    }

    public LogBOBuilder header(String header) {
        logBO.setHeader(header);
        return this;
    }

    public LogBOBuilder other1(String other1) {
        logBO.setOther1(other1);
        return this;
    }

    public LogBOBuilder other2(String other2) {
        logBO.setOther2(other2);
        return this;
    }

    public LogBOBuilder other3(String other3) {
        logBO.setOther3(other3);
        return this;
    }

    public void info() {
        LogUtil.info(logBO);
    }

    public void warning() {
        LogUtil.warning(logBO);
    }

    public void error() {
        LogUtil.error(logBO);
    }
}
