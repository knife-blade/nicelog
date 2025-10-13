package com.suchtool.nicelog.configuration;

import com.suchtool.nicelog.aspect.NiceLogAspectDispatcher;
import com.suchtool.nicelog.aspect.impl.*;
import com.suchtool.nicelog.aspect.impl.feign.NiceLogFeignLogRequestInterceptor;
import com.suchtool.nicelog.aspect.impl.feign.NiceLogFeignLogResponseDecoder;
import com.suchtool.nicelog.process.NiceLogDetailProcess;
import com.suchtool.nicelog.process.NiceLogProcess;
import com.suchtool.nicelog.process.impl.NiceLogDetailProcessDefaultImpl;
import com.suchtool.nicelog.process.impl.NiceLogProcessDefaultImpl;
import com.suchtool.nicelog.property.NiceLogAspectOrderProperty;
import com.suchtool.nicelog.property.NiceLogProcessProperty;
import com.suchtool.nicelog.property.NiceLogProperty;
import com.suchtool.nicelog.runner.NiceLogApplicationRunner;
import com.xxl.job.core.handler.annotation.XxlJob;
import feign.codec.Decoder;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.boot.autoconfigure.condition.*;
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

    @Bean(name = "com.suchtool.nicelog.niceLogProcessProperty")
    @ConfigurationProperties(prefix = "suchtool.nicelog.process")
    public NiceLogProcessProperty niceLogProcessProperty() {
        return new NiceLogProcessProperty();
    }

    @Bean(name = "com.suchtool.nicelog.niceLogAspectDispatcher")
    public NiceLogAspectDispatcher niceLogAspectDispatcher() {
        return new NiceLogAspectDispatcher();
    }

    @ConditionalOnProperty(name = "suchtool.nicelog.enable-controller-log", havingValue = "true", matchIfMissing = true)
    @Configuration(value = "com.suchtool.nicelog.niceLogControllerAspectConfiguration", proxyBeanMethods = false)
    protected static class NiceLogControllerAspectConfiguration {
        @Bean(name = "com.suchtool.nicelog.niceLogControllerLogAspect")
        public NiceLogControllerLogAspect niceLogControllerLogAspect(
                NiceLogAspectOrderProperty niceLogAspectOrderProperty,
                NiceLogProperty niceLogProperty) {
            int order = niceLogAspectOrderProperty.getControllerLogOrder();
            return new NiceLogControllerLogAspect(order, niceLogProperty);
        }
    }

    @ConditionalOnClass(XxlJob.class)
    @ConditionalOnProperty(name = "suchtool.nicelog.enable-xxl-job-log", havingValue = "true", matchIfMissing = true)
    @Configuration(value = "com.suchtool.nicelog.niceLogXxlJobAspectConfiguration", proxyBeanMethods = false)
    protected static class XxlJobAspectConfiguration {
        @Bean(name = "com.suchtool.nicelog.niceLogXxlJobLogAspect")
        public NiceLogXxlJobLogAspect niceLogXxlJobLogAspect(
                NiceLogAspectOrderProperty niceLogAspectOrderProperty,
                NiceLogProperty niceLogProperty) {
            int order = niceLogAspectOrderProperty.getXxlJobLogOrder();
            return new NiceLogXxlJobLogAspect(order, niceLogProperty);
        }
    }

    @ConditionalOnBean(ScheduledAnnotationBeanPostProcessor.class)
    @ConditionalOnProperty(name = "suchtool.nicelog.enable-scheduled-log", havingValue = "true", matchIfMissing = true)
    @Configuration(value = "com.suchtool.nicelog.niceLogScheduledAspectConfiguration", proxyBeanMethods = false)
    protected static class ScheduledAspectConfiguration {
        @Bean(name = "com.suchtool.nicelog.niceLogScheduledLogAspect")
        public NiceLogScheduledLogAspect niceLogScheduledLogAspect(
                NiceLogAspectOrderProperty niceLogAspectOrderProperty,
                NiceLogProperty niceLogProperty) {
            int order = niceLogAspectOrderProperty.getScheduledLogOrder();
            return new NiceLogScheduledLogAspect(order, niceLogProperty);
        }
    }

    @ConditionalOnProperty(name = "suchtool.nicelog.enable-nice-log-annotation-log", havingValue = "true", matchIfMissing = true)
    @Configuration(value = "com.suchtool.nicelog.niceLogAnnotationAspectConfiguration", proxyBeanMethods = false)
    protected static class NiceLogAnnotationAspectConfiguration {
        @Bean(name = "com.suchtool.nicelog.niceLogAnnotationLogAspect")
        public NiceLogAnnotationLogAspect niceLogAnnotationLog(
                NiceLogAspectOrderProperty niceLogAspectOrderProperty,
                NiceLogProperty niceLogProperty) {
            int order = niceLogAspectOrderProperty.getNiceLogAnnotationLogOrder();
            return new NiceLogAnnotationLogAspect(order, niceLogProperty);
        }
    }

    @ConditionalOnProperty(name = "suchtool.nicelog.enable-rabbit-mq-log", havingValue = "true", matchIfMissing = true)
    @ConditionalOnClass(RabbitListener.class)
    @Configuration(value = "com.suchtool.nicelog.niceLogRabbitMQAspectConfiguration", proxyBeanMethods = false)
    protected static class RabbitMQAspectConfiguration {
        @Bean(name = "com.suchtool.nicelog.niceLogRabbitMQLogAspect")
        public NiceLogRabbitMQLogAspect niceLogRabbitMQLogAspect(
                NiceLogAspectOrderProperty niceLogAspectOrderProperty,
                NiceLogProperty niceLogProperty) {
            int order = niceLogAspectOrderProperty.getRabbitMQLogOrder();
            return new NiceLogRabbitMQLogAspect(order, niceLogProperty);
        }
    }

    @ConditionalOnClass(RocketMQMessageListener.class)
    @ConditionalOnProperty(name = "suchtool.nicelog.enable-rocket-mq-log", havingValue = "true", matchIfMissing = true)
    @Configuration(value = "com.suchtool.nicelog.niceLogRocketMQAspectConfiguration", proxyBeanMethods = false)
    protected static class RocketMQAspectConfiguration {
        @Bean(name = "com.suchtool.nicelog.niceLogRocketMQLogAspect")
        public NiceLogRocketMQLogAspect niceLogRocketMQLogAspect(
                NiceLogAspectOrderProperty niceLogAspectOrderProperty,
                NiceLogProperty niceLogProperty) {
            int order = niceLogAspectOrderProperty.getRocketMQLogOrder();
            return new NiceLogRocketMQLogAspect(order, niceLogProperty);
        }
    }

    @ConditionalOnClass(KafkaListener.class)
    @ConditionalOnProperty(name = "suchtool.nicelog.enable-kafka-log", havingValue = "true", matchIfMissing = true)
    @Configuration(value = "com.suchtool.nicelog.niceLogKafkaAspectConfiguration", proxyBeanMethods = false)
    protected static class KafkaAspectConfiguration {
        @Bean(name = "com.suchtool.nicelog.niceLogKafkaMQLogAspect")
        public NiceLogKafkaLogAspect niceLogKafkaMQLogAspect(
                NiceLogAspectOrderProperty niceLogAspectOrderProperty,
                NiceLogProperty niceLogProperty) {
            int order = niceLogAspectOrderProperty.getKafkaLogOrder();
            return new NiceLogKafkaLogAspect(order, niceLogProperty);
        }
    }

    @ConditionalOnClass(FeignClient.class)
    @ConditionalOnProperty(name = "suchtool.nicelog.enable-feign-log", havingValue = "true", matchIfMissing = true)
    @Configuration(value = "com.suchtool.nicelog.niceLogFeignLogAspectConfiguration", proxyBeanMethods = false)
    protected static class FeignLogAspectConfiguration {
        @Bean(name = "com.suchtool.nicelog.niceLogFeignLogAspect")
        public NiceLogFeignLogAspect niceLogNiceLogAnnotationLog(
                NiceLogAspectOrderProperty niceLogAspectOrderProperty,
                NiceLogProperty niceLogProperty) {
            int order = niceLogAspectOrderProperty.getFeignLogOrder();
            return new NiceLogFeignLogAspect(order, niceLogProperty);
        }

        @Bean(name = "com.suchtool.nicelog.niceLogFeignLogRequestInterceptor")
        public NiceLogFeignLogRequestInterceptor niceLogFeignLogRequestInterceptor(
                NiceLogAspectOrderProperty niceLogAspectOrderProperty) {
            int order = niceLogAspectOrderProperty.getFeignRequestInterceptorOrder();
            return new NiceLogFeignLogRequestInterceptor(order);
        }

        @Bean(name = "com.suchtool.nicelog.niceLogFeignLogResponseDecoder")
        @ConditionalOnMissingBean(Decoder.class)
        public NiceLogFeignLogResponseDecoder niceLogFeignLogResponseDecoder(
                ObjectFactory<HttpMessageConverters> messageConverters) {
            return new NiceLogFeignLogResponseDecoder(messageConverters);
        }
    }

    @Bean(name = "com.suchtool.nicelog.niceLogProcess")
    @ConditionalOnMissingBean(NiceLogProcess.class)
    public NiceLogProcessDefaultImpl niceLogProcess() {
        return new NiceLogProcessDefaultImpl();
    }

    @Bean(name = "com.suchtool.nicelog.niceLogDetailProcess")
    @ConditionalOnMissingBean(NiceLogDetailProcess.class)
    public NiceLogDetailProcessDefaultImpl niceLogDetailProcess() {
        return new NiceLogDetailProcessDefaultImpl();
    }

    @Bean(name = "com.suchtool.nicelog.niceLogApplicationRunner")
    @ConditionalOnExpression("${suchtool.nicelog.logback-enabled:false}")
    public NiceLogApplicationRunner niceLogApplicationRunner(NiceLogProperty niceLogProperty) {
        return new NiceLogApplicationRunner(niceLogProperty);
    }
}
