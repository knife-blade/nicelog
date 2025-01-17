package com.suchtool.nicelog.configuration;

import com.suchtool.nicelog.aspect.NiceLogAspectDispatcher;
import com.suchtool.nicelog.aspect.impl.*;
import com.suchtool.nicelog.aspect.impl.feign.NiceLogFeignLogRequestInterceptor;
import com.suchtool.nicelog.aspect.impl.feign.NiceLogFeignLogResponseDecoder;
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
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.scheduling.annotation.ScheduledAnnotationBeanPostProcessor;

@Configuration(value = "com.suchtool.nicelog.niceLogConfiguration", proxyBeanMethods = false)
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

    @ConditionalOnProperty(name = "suchtool.nicelog.enable-controller-log", havingValue = "true", matchIfMissing = true)
    @Configuration(value = "com.suchtool.nicelog.niceLogControllerAspectConfiguration", proxyBeanMethods = false)
    protected static class NiceLogControllerAspectConfiguration {
        @Bean(name = "com.suchtool.nicelog.niceLogControllerLogAspect")
        public NiceLogControllerNiceLogAspect niceLogControllerLogAspect(NiceLogAspectOrderProperty niceLogAspectOrderProperty) {
            int order = niceLogAspectOrderProperty.getControllerLogOrder();
            return new NiceLogControllerNiceLogAspect(order);
        }
    }

    @ConditionalOnClass(XxlJob.class)
    @ConditionalOnProperty(name = "suchtool.nicelog.enable-xxl-job-log", havingValue = "true", matchIfMissing = true)
    @Configuration(value = "com.suchtool.nicelog.niceLogXxlJobAspectConfiguration", proxyBeanMethods = false)
    protected static class XxlJobAspectConfiguration {
        @Bean(name = "com.suchtool.nicelog.niceLogXxlJobLogAspect")
        public NiceLogXxlJobNiceLogAspect niceLogXxlJobLogAspect(NiceLogAspectOrderProperty niceLogAspectOrderProperty) {
            int order = niceLogAspectOrderProperty.getXxlJobLogOrder();
            return new NiceLogXxlJobNiceLogAspect(order);
        }
    }

    @ConditionalOnBean(ScheduledAnnotationBeanPostProcessor.class)
    @ConditionalOnProperty(name = "suchtool.nicelog.enable-scheduled-log", havingValue = "true", matchIfMissing = true)
    @Configuration(value = "com.suchtool.nicelog.niceLogScheduledAspectConfiguration", proxyBeanMethods = false)
    protected static class ScheduledAspectConfiguration {
        @Bean(name = "com.suchtool.nicelog.niceLogScheduledLogAspect")
        public NiceLogScheduledNiceLogAspect niceLogScheduledLogAspect(NiceLogAspectOrderProperty niceLogAspectOrderProperty) {
            int order = niceLogAspectOrderProperty.getScheduledLogOrder();
            return new NiceLogScheduledNiceLogAspect(order);
        }
    }

    @ConditionalOnProperty(name = "suchtool.nicelog.enable-nice-log-annotation-log", havingValue = "true", matchIfMissing = true)
    @Configuration(value = "com.suchtool.nicelog.niceLogAnnotationAspectConfiguration", proxyBeanMethods = false)
    protected static class NiceLogAnnotationAspectConfiguration {
        @Bean(name = "com.suchtool.nicelog.niceLogAnnotationLogAspect")
        public NiceNiceLogAnnotationAspect niceLogAnnotationLog(NiceLogAspectOrderProperty niceLogAspectOrderProperty) {
            int order = niceLogAspectOrderProperty.getNiceLogAnnotationLogOrder();
            return new NiceNiceLogAnnotationAspect(order);
        }
    }

    @ConditionalOnProperty(name = "suchtool.nicelog.enable-rabbit-mq-log", havingValue = "true", matchIfMissing = true)
    @ConditionalOnClass(RabbitListener.class)
    @Configuration(value = "com.suchtool.nicelog.niceLogRabbitMQAspectConfiguration", proxyBeanMethods = false)
    protected static class RabbitMQAspectConfiguration {
        @Bean(name = "com.suchtool.nicelog.niceLogRabbitMQLogAspect")
        public NiceLogRabbitMQNiceLogAspect niceLogRabbitMQLogAspect(NiceLogAspectOrderProperty niceLogAspectOrderProperty) {
            int order = niceLogAspectOrderProperty.getRabbitMQLogOrder();
            return new NiceLogRabbitMQNiceLogAspect(order);
        }
    }

    @ConditionalOnClass(RocketMQMessageListener.class)
    @ConditionalOnProperty(name = "suchtool.nicelog.enable-rocket-mq-log", havingValue = "true", matchIfMissing = true)
    @Configuration(value = "com.suchtool.nicelog.niceLogRocketMQAspectConfiguration", proxyBeanMethods = false)
    protected static class RocketMQAspectConfiguration {
        @Bean(name = "com.suchtool.nicelog.niceLogRocketMQLogAspect")
        public NiceLogRocketMQNiceLogAspect niceLogRocketMQLogAspect(NiceLogAspectOrderProperty niceLogAspectOrderProperty) {
            int order = niceLogAspectOrderProperty.getRocketMQLogOrder();
            return new NiceLogRocketMQNiceLogAspect(order);
        }
    }

    @ConditionalOnClass(KafkaListener.class)
    @ConditionalOnProperty(name = "suchtool.nicelog.enable-kafka-log", havingValue = "true", matchIfMissing = true)
    @Configuration(value = "com.suchtool.nicelog.niceLogKafkaAspectConfiguration", proxyBeanMethods = false)
    protected static class KafkaAspectConfiguration {
        @Bean(name = "com.suchtool.nicelog.niceLogKafkaMQLogAspect")
        public NiceLogKafkaNiceLogAspect niceLogKafkaMQLogAspect(NiceLogAspectOrderProperty niceLogAspectOrderProperty) {
            int order = niceLogAspectOrderProperty.getKafkaLogOrder();
            return new NiceLogKafkaNiceLogAspect(order);
        }
    }

    @ConditionalOnClass(FeignClient.class)
    @ConditionalOnProperty(name = "suchtool.nicelog.enable-feign-log", havingValue = "true", matchIfMissing = true)
    @Configuration(value = "com.suchtool.nicelog.niceLogFeignLogAspectConfiguration", proxyBeanMethods = false)
    protected static class FeignLogAspectConfiguration {
        @Bean(name = "com.suchtool.nicelog.niceLogFeignLogAspect")
        public NiceLogFeignNiceLogAspect niceLogNiceLogAnnotationLog(NiceLogAspectOrderProperty niceLogAspectOrderProperty) {
            int order = niceLogAspectOrderProperty.getFeignLogOrder();
            return new NiceLogFeignNiceLogAspect(order);
        }

        @Bean(name = "com.suchtool.nicelog.niceLogFeignLogRequestInterceptor")
        public NiceLogFeignLogRequestInterceptor niceLogFeignLogRequestInterceptor(NiceLogAspectOrderProperty niceLogAspectOrderProperty) {
            int order = niceLogAspectOrderProperty.getFeignRequestInterceptorOrder();
            return new NiceLogFeignLogRequestInterceptor(order);
        }

        @Bean(name = "com.suchtool.nicelog.niceLogFeignLogResponseDecoder")
        @ConditionalOnMissingBean(Decoder.class)
        public NiceLogFeignLogResponseDecoder niceLogFeignLogResponseDecoder(ObjectFactory<HttpMessageConverters> messageConverters) {
            return new NiceLogFeignLogResponseDecoder(messageConverters);
        }
    }

    @Bean(name = "com.suchtool.nicelog.niceLogProcessDefaultImpl")
    @ConditionalOnMissingBean(NiceLogProcess.class)
    public NiceLogProcessDefaultImpl niceLogProcessDefaultImpl() {
        return new NiceLogProcessDefaultImpl();
    }
}
