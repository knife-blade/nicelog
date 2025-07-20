package com.suchtool.nicelog.util.log.bo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.suchtool.nicelog.util.log.inner.bo.NiceLogInnerBO;
import lombok.Data;

@Data
public class NiceLogBO {
    /**
     * 标记
     */
    private String mark;

    /**
     * 业务单号
     */
    private String businessNo;

    /**
     * 信息
     */
    private String message;

    /**
     * 错误信息
     */
    private String errorInfo;

    /**
     * 错误详细信息
     */
    private String errorDetailInfo;

    /**
     * 异常信息。栈追踪字符串会自动保存到{@link NiceLogInnerBO#getStackTrace()}
     */
    @JsonIgnore
    private Throwable throwable;

    /**
     * 打印栈追踪(用于非异常时主动获得栈追踪）
     */
    private Boolean recordStackTrace;

    /**
     * 调recordStrackTrace时，栈深度
     */
    private Integer stackTraceDepth;

    /**
     * 调用的栈追踪
     */
    @JsonIgnore
    private StackTraceElement[] stackTrace;

    /**
     * 参数
     */
    private String param;

    /**
     * 返回值
     */
    private String returnValue;

    /**
     * 原始返回值
     */
    private String originReturnValue;

    /**
     * 操作人ID
     */
    private String operatorId;

    /**
     * 操作人名字
     */
    private String operatorName;

    /**
     * 其他1
     */
    private String other1;

    /**
     * 其他2
     */
    private String other2;

    /**
     * 其他3
     */
    private String other3;

    /**
     * 其他4
     */
    private String other4;

    /**
     * 其他5
     */
    private String other5;

    /**
     * 其他6
     */
    private String other6;

    /**
     * 其他7
     */
    private String other7;

    /**
     * 其他8
     */
    private String other8;

    /**
     * 其他9
     */
    private String other9;

    /**
     * 其他10
     */
    private String other10;

}
