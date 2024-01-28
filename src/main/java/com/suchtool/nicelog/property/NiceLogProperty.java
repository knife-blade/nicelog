package com.suchtool.nicelog.property;

import lombok.Data;

@Data
public class NiceLogProperty {
    /**
     * 启用所有日志
     */
    private Boolean enabled;

    /**
     * 启用Controller日志
     */
    private Boolean enableControllerLog;

    /**
     * 启用XxlJob日志
     */
    private Boolean enableXxlJobLog;

    /**
     * 启用RabbitMQ日志
     */
    private Boolean enableRabbitMQLog;
}
