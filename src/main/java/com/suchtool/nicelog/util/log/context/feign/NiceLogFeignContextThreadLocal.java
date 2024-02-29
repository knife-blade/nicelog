package com.suchtool.nicelog.util.log.context.feign;

/**
 * 接口日志的上下文信息的读写
 */
public class NiceLogFeignContextThreadLocal {
    /**
     * 构造函数私有
     */
    private NiceLogFeignContextThreadLocal() {

    }

    private static final ThreadLocal<NiceLogFeignContext>
            LOG_CONTEXT_THREAD_LOCAL = new ThreadLocal<>();

    /**
     * 清除日志上下文信息
     */
    public static void clear() {
        LOG_CONTEXT_THREAD_LOCAL.remove();
    }

    /**
     * 存储日志上下文信息
     */
    public static void write(NiceLogFeignContext niceLogFeignContext) {
        LOG_CONTEXT_THREAD_LOCAL.set(niceLogFeignContext);
    }

    /**
     * 获取当前日志上下文信息
     */
    public static NiceLogFeignContext read() {
        return LOG_CONTEXT_THREAD_LOCAL.get();
    }
}
