package com.suchtool.nicelog.process;

import com.suchtool.nicelog.util.log.inner.bo.NiceLogInnerBO;

public interface NiceLogProcess {
    void process(NiceLogInnerBO logInnerBO);
}
