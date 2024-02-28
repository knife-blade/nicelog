package com.suchtool.nicelog.configuration;

import com.suchtool.nicelog.aspect.impl.*;
import com.suchtool.nicelog.aspect.impl.feign.FeignLogRequestInterceptor;
import com.suchtool.nicelog.process.NiceLogProcess;
import com.suchtool.nicelog.process.impl.NiceLogProcessDefaultImpl;
import com.suchtool.nicelog.property.NiceLogProperty;
import com.xxl.job.core.handler.annotation.XxlJob;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

@Configuration(proxyBeanMethods = false)
@ConditionalOnProperty(name = "suchtool.nicelog.enabled", havingValue = "true", matchIfMissing = true)
public class NiceLogConfiguration {
    @Bean(name = "com.suchtool.nicelog.niceLogProperty")
    @ConfigurationProperties(prefix = "suchtool.nicelog")
    public NiceLogProperty niceLogProperty() {
        return new NiceLogProperty();
    }

    @Configuration(proxyBeanMethods = false)
    protected static class ControllerAspectConfiguration extends AbstractNiceLogAspectConfiguration {
        @Bean(name = "com.suchtool.nicelog.controllerLogAspect")
        @ConditionalOnProperty(name = "com.suchtool.nicelog.enableControllerLog", havingValue = "true", matchIfMissing = true)
        public ControllerLogAspect controllerLogAspect() {
            int order = Ordered.LOWEST_PRECEDENCE;
            if (enableNiceLog != null) {
                order = enableNiceLog.<Integer>getNumber("controllerLogOrder");
            }

            return new ControllerLogAspect(order);
        }
    }

    @ConditionalOnClass(XxlJob.class)
    @Configuration(proxyBeanMethods = false)
    protected static class XxlJobAspectConfiguration extends AbstractNiceLogAspectConfiguration {
        @Bean(name = "com.suchtool.nicelog.xxlJobLogAspect")
        @ConditionalOnProperty(name = "com.suchtool.nicelog.enableControllerLog", havingValue = "true", matchIfMissing = true)
        public XxlJobLogAspect xxlJobLogAspect() {
            int order = Ordered.LOWEST_PRECEDENCE;
            if (enableNiceLog != null) {
                order = enableNiceLog.<Integer>getNumber("xxlJobLogOrder");
            }

            return new XxlJobLogAspect(order);
        }
    }

    @ConditionalOnClass(RabbitListener.class)
    @Configuration(proxyBeanMethods = false)
    protected static class RabbitMQAspectConfiguration extends AbstractNiceLogAspectConfiguration {
        @Bean(name = "com.suchtool.nicelog.rabbitMQLogAspect")
        @ConditionalOnProperty(name = "com.suchtool.nicelog.enableRabbitMQLog", havingValue = "true", matchIfMissing = true)
        public RabbitMQLogAspect rabbitMQLogAspect() {
            int order = Ordered.LOWEST_PRECEDENCE;
            if (enableNiceLog != null) {
                order = enableNiceLog.<Integer>getNumber("rabbitMQLogOrder");
            }

            return new RabbitMQLogAspect(order);
        }
    }

    @Configuration(proxyBeanMethods = false)
    protected static class NiceLogAnnotationAspectConfiguration extends AbstractNiceLogAspectConfiguration {
        @Bean(name = "com.suchtool.nicelog.niceLogAnnotationLogAspect")
        @ConditionalOnProperty(name = "com.suchtool.nicelog.enableNiceLogAnnotationLog", havingValue = "true", matchIfMissing = true)
        public NiceLogAnnotationAspect niceLogAnnotationLog() {
            int order = Ordered.LOWEST_PRECEDENCE;
            if (enableNiceLog != null) {
                order = enableNiceLog.<Integer>getNumber("niceLogAnnotationLogOrder");
            }

            return new NiceLogAnnotationAspect(order);
        }
    }

    @ConditionalOnClass(FeignClient.class)
    @Configuration(proxyBeanMethods = false)
    protected static class FeignLogAspectConfiguration extends AbstractNiceLogAspectConfiguration {
        @Bean(name = "com.suchtool.nicelog.feignLogAspect")
        @ConditionalOnProperty(name = "com.suchtool.nicelog.enableFeignLog", havingValue = "true", matchIfMissing = true)
        public FeignLogAspect niceLogAnnotationLog() {
            int order = Ordered.LOWEST_PRECEDENCE;
            if (enableNiceLog != null) {
                order = enableNiceLog.<Integer>getNumber("feignLogOrder");
            }

            return new FeignLogAspect(order);
        }

        @Bean(name = "com.suchtool.nicelog.feignLogRequestInterceptor")
        @ConditionalOnProperty(name = "com.suchtool.nicelog.enableFeignLog", havingValue = "true", matchIfMissing = true)
        public FeignLogRequestInterceptor feignLogRequestInterceptor() {
            return new FeignLogRequestInterceptor();
        }
    }

    @Bean(name = "com.suchtool.nicelog.niceLogProcessDefaultImpl")
    @ConditionalOnMissingBean(NiceLogProcess.class)
    public NiceLogProcessDefaultImpl niceLogProcessDefaultImpl() {
        return new NiceLogProcessDefaultImpl();
    }
}
