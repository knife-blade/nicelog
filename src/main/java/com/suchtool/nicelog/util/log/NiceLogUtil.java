package com.suchtool.nicelog.util.log;

import com.suchtool.nicelog.constant.AspectTypeEnum;
import com.suchtool.nicelog.constant.LogLevelEnum;
import com.suchtool.nicelog.util.log.bo.NiceLogBO;
import com.suchtool.nicelog.util.log.inner.bo.NiceLogInnerBO;
import com.suchtool.nicelog.util.log.inner.util.NiceLogInnerUtil;
import com.suchtool.niceutil.util.lib.datetime.DateTimeUtil;
import com.suchtool.niceutil.util.lib.datetime.constant.DateTimeFormatConstant;
import org.springframework.beans.BeanUtils;

import java.time.LocalDateTime;

public class NiceLogUtil {
    public static void info(NiceLogBO niceLogBO) {
        convertAndRecord(niceLogBO, LogLevelEnum.INFO);
    }

    public static void warning(NiceLogBO niceLogBO) {
        convertAndRecord(niceLogBO, LogLevelEnum.WARNING);
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
        logInnerBO.setLogTime(DateTimeUtil.format(LocalDateTime.now(),
                DateTimeFormatConstant.DATE_TIME_FORMAT_NORMAL));
        logInnerBO.setAspectType(AspectTypeEnum.MANUAL);
        logInnerBO.setLevel(level);

        NiceLogInnerUtil.record(logInnerBO);
    }
}
