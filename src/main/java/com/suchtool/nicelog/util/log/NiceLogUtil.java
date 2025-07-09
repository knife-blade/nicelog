package com.suchtool.nicelog.util.log;

import com.suchtool.nicelog.constant.EntryTypeEnum;
import com.suchtool.nicelog.constant.LogLevelEnum;
import com.suchtool.nicelog.util.log.bo.NiceLogBO;
import com.suchtool.nicelog.util.log.bo.NiceLogBOBuilder;
import com.suchtool.nicelog.util.log.inner.bo.NiceLogInnerBO;
import com.suchtool.nicelog.util.log.inner.util.NiceLogInnerUtil;
import org.springframework.beans.BeanUtils;


public class NiceLogUtil {
    public static NiceLogBOBuilder createBuilder() {
        return new NiceLogBOBuilder();
    }

    public static void debug(NiceLogBO niceLogBO) {
        convertAndRecord(niceLogBO, LogLevelEnum.DEBUG);
    }

    public static void info(NiceLogBO niceLogBO) {
        convertAndRecord(niceLogBO, LogLevelEnum.INFO);
    }

    public static void warn(NiceLogBO niceLogBO) {
        convertAndRecord(niceLogBO, LogLevelEnum.WARN);
    }

    public static void error(NiceLogBO niceLogBO) {
        convertAndRecord(niceLogBO, LogLevelEnum.ERROR);
    }

    /**
     * 转换实体类并记录日志
     */
    private static void convertAndRecord(NiceLogBO niceLogBO, LogLevelEnum level) {
        NiceLogInnerBO logInnerBO = new NiceLogInnerBO();
        BeanUtils.copyProperties(niceLogBO, logInnerBO);
        logInnerBO.setEntryType(EntryTypeEnum.MANUAL.name());
        logInnerBO.setLevel(level);

        NiceLogInnerUtil.record(logInnerBO);
    }
}
