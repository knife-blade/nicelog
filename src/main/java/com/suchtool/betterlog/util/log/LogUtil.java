package com.suchtool.betterlog.util.log;

import com.suchtool.betterlog.constant.LogLevelEnum;
import com.suchtool.betterlog.util.log.bo.LogBO;
import com.suchtool.betterlog.util.log.inner.bo.LogInnerBO;
import com.suchtool.betterlog.util.log.inner.util.LogInnerUtil;
import com.suchtool.betterutil.constant.DateTimeFormatConstant;
import com.suchtool.betterutil.util.DateTimeUtil;
import org.springframework.beans.BeanUtils;

import java.time.LocalDateTime;

public class LogUtil {
    public static void info(LogBO logBO) {
        convertAndRecord(logBO, LogLevelEnum.INFO);
    }

    public static void warning(LogBO logBO) {
        convertAndRecord(logBO, LogLevelEnum.WARNING);
    }

    public static void error(LogBO logBO) {
        convertAndRecord(logBO, LogLevelEnum.ERROR);
    }

    /**
     * 转换实体类并记录日志
     */
    private static void convertAndRecord(LogBO logBO, LogLevelEnum level) {
        LogInnerBO logInnerBO = new LogInnerBO();
        BeanUtils.copyProperties(logBO, logInnerBO);
        logInnerBO.setLogTime(DateTimeUtil.format(LocalDateTime.now(),
                DateTimeFormatConstant.DATE_TIME_FORMAT_NORMAL));

        logInnerBO.setTypeDetail("手动打印");
        logInnerBO.setLevel(level);

        LogInnerUtil.record(logInnerBO);
    }
}
