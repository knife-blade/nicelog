package com.suchtool.nicelog.property;

import com.suchtool.nicelog.annotation.NiceLog;
import com.suchtool.nicelog.constant.LogLevelEnum;
import lombok.Data;

import java.util.List;

@Data
public class NiceLogProperty {
    /**
     * 启用日志功能
     */
    private Boolean enabled = true;

    /**
     * 日志收集级别
     */
    private LogLevelEnum logLevel = LogLevelEnum.INFO;

    /**
     * 收集栈日志的包名前缀。为空则全部收集
     */
    private List<String> stackTracePackageName;

    /**
     * 调用栈的深度
     */
    private Integer callerStackTraceDepth = 6;

    /**
     * 自动收集日志
     * <p>true：自动收集组件支持的所有日志</p>
     * <p>false：不自动收集日志，需要用{@link NiceLog}注解来启用类或者方法的日志</p>
     */
    private Boolean autoCollect = true;

    /**
     * 自动收集的包的前缀。默认只收集启动类所在包
     */
    private List<String> autoCollectPackageName;

    /**
     * 启用Controller日志
     */
    private Boolean enableControllerLog = true;

    /**
     * 启用XxlJob日志
     */
    private Boolean enableXxlJobLog = true;

    /**
     * 启用Scheduled日志
     */
    private Boolean enableScheduledLog = true;

    /**
     * 启用NiceLog注解日志
     */
    private Boolean enableNiceLogAnnotationLog = true;

    /**
     * 启用RabbitMQ日志
     */
    private Boolean enableRabbitMQLog = true;

    /**
     * 启用RocketMQ日志
     */
    private Boolean enableRocketMQLog = true;

    /**
     * 启用Kafka日志
     */
    private Boolean enableKafkaLog = true;

    /**
     * 启用Feign日志
     */
    private Boolean enableFeignLog = true;

    /**
     * 不收集Feign日志的包名前缀
     */
    private List<String> ignoreFeignLogPackageName;

    /**
     * 启用Feign的Trace-Id请求头
     */
    private Boolean enableFeignTraceIdRequestHeader = true;

    /**
     * Feign的TraceId的header名字
     */
    private String feignTraceIdRequestHeader = "Nice-Log-Trace-Id";

    /**
     * 字符串字段最大保留长度。默认-1，不截断
     */
    private Integer stringMaxLength = -1;

    /**
     * 启用logback的接管
     */
    private Boolean logbackEnabled = false;

    /**
     * 记录logback的调用栈
     */
    private Boolean logbackRecordCallerStackTrace = false;
}
