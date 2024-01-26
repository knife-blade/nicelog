package com.suchtool.betterlog.configuration;

import com.suchtool.betterlog.annotation.EnableBetterLog;
import com.suchtool.betterlog.aspect.impl.ControllerLogAspect;
import com.suchtool.betterlog.aspect.impl.RabbitMQLogAspect;
import com.suchtool.betterlog.aspect.impl.XxlJobLogAspect;
import com.suchtool.betterlog.process.BetterLogProcess;
import com.suchtool.betterlog.process.impl.BetterLogProcessDefaultImpl;
import com.suchtool.betterlog.property.BetterLogProperty;
import com.xxl.job.core.handler.annotation.XxlJob;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportAware;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.lang.Nullable;

@Configuration(proxyBeanMethods = false)
public class BetterLogConfiguration {
    /**
     * 只为代码提示，无实际作用
     */
    @Bean(name = "com.suchtool.betterlog.betterLogProperty")
    @ConfigurationProperties(prefix = "suchtool.betterlog")
    public BetterLogProperty betterLogProperty() {
        return new BetterLogProperty();
    }

    @Configuration(proxyBeanMethods = false)
    protected static class ControllerAspectConfiguration extends AbstractBetterLogAspectConfiguration {
        @Bean(name = "com.suchtool.betterlog.controllerLogAspect")
        @ConditionalOnProperty(name = "com.suchtool.betterlog.enableControllerLog", havingValue = "true", matchIfMissing = true)
        public ControllerLogAspect controllerLogAspect() {
            int order = Ordered.LOWEST_PRECEDENCE;
            if (enableBetterLog != null) {
                order = enableBetterLog.<Integer>getNumber("controllerLogOrder");
            }

            return new ControllerLogAspect(order);
        }
    }

    @ConditionalOnClass(XxlJob.class)
    @Configuration(proxyBeanMethods = false)
    protected static class XxlJobAspectConfiguration extends AbstractBetterLogAspectConfiguration {
        @Bean(name = "com.suchtool.betterlog.xxlJobLogAspect")
        @ConditionalOnProperty(name = "com.suchtool.betterlog.enableControllerLog", havingValue = "true", matchIfMissing = true)
        public XxlJobLogAspect xxlJobLogAspect() {
            int order = Ordered.LOWEST_PRECEDENCE;
            if (enableBetterLog != null) {
                order = enableBetterLog.<Integer>getNumber("xxlJobLogOrder");
            }

            return new XxlJobLogAspect(order);
        }
    }

    @ConditionalOnClass(RabbitListener.class)
    @Configuration(proxyBeanMethods = false)
    protected static class RabbitMQAspectConfiguration extends AbstractBetterLogAspectConfiguration {
        public RabbitMQLogAspect rabbitMQLogAspect() {
            int order = Ordered.LOWEST_PRECEDENCE;
            if (enableBetterLog != null) {
                order = enableBetterLog.<Integer>getNumber("rabbitMQLogOrder");
            }

            return new RabbitMQLogAspect(order);
        }
    }

    // @Bean(name = "com.suchtool.betterlog.xxlJobLogAspect")
    // @ConditionalOnClass(XxlJob.class)
    // @ConditionalOnProperty(name = "com.suchtool.betterlog.enableXxlJobLog", havingValue = "true", matchIfMissing = true)
    // public XxlJobLogAspect xxlJobLogAspect() {
    //     int order = Ordered.LOWEST_PRECEDENCE;
    //     if (enableBetterLog != null) {
    //         order = enableBetterLog.<Integer>getNumber("xxlJobLogOrder");
    //     }
    //
    //     return new XxlJobLogAspect(order);
    // }

    // @Bean(name = "com.suchtool.betterlog.rabbitMQLogAspect")
    // @ConditionalOnClass(RabbitListener.class)
    // @ConditionalOnProperty(name = "com.suchtool.betterlog.enableRabbitMQLog", havingValue = "true", matchIfMissing = true)
    // public RabbitMQLogAspect rabbitMQLogAspect() {
    //     int order = Ordered.LOWEST_PRECEDENCE;
    //     if (enableBetterLog != null) {
    //         order = enableBetterLog.<Integer>getNumber("rabbitMQLogOrder");
    //     }
    //
    //     return new RabbitMQLogAspect(order);
    // }

    @Bean(name = "com.suchtool.betterlog.betterLogProcessDefaultImpl")
    @ConditionalOnMissingBean(BetterLogProcess.class)
    public BetterLogProcessDefaultImpl betterLogProcessDefaultImpl() {
        return new BetterLogProcessDefaultImpl();
    }
}
