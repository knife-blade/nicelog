package com.suchtool.nicelog.process;

import com.suchtool.nicelog.util.log.inner.bo.NiceLogInnerBO;

public interface NiceLogDetailProcess {
    /**
     * 预处理
     */
    void preProcess(NiceLogInnerBO logInnerBO);

    /**
     * 同步记录日志
     */
    void recordSync(NiceLogInnerBO logInnerBO);

    /**
     * 异步记录日志
     */
    void recordAsync(NiceLogInnerBO logInnerBO);

}
