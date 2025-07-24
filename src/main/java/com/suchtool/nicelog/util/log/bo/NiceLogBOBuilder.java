package com.suchtool.nicelog.util.log.bo;

import com.suchtool.nicelog.util.log.NiceLogUtil;

public final class NiceLogBOBuilder {
    private final NiceLogBO niceLogBO;

    public NiceLogBOBuilder() {
        this.niceLogBO = new NiceLogBO();
    }

    public void trace() {
        NiceLogUtil.trace(niceLogBO);
    }

    public void debug() {
        NiceLogUtil.debug(niceLogBO);
    }

    public void info() {
        NiceLogUtil.info(niceLogBO);
    }

    public void warn() {
        NiceLogUtil.warn(niceLogBO);
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

    public NiceLogBOBuilder businessNo(String businessNo) {
        niceLogBO.setBusinessNo(businessNo);
        return this;
    }

    public NiceLogBOBuilder message(String message) {
        niceLogBO.setMessage(message);
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

    public NiceLogBOBuilder recordStackTrace(Boolean recordStackTrace) {
        niceLogBO.setRecordStackTrace(recordStackTrace);
        return this;
    }

    public NiceLogBOBuilder stackTraceDepth(Integer stackTraceDepth) {
        niceLogBO.setStackTraceDepth(stackTraceDepth);
        return this;
    }

    public NiceLogBOBuilder callerStackTrace(StackTraceElement[] stackTraceElements) {
        niceLogBO.setStackTrace(stackTraceElements);
        return this;
    }

    public NiceLogBOBuilder operatorId(String operatorId) {
        niceLogBO.setOperatorId(operatorId);
        return this;
    }

    public NiceLogBOBuilder operatorName(String operatorName) {
        niceLogBO.setOperatorName(operatorName);
        return this;
    }

    public NiceLogBOBuilder enhanceType(String enhanceType) {
        niceLogBO.setEnhanceType(enhanceType);
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

    public NiceLogBOBuilder other6(String other6) {
        niceLogBO.setOther6(other6);
        return this;
    }

    public NiceLogBOBuilder other7(String other7) {
        niceLogBO.setOther7(other7);
        return this;
    }

    public NiceLogBOBuilder other8(String other8) {
        niceLogBO.setOther8(other8);
        return this;
    }

    public NiceLogBOBuilder other9(String other9) {
        niceLogBO.setOther9(other9);
        return this;
    }

    public NiceLogBOBuilder other10(String other10) {
        niceLogBO.setOther10(other10);
        return this;
    }
}
