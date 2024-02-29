package com.suchtool.nicelog.util.log.context.feign;

import lombok.Data;

/**
 * 日志的上下文信息
 */
@Data
public class NiceLogFeignContext {
    /**
     * 入口
     */
    private String entry;

    /**
     * 入口类标记
     */
    private String entryClassTag;

    /**
     * 入口方法标记
     */
    private String entryMethodTag;

}
