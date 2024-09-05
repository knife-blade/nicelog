package com.suchtool.nicelog.util.log.context.feign;

import lombok.Data;

/**
 * 日志的上下文信息
 */
@Data
public class NiceLogFeignContext {
    /**
     * 原始响应体
     */
    private String feignOriginResponseBody;
}
