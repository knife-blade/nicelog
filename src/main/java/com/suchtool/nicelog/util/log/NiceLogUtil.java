package com.suchtool.nicelog.util.log;

import com.suchtool.nicelog.constant.NiceLogEntryTypeEnum;
import com.suchtool.nicelog.constant.NiceLogLogLevelEnum;
import com.suchtool.nicelog.util.log.bo.NiceLogBO;
import com.suchtool.nicelog.util.log.bo.NiceLogBOBuilder;
import com.suchtool.nicelog.util.log.inner.bo.NiceLogInnerBO;
import com.suchtool.nicelog.util.log.inner.util.NiceLogInnerUtil;
import org.springframework.beans.BeanUtils;


public class NiceLogUtil {
    public static NiceLogBOBuilder createBuilder() {
        return new NiceLogBOBuilder();
    }

    public static void trace(NiceLogBO niceLogBO) {
        convertAndRecord(niceLogBO, NiceLogLogLevelEnum.TRACE);
    }

    public static void debug(NiceLogBO niceLogBO) {
        convertAndRecord(niceLogBO, NiceLogLogLevelEnum.DEBUG);
    }

    public static void info(NiceLogBO niceLogBO) {
        convertAndRecord(niceLogBO, NiceLogLogLevelEnum.INFO);
    }

    public static void warn(NiceLogBO niceLogBO) {
        convertAndRecord(niceLogBO, NiceLogLogLevelEnum.WARN);
    }

    public static void error(NiceLogBO niceLogBO) {
        convertAndRecord(niceLogBO, NiceLogLogLevelEnum.ERROR);
    }

    /**
     * 转换实体类并记录日志
     */
    private static void convertAndRecord(NiceLogBO niceLogBO, NiceLogLogLevelEnum level) {
        NiceLogInnerBO logInnerBO = new NiceLogInnerBO();
        BeanUtils.copyProperties(niceLogBO, logInnerBO);
        logInnerBO.setEntryType(NiceLogEntryTypeEnum.MANUAL.name());
        logInnerBO.setLevel(level);

        NiceLogInnerUtil.record(logInnerBO);
    }
}
