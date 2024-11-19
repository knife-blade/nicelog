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
     * 入口
     * <p>对于Controller，是URL</p>
     * <p>对于RabbitMQ，是@RabbitMQ的queues</p>
     * <p>对于XXL-JOB，是@XxlJob的value</p>
     */
    private String entry;

    /**
     * 入口类的tag
     */
    private String entryClassTag;

    /**
     * 入口方法的tag
     */
    private String entryMethodTag;

    /**
     * 入口个数。清零时把context删掉
     */
    private int entryCount;
}
