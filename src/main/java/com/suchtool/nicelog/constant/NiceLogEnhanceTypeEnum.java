package com.suchtool.nicelog.constant;

import lombok.Getter;

/**
 * 增强类型
 */
@Getter
public enum NiceLogEnhanceTypeEnum {
    LOGBACK("Logback"),
    ;

    private final String description;

    NiceLogEnhanceTypeEnum(String description) {
        this.description = description;
    }
}
