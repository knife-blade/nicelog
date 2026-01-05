package com.suchtool.nicelog.constant;

import lombok.Getter;

@Getter
public enum NiceLogLogLevelEnum {
    TRACE("追踪"),
    DEBUG("调试"),
    INFO("信息"),
    WARN("警告"),
    ERROR("错误"),
    ;

    private final String name;

    NiceLogLogLevelEnum(String name) {
        this.name = name;
    }
}
