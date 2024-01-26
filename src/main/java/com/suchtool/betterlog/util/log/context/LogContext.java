package com.suchtool.betterlog.util.log.context;

import lombok.Data;

/**
 * 日志的上下文信息
 */
@Data
public class LogContext {
    /**
     * 入口
     *   对于Controller，是URL
     *   对于MQ，是队列名
     */
    private String entry;

    /**
     * TraceId
     */
    private String traceId;

}
