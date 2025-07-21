package com.suchtool.nicelog.util.log.inner.bo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.suchtool.nicelog.constant.EntryTypeEnum;
import com.suchtool.nicelog.constant.DirectionTypeEnum;
import com.suchtool.nicelog.constant.LogLevelEnum;
import com.suchtool.nicelog.util.log.bo.NiceLogBO;
import com.suchtool.nicetool.util.lib.datetime.constant.DateTimeFormatConstant;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.OffsetDateTime;

/**
 * 日志内部BO
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class NiceLogInnerBO extends NiceLogBO {
    /**
     * TraceId
     */
    private String traceId;

    /**
     * 时间
     */
    private String logTime;

    /**
     * 日志级别
     */
    private LogLevelEnum level;

    /**
     * 方向
     */
    private DirectionTypeEnum directionType;

    /**
     * 入口类型
     * 对应：{@link EntryTypeEnum#name()}
     */
    private String entryType;

    /**
     * 入口
     * <p>对于Controller，是URL</p>
     * <p>对于RabbitMQ，是@RabbitMQ的queues</p>
     * <p>对于XXL-JOB，是@XxlJob的value</p>
     */
    private String entry;

    /**
     * 入口类上的标签
     */
    private String entryClassTag;

    /**
     * 入口方法的标记
     */
    private String entryMethodTag;

    /**
     * 类名
     */
    private String className;

    /**
     * 类的标记
     * <p>对于Controller，是Controller的@Api的tags值</p>
     */
    private String classTag;

    /**
     * 方法名字
     */
    private String methodName;

    /**
     * 入口的方法标记
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
    private String lineNumber;

    /**
     * 类名及代码行号，中间用:隔开
     */
    private String classNameAndLineNumber;

    /**
     * 调用栈追踪
     */
    private String callerStackTrace;

    /**
     * 错误栈追踪
     */
    private String errorStackTrace;

    /**
     * 应用名字
     */
    private String appName;

    /**
     * 组名字
     */
    private String groupName;

    /**
     * 客户端ip
     */
    private String clientIp;

    /**
     * 调用方ip
     */
    private String callerIp;

    /**
     * 主机IP
     */
    private String hostIp;
}
