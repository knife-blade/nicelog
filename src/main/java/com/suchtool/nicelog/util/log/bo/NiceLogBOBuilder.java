package com.suchtool.nicelog.util.log.bo;

import com.suchtool.nicelog.util.log.NiceLogUtil;

public final class NiceLogBOBuilder {
    private final NiceLogBO niceLogBO;

    public NiceLogBOBuilder() {
        this.niceLogBO = new NiceLogBO();
    }

    public void info() {
        NiceLogUtil.info(niceLogBO);
    }

    public void warning() {
        NiceLogUtil.warning(niceLogBO);
    }

    public void error() {
        NiceLogUtil.error(niceLogBO);
    }

    public NiceLogBOBuilder param(String param) {
        niceLogBO.setParam(param);
        return this;
    }

    public NiceLogBOBuilder returnValue(String returnValue) {
        niceLogBO.setReturnValue(returnValue);
        return this;
    }

    public NiceLogBOBuilder mark(String mark) {
        niceLogBO.setMark(mark);
        return this;
    }

    public NiceLogBOBuilder errorInfo(String errorInfo) {
        niceLogBO.setErrorInfo(errorInfo);
        return this;
    }

    public NiceLogBOBuilder throwable(Throwable throwable) {
        niceLogBO.setThrowable(throwable);
        return this;
    }

    public NiceLogBOBuilder other1(String other1) {
        niceLogBO.setOther1(other1);
        return this;
    }

    public NiceLogBOBuilder other2(String other2) {
        niceLogBO.setOther2(other2);
        return this;
    }

    public NiceLogBOBuilder other3(String other3) {
        niceLogBO.setOther3(other3);
        return this;
    }

    public NiceLogBOBuilder other4(String other4) {
        niceLogBO.setOther4(other4);
        return this;
    }

    public NiceLogBOBuilder other5(String other5) {
        niceLogBO.setOther5(other5);
        return this;
    }
}
