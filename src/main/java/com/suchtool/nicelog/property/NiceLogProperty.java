package com.suchtool.nicelog.property;

import lombok.Data;

@Data
public class NiceLogProperty {
    /**
     * 启用日志功能
     */
    private Boolean enabled = true;

    /**
     * 收集所有日志
     * <p>true：自动收集组件支持的所有日志</p>
     * <p>false：不自动收集日志，需要用{@link com.suchtool.nicelog.annotation.NiceLog}注解来启用类或者方法的日志</p>
     */
    private Boolean collectAll = true;

    /**
     * 启用Controller日志
     */
    private Boolean enableControllerLog = true;

    /**
     * 启用XxlJob日志
     */
    private Boolean enableXxlJobLog = true;

    /**
     * 启用RabbitMQ日志
     */
    private Boolean enableRabbitMQLog = true;
}
