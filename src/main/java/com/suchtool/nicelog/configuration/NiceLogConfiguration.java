package com.suchtool.nicelog.configuration;

import com.suchtool.nicelog.aspect.NiceLogAspectDispatcher;
import com.suchtool.nicelog.aspect.impl.*;
import com.suchtool.nicelog.aspect.impl.feign.FeignLogRequestInterceptor;
import com.suchtool.nicelog.aspect.impl.feign.FeignLogResponseDecoder;
import com.suchtool.nicelog.process.NiceLogProcess;
import com.suchtool.nicelog.process.impl.NiceLogProcessDefaultImpl;
import com.suchtool.nicelog.property.NiceLogAspectOrderProperty;
import com.suchtool.nicelog.property.NiceLogProperty;
import com.xxl.job.core.handler.annotation.XxlJob;
import feign.codec.Decoder;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.scheduling.annotation.ScheduledAnnotationBeanPostProcessor;

@Configuration(value = "com.suchtool.nicelog.niceLogConfiguratio", proxyBeanMethods = false)
@ConditionalOnProperty(name = "suchtool.nicelog.enabled", havingValue = "true", matchIfMissing = true)
public class NiceLogConfiguration {
    @Bean(name = "com.suchtool.nicelog.niceLogProperty")
    @ConfigurationProperties(prefix = "suchtool.nicelog")
    public NiceLogProperty niceLogProperty() {
        return new NiceLogProperty();
    }

    @Bean(name = "com.suchtool.nicelog.niceLogAspectOrderProperty")
    @ConfigurationProperties(prefix = "suchtool.nicelog.order")
    public NiceLogAspectOrderProperty niceLogAspectOrderProperty() {
        return new NiceLogAspectOrderProperty();
    }

    @Bean(name = "com.suchtool.nicelog.niceLogAspectDispatcher")
    public NiceLogAspectDispatcher niceLogAspectDispatcher() {
        return new NiceLogAspectDispatcher();
    }

    @ConditionalOnProperty(name = "com.suchtool.nicelog.enableControllerLog", havingValue = "true", matchIfMissing = true)
    @Configuration(value = "com.suchtool.nicelog.niceLog.controllerAspectConfiguration", proxyBeanMethods = false)
    protected static class ControllerAspectConfiguration {
        @Bean(name = "com.suchtool.nicelog.controllerLogAspect")
        public ControllerLogAspect controllerLogAspect(NiceLogAspectOrderProperty niceLogAspectOrderProperty) {
            int order = niceLogAspectOrderProperty.getControllerLogOrder();
            return new ControllerLogAspect(order);
        }
    }

    @ConditionalOnClass(XxlJob.class)
    @ConditionalOnProperty(name = "com.suchtool.nicelog.enableControllerLog", havingValue = "true", matchIfMissing = true)
    @Configuration(value = "com.suchtool.nicelog.niceLog.xxlJobAspectConfiguration", proxyBeanMethods = false)
    protected static class XxlJobAspectConfiguration {
        @Bean(name = "com.suchtool.nicelog.xxlJobLogAspect")
        public XxlJobLogAspect xxlJobLogAspect(NiceLogAspectOrderProperty niceLogAspectOrderProperty) {
            int order = niceLogAspectOrderProperty.getXxlJobLogOrder();
            return new XxlJobLogAspect(order);
        }
    }

    @ConditionalOnProperty(name = "com.suchtool.nicelog.enableNiceLogAnnotationLog", havingValue = "true", matchIfMissing = true)
    @Configuration(value = "com.suchtool.nicelog.niceLog.niceLogAnnotationAspectConfiguration", proxyBeanMethods = false)
    protected static class NiceLogAnnotationAspectConfiguration {
        @Bean(name = "com.suchtool.nicelog.niceLogAnnotationLogAspect")
        public NiceLogAnnotationAspect niceLogAnnotationLog(NiceLogAspectOrderProperty niceLogAspectOrderProperty) {
            int order = niceLogAspectOrderProperty.getNiceLogAnnotationLogOrder();
            return new NiceLogAnnotationAspect(order);
        }
    }

