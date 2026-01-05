package com.suchtool.nicelog.constant;

import lombok.Getter;

@Getter
public enum NiceLogDirectionTypeEnum {
    IN("进入"),
    INNER("内部"),
    OUT("返回"),
    ;

    private final String name;

    NiceLogDirectionTypeEnum(String name) {
        this.name = name;
    }
}
