package com.suchtool.nicelog.constant;

import lombok.Getter;

/**
 * 增强类型
 */
@Getter
public enum EnhanceTypeEnum {
    LOGBACK("Logback"),
    ;

    private final String description;

    EnhanceTypeEnum(String description) {
        this.description = description;
    }
}
