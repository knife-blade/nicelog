package com.suchtool.nicelog.util.log.context;

import lombok.Data;

/**
 * 日志的上下文信息
 */
@Data
public class NiceLogContext {
    /**
     * TraceId
     */
    private String traceId;

    /**
     * 入口个数。清零时把context删掉
     */
    private int entryCount;
}
