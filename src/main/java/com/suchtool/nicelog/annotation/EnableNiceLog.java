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
     * NiceLog注解日志的顺序
     */
    int niceLogAnnotationLogOrder() default Ordered.LOWEST_PRECEDENCE;

    /**
     * Feign请求日志的顺序
     */
    int feignRequestLogOrder() default Ordered.LOWEST_PRECEDENCE;

    /**
     * Feign响应日志的顺序
     */
    int feignResponseLogOrder() default Ordered.LOWEST_PRECEDENCE;
}
