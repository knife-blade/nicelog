package com.suchtool.nicelog.process.impl;

import com.suchtool.nicelog.process.NiceLogProcess;
import com.suchtool.nicelog.util.log.inner.bo.NiceLogInnerBO;
import com.suchtool.niceutil.util.base.JsonUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NiceLogProcessDefaultImpl implements NiceLogProcess {
    @Override
    public void process(NiceLogInnerBO logInnerBO) {
        // 本处只是将日志打印出来。实际项目可以将日志上传到ES。
        switch (logInnerBO.getLevel()) {
            case INFO:
                log.info("nicelog日志：{}", JsonUtil.toJsonString(logInnerBO));
                break;
            case WARNING:
                log.warn("nicelog日志：{}", JsonUtil.toJsonString(logInnerBO));
                break;
            case ERROR:
                log.error("nicelog日志：{}", JsonUtil.toJsonString(logInnerBO));
                break;
        }
    }
}
