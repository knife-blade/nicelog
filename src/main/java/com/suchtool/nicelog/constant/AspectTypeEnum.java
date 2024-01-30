package com.suchtool.nicelog.constant;

import lombok.Getter;

@Getter
public enum AspectTypeEnum {
    MANUAL("手动"),
    CONTROLLER("接口"),
    RABBIT_MQ("RabbitMQ"),
    XXL_JOB("XXL-JOB"),
    NICE_LOG_ANNOTATION("NiceLog注解")
    ;

    private final String name;

    AspectTypeEnum(String name) {
        this.name = name;
    }
}
