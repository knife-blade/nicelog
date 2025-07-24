package com.suchtool.nicelog.process.impl;

import com.suchtool.nicelog.constant.EnhanceTypeEnum;
import com.suchtool.nicelog.process.NiceLogProcess;
import com.suchtool.nicelog.util.log.inner.bo.NiceLogInnerBO;
import com.suchtool.nicetool.util.base.JsonUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NiceLogProcessDefaultImpl implements NiceLogProcess {
    @Override
    public void process(NiceLogInnerBO logInnerBO) {
        if (!EnhanceTypeEnum.LOGBACK.name().equals(logInnerBO.getEnhanceType())) {
            print(logInnerBO);
        }

        // 实际项目在可以在这里将日志上传到ES
    }

    private void print(NiceLogInnerBO logInnerBO) {
        switch (logInnerBO.getLevel()) {
            case TRACE:
                log.trace("nicelog日志：{}", JsonUtil.toJsonString(logInnerBO));
                break;
            case DEBUG:
                log.debug("nicelog日志：{}", JsonUtil.toJsonString(logInnerBO));
                break;
            case INFO:
                log.info("nicelog日志：{}", JsonUtil.toJsonString(logInnerBO));
                break;
            case WARN:
                log.warn("nicelog日志：{}", JsonUtil.toJsonString(logInnerBO));
                break;
            case ERROR:
                log.error("nicelog日志：{}", JsonUtil.toJsonString(logInnerBO));
                break;
        }
    }
}
