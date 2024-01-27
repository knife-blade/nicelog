package com.suchtool.nicelog.util.log.inner.bo;

import com.suchtool.nicelog.constant.AspectTypeEnum;
import com.suchtool.nicelog.constant.DirectionTypeEnum;
import com.suchtool.nicelog.constant.LogLevelEnum;
import com.suchtool.nicelog.util.log.bo.LogBO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 日志内部BO
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class LogInnerBO extends LogBO {
    /**
     * 应用名字
     */
    private String appName;

    /**
     * 入口
     * <p>对于Controller，是URL</p>
     * <p>对于RabbitMQ，是@RabbitMQ的queues</p>
     * <p>对于XXL-JOB，是@XxlJob的value</p>
     */
    private String entry;

    /**
     * 类名
     */
    private String className;

    /**
     * 类的标签
     * <p>对于Controller，是Controller的@Api的tags值</p>
     */
    private String classTag;

    /**
     * 方法名字
     */
    private String methodName;

    /**
     * 入口的方法标签
     * <p>对于Controller，是Controller的方法的@ApiOperation的value字段值</p>
     */
    private String methodTag;

    /**
     * 方法详情
     */
    private String methodDetail;

    /**
     * 代码行号
     */
    private String codeLineNumber;

    /**
     * 日志级别
     */
    private LogLevelEnum level;

    /**
     * 切面类型
     */
    private AspectTypeEnum aspectType;

    /**
     * 方向
     */
    private DirectionTypeEnum directionType;

    /**
     * TraceId
     */
    private String traceId;

    /**
     * 时间
     */
    private String logTime;

    /**
     * 客户端ip
     */
    private String clientIp;

    /**
     * ip
     */
    private String ip;
}
