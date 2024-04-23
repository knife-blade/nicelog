package com.suchtool.nicelog.constant;

public interface NiceLogPointcutExpression {
    String CONTROLLER_LOG_ASPECT = "@within(org.springframework.web.bind.annotation.RestController)";
    String FEIGN_LOG_ASPECT = "@within(org.springframework.cloud.openfeign.FeignClient)";
    String KAFKA_LOG_ASPECT = "@annotation(org.springframework.kafka.annotation.KafkaListener)";
    String RABBIT_MQ_LOG_ASPECT = "@annotation(org.springframework.amqp.rabbit.annotation.RabbitListener)";
    String ROCKET_MQ_LOG_ASPECT = "@within(org.apache.rocketmq.spring.annotation.RocketMQMessageListener)";
    String SCHEDULED_LOG_ASPECT = "@annotation(org.springframework.scheduling.annotation.Scheduled)";
    String XXL_JOB_LOG_ASPECT = "@annotation(com.xxl.job.core.handler.annotation.XxlJob)";
    String NICE_LOG_ANNOTATION_ASPECT = "(@annotation(com.suchtool.nicelog.annotation.NiceLog)" +
            " || @within(com.suchtool.nicelog.annotation.NiceLog))";
}
