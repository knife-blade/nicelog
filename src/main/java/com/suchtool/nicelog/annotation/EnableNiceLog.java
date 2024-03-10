package com.suchtool.nicelog.annotation;

import com.suchtool.nicelog.configuration.NiceLogConfigurationSelector;
import org.springframework.context.annotation.Import;
import org.springframework.core.Ordered;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(NiceLogConfigurationSelector.class)
public @interface EnableNiceLog {
    /**
     * Controller接口日志的顺序
     */
    int controllerLogOrder() default Ordered.LOWEST_PRECEDENCE;

    /**
     * XxlJob日志的顺序
     */
    int xxlJobLogOrder() default Ordered.LOWEST_PRECEDENCE;

    /**
     * RabbitMQ日志的顺序
     */
    int rabbitMQLogOrder() default Ordered.LOWEST_PRECEDENCE;

    /**
     * RocketMQ日志的顺序
     */
    int rocketMQLogOrder() default Ordered.LOWEST_PRECEDENCE;

    /**
     * Kafka日志的顺序
     */
    int kafkaLogOrder() default Ordered.LOWEST_PRECEDENCE;

    /**
     * NiceLog注解日志的顺序
     */
    int niceLogAnnotationLogOrder() default Ordered.LOWEST_PRECEDENCE;

    /**
     * Feign日志的顺序
     */
    int feignLogOrder() default Ordered.LOWEST_PRECEDENCE - 100;

    /**
     * Feign请求拦截器的顺序
     */
    int feignRequestInterceptorOrder() default Ordered.HIGHEST_PRECEDENCE + 100;

    /**
     * Scheduled日志的顺序
     */
    int scheduledLogOrder() default Ordered.LOWEST_PRECEDENCE;

}
