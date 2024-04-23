package com.suchtool.nicelog.property;

import lombok.Data;

@Data
public class NiceLogAspectOrderProperty {
    /**
     * Controller接口日志的顺序
     */
    Integer controllerLogOrder = 20000;

    /**
     * XxlJob日志的顺序
     */
    Integer xxlJobLogOrder = 20000;

    /**
     * RabbitMQ日志的顺序
     */
    Integer rabbitMQLogOrder = 20000;

    /**
     * RocketMQ日志的顺序
     */
    Integer rocketMQLogOrder = 20000;

    /**
     * Kafka日志的顺序
     */
    Integer kafkaLogOrder = 20000;

    /**
     * NiceLog注解日志的顺序
     */
    Integer niceLogAnnotationLogOrder = 20000;

    /**
     * Feign日志的顺序
     */
    Integer feignLogOrder = 20000;

    /**
     * Feign请求拦截器的顺序
     */
    Integer feignRequestInterceptorOrder = 20000;

    /**
     * Scheduled日志的顺序
     */
    Integer scheduledLogOrder = 20000;
}
