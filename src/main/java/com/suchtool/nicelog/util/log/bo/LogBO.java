package com.suchtool.nicelog.util.log.bo;

import lombok.Data;

@Data
public class LogBO {
    /**
     * 参数
     */
    private String param;

    /**
     * 返回值
     */
    private String returnValue;

    /**
     * 标记
     */
    private String mark;

    /**
     * 错误信息
     */
    private String errorInfo;

    /**
     * 异常信息
     * 可通过工具类获得异常堆栈：{@link com.suchtool.niceutil.util.ThrowableUtil}
     */
    private Throwable throwable;

    /**
     * 请求头
     */
    private String header;

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

}