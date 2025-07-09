package com.suchtool.nicelog.constant;

import lombok.Getter;

@Getter
public enum EntryTypeEnum {
    MANUAL("手动"),
    CONTROLLER("接口"),
    RABBIT_MQ("RabbitMQ"),
    ROCKETMQ("RocketMQ"),
    KAFKA("Kafka"),
    XXL_JOB("XXL-JOB"),
    NICE_LOG_ANNOTATION("NiceLog注解"),
    FEIGN("Feign"),
    SCHEDULED("Scheduled注解")
    ;

    private final String description;

    EntryTypeEnum(String description) {
        this.description = description;
    }
}
