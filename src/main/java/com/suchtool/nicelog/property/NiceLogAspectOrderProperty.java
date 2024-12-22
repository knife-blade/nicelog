package com.suchtool.nicelog.property;

import lombok.Data;

@Data
public class NiceLogAspectOrderProperty {
    /**
     * Controller接口日志的优先级
     */
    Integer controllerLogOrder = 10000;

    /**
     * XxlJob日志的优先级
     */
    Integer xxlJobLogOrder = 10001;

    /**
     * RabbitMQ日志的优先级
     */
    Integer rabbitMQLogOrder = 10002;

    /**
     * RocketMQ日志的优先级
     */
    Integer rocketMQLogOrder = 10003;

    /**
     * Kafka日志的优先级
     */
    Integer kafkaLogOrder = 10004;

    /**
     * Feign日志的优先级
     */
    Integer feignLogOrder = 10005;

    /**
     * Feign请求拦截器的优先级
     */
    Integer feignRequestInterceptorOrder = 10006;

    /**
     * Scheduled日志的优先级
     */
    Integer scheduledLogOrder = 10007;

    /**
     * NiceLog注解日志的优先级
     */
    Integer niceLogAnnotationLogOrder = 10008;

}
