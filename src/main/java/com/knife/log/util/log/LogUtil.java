package com.knife.log.util.log;

import com.knife.log.util.log.bo.LogBO;
import com.knife.log.util.log.inner.bo.LogInnerBO;
import com.knife.log.util.log.inner.util.LogInnerUtil;
import org.springframework.beans.BeanUtils;

public class LogUtil {
    public static void info(LogBO logBO) {
        convertAndRecord(logBO, "INFO");
    }

    public static void warning(LogBO logBO) {
        convertAndRecord(logBO, "WARNING");
    }

    public static void error(LogBO logBO) {
        convertAndRecord(logBO, "ERROR");
    }

    /**
     * 转换实体类并记录日志
     */
    private static void convertAndRecord(LogBO logBO, String level) {
        LogInnerBO logInnerBO = new LogInnerBO();
        BeanUtils.copyProperties(logBO, logInnerBO);
        logInnerBO.setLogTime(level);
        logInnerBO.setTypeDetail("手动打印");
        logInnerBO.setLevel(level);

        LogInnerUtil.record(logInnerBO);
    }
}
