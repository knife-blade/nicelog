package com.suchtool.nicelog.util.log.context;

/**
 * 接口日志的上下文
 */
public class NiceLogContextThreadLocal {
    /**
     * 构造函数私有
     */
    private NiceLogContextThreadLocal() {
    }

    private static final ThreadLocal<NiceLogContext>
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
    public static void write(NiceLogContext niceLogContext) {
        THREAD_LOCAL.set(niceLogContext);
    }

    /**
     * 获取当前数据
     */
    public static NiceLogContext read() {
        return THREAD_LOCAL.get();
    }
}
