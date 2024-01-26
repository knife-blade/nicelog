package com.suchtool.betterlog.annotation;

import com.suchtool.betterlog.configuration.BetterLogConfigurationSelector;
import org.springframework.context.annotation.Import;
import org.springframework.core.Ordered;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(BetterLogConfigurationSelector.class)
public @interface EnableBetterLog {
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
}
