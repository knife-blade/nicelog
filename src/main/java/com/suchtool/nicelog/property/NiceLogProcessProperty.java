package com.suchtool.nicelog.property;

import lombok.Data;

@Data
public class NiceLogProcessProperty {
    /**
     * 启用同步记录
     */
    private Boolean enableRecordSync = true;

    /**
     * 启用异步记录
     */
    private Boolean enableRecordAsync = false;

    /**
     * 异步记录的队列容量
     */
    private Integer recordAsyncQueueCapacity = 5000;

}
