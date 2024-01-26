package com.suchtool.betterlog.util.log.bo;

import com.suchtool.betterlog.util.log.LogUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LogBO {
    /**
     * 参数
     */
    private String param;

    /**
     * 返回值
     */
    private String returnValue;

    /**
     * 标记
     */
    private String mark;

    /**
     * 异常简要信息
     */
    private String exceptionInfo;

    /**
     * 异常详细信息
     */
    private String exceptionDetail;

    /**
     * 请求头
     */
    private String header;

    /**
     * 其他1
     */
    private String other1;

    /**
     * 其他2
     */
    private String other2;

    /**
     * 其他3
     */
    private String other3;

    public void info() {
        LogUtil.info(this);
    }

    public void warning() {
        LogUtil.warning(this);
    }

    public void error() {
        LogUtil.error(this);
    }
}
