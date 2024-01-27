package com.suchtool.nicelog.property;

import lombok.Data;

@Data
public class BetterLogProperty {
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
