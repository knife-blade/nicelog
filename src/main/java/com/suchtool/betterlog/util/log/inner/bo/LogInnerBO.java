package com.suchtool.betterlog.util.log.inner.bo;

import com.suchtool.betterlog.constant.AspectTypeEnum;
import com.suchtool.betterlog.constant.LogLevelEnum;
import com.suchtool.betterlog.util.log.bo.LogBO;
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
     */
    private LogLevelEnum level;

    /**
     * 类型
     */
    private AspectTypeEnum type;

    /**
     * 日志类型详情
     * 用{@link LogInnerBO#type}的getName()拼接"进入"、"返回"等而来
     * 比如：手动、接口进入、接口返回、接口报错、RabbitMQ进入
     */
    private String typeDetail;

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
