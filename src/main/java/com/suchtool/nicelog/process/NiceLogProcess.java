package com.suchtool.nicelog.process;

import com.suchtool.nicelog.util.log.inner.bo.NiceLogInnerBO;

public interface NiceLogProcess {
    /**
     * 处理
     */
   void process(NiceLogInnerBO logInnerBO);
}
