package com.suchtool.nicelog.util.log.context.feign;

/**
 * Feign接口日志的上下文
 */
public class NiceLogFeignContextThreadLocal {
    /**
     * 构造函数私有
     */
    private NiceLogFeignContextThreadLocal() {
    }

    private static final ThreadLocal<NiceLogFeignContext>
            THREAD_LOCAL = new ThreadLocal<>();

    /**
     * 清除数据
     */
    public static void clear() {
        THREAD_LOCAL.remove();
    }

    /**
     * 写入数据
     */
    public static void write(NiceLogFeignContext niceLogFeignContext) {
        THREAD_LOCAL.set(niceLogFeignContext);
    }

    /**
     * 获取数据
     */
    public static NiceLogFeignContext read() {
        return THREAD_LOCAL.get();
    }

    /**
     * 存储feign原始响应体
     */
    public static void saveOriginFeignResponseBody(String body) {
        NiceLogFeignContext niceLogFeignContext = THREAD_LOCAL.get();
        if (niceLogFeignContext != null) {
            niceLogFeignContext.setFeignOriginResponseBody(body);
        }
    }
}
