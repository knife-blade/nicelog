package com.suchtool.nicelog.util.log.context;

import lombok.Data;

/**
 * 日志的上下文信息
 */
@Data
public class LogContext {
    /**
     * 入口
     */
    private String entry;

    /**
     * TraceId
     */
    private String traceId;

}
