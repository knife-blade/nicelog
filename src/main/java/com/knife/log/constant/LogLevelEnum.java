package com.knife.log.constant;

import lombok.Getter;

@Getter
public enum LogLevelEnum {
    INFO("信息"),
    WARNING("警告"),
    ERROR("错误"),
    ;

    private final String name;

    LogLevelEnum(String name) {
        this.name = name;
    }
}
