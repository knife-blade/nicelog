package com.knife.log.util.log.inner.bo;

import com.knife.example.common.util.log.bo.LogBO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 日志内部BO
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class LogInnerBO extends LogBO {
    /**
     * 应用编码
     */
    private String appCode;

    /**
     * 应用名字
     */
    private String appName;

    /**
     * 入口
     *   对于Controller，是URL
     *   对于MQ，是队列名
     */
    private String entry;

    /**
     * 类名
     */
    private String className;

    /**
     * 类的标签
     *  对于Controller，是Controller的@Api的tags值
     *  对于MQ，是空值
     */
    private String classTag;

    /**
     * 入口的方法名字
     *    对于Controller，是Controller的方法
     *    对于MQ，是消费类对应的方法
     */
    private String methodName;

    /**
     * 入口的方法标签
     *    对于Controller，是Controller的方法的@ApiOperation的value字段值
     *    对于MQ，是空值
     */
    private String methodTag;

    /**
     * 代码行号
     */
    private String codeLineNumber;

    /**
     * 日志级别
     *   INFO：信息
     *   WARNING：警告
     *   ERROR：错误
     */
    private String level;

    /**
     * 日志类型。取值如下：
     *   手动打印、接口请求、接口返回、接口报错、RabbitMQ消费
     */
    private String type;

    /**
     * TraceId
     */
    private String traceId;

    /**
     * 操作人编码
     */
    private String operatorCode;

    /**
     * 操作人名字
     */
    private String operatorName;

    /**
     * 时间
     */
    private String logTime;
}
