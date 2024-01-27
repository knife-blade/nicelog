package com.suchtool.nicelog.configuration;

import com.suchtool.nicelog.aspect.impl.ControllerLogAspect;
import com.suchtool.nicelog.aspect.impl.RabbitMQLogAspect;
import com.suchtool.nicelog.aspect.impl.XxlJobLogAspect;
import com.suchtool.nicelog.process.BetterLogProcess;
import com.suchtool.nicelog.process.impl.BetterLogProcessDefaultImpl;
import com.suchtool.nicelog.property.BetterLogProperty;
import com.xxl.job.core.handler.annotation.XxlJob;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

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

    @Bean(name = "com.suchtool.betterlog.betterLogProcessDefaultImpl")
    @ConditionalOnMissingBean(BetterLogProcess.class)
    public BetterLogProcessDefaultImpl betterLogProcessDefaultImpl() {
        return new BetterLogProcessDefaultImpl();
    }
}