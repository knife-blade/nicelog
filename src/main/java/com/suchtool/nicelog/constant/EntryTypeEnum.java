package com.suchtool.nicelog.constant;

import lombok.Getter;

@Getter
public enum EntryTypeEnum {
    MANUAL("手动"),
    CONTROLLER("接口"),
    RABBIT_MQ("RabbitMQ"),
    XXL_JOB("XXL-JOB"),
    NICE_LOG_ANNOTATION("NiceLog注解"),
    FEIGN("Feign"),
    ROCKETMQ("RocketMQ"),
    KAFKA("Kafka"),
    ;

    private final String name;

    EntryTypeEnum(String name) {
        this.name = name;
    }
}
