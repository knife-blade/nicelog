package com.suchtool.betterlog.constant;

import lombok.Getter;

@Getter
public enum DirectionTypeEnum {
    IN("进入"),
    INNER("内部"),
    OUT("返回"),
    ;

    private final String name;

    DirectionTypeEnum(String name) {
        this.name = name;
    }
}