    @ConditionalOnClass(FeignClient.class)
    @ConditionalOnProperty(name = "com.suchtool.nicelog.enableFeignLog", havingValue = "true", matchIfMissing = true)
    @Configuration(value = "com.suchtool.nicelog.niceLog.feignLogAspectConfiguration", proxyBeanMethods = false)
    protected static class FeignLogAspectConfiguration {
        @Bean(name = "com.suchtool.nicelog.feignLogAspect")
        public FeignLogAspect niceLogAnnotationLog(NiceLogAspectOrderProperty niceLogAspectOrderProperty) {
            int order = niceLogAspectOrderProperty.getFeignLogOrder();
            return new FeignLogAspect(order);
        }

        @Bean(name = "com.suchtool.nicelog.feignLogRequestInterceptor")
        public FeignLogRequestInterceptor feignLogRequestInterceptor(NiceLogAspectOrderProperty niceLogAspectOrderProperty) {
            int order = niceLogAspectOrderProperty.getFeignRequestInterceptorOrder();
            return new FeignLogRequestInterceptor(order);
        }

        @Bean(name = "com.suchtool.nicelog.feignLogResponseDecoder")
        @ConditionalOnMissingBean(Decoder.class)
        public FeignLogResponseDecoder feignLogResponseDecoder(ObjectFactory<HttpMessageConverters> messageConverters) {
            return new FeignLogResponseDecoder(messageConverters);
        }
    }

    @ConditionalOnProperty(name = "com.suchtool.nicelog.enableRabbitMQLog", havingValue = "true", matchIfMissing = true)
    @ConditionalOnClass(RabbitListener.class)
    @Configuration(value = "com.suchtool.nicelog.niceLog.rabbitMQAspectConfiguration", proxyBeanMethods = false)
    protected static class RabbitMQAspectConfiguration {
        @Bean(name = "com.suchtool.nicelog.rabbitMQLogAspect")
        public RabbitMQLogAspect rabbitMQLogAspect(NiceLogAspectOrderProperty niceLogAspectOrderProperty) {
            int order = niceLogAspectOrderProperty.getRabbitMQLogOrder();
            return new RabbitMQLogAspect(order);
        }
    }

    @ConditionalOnClass(RocketMQMessageListener.class)
    @ConditionalOnProperty(name = "com.suchtool.nicelog.enableRocketMQLog", havingValue = "true", matchIfMissing = true)
    @Configuration(value = "com.suchtool.nicelog.niceLog.rocketMQAspectConfiguration", proxyBeanMethods = false)
    protected static class RocketMQAspectConfiguration {
        @Bean(name = "com.suchtool.nicelog.rocketMQLogAspect")
        public RocketMQLogAspect rocketMQLogAspect(NiceLogAspectOrderProperty niceLogAspectOrderProperty) {
            int order = niceLogAspectOrderProperty.getRocketMQLogOrder();
            return new RocketMQLogAspect(order);
        }
    }

    @ConditionalOnClass(KafkaListener.class)
    @ConditionalOnProperty(name = "com.suchtool.nicelog.enableKafkaLog", havingValue = "true", matchIfMissing = true)
    @Configuration(value = "com.suchtool.nicelog.niceLog.kafkaAspectConfiguration", proxyBeanMethods = false)
    protected static class KafkaAspectConfiguration {
        @Bean(name = "com.suchtool.nicelog.kafkaMQLogAspect")
        public KafkaLogAspect kafkaMQLogAspect(NiceLogAspectOrderProperty niceLogAspectOrderProperty) {
            int order = niceLogAspectOrderProperty.getKafkaLogOrder();
            return new KafkaLogAspect(order);
        }
    }

    @ConditionalOnBean(ScheduledAnnotationBeanPostProcessor.class)
    @ConditionalOnProperty(name = "com.suchtool.nicelog.enableScheduledLog", havingValue = "true", matchIfMissing = true)
    @Configuration(value = "com.suchtool.nicelog.niceLog.scheduledAspectConfiguration", proxyBeanMethods = false)
    protected static class ScheduledAspectConfiguration {
        @Bean(name = "com.suchtool.nicelog.scheduledLogAspect")
        public ScheduledLogAspect scheduledLogAspect(NiceLogAspectOrderProperty niceLogAspectOrderProperty) {
            int order = niceLogAspectOrderProperty.getScheduledLogOrder();
            return new ScheduledLogAspect(order);
        }
    }

    @Bean(name = "com.suchtool.nicelog.niceLogProcessDefaultImpl")
    @ConditionalOnMissingBean(NiceLogProcess.class)
    public NiceLogProcessDefaultImpl niceLogProcessDefaultImpl() {
        return new NiceLogProcessDefaultImpl();
    }
}
