package com.suchtool.nicelog.annotation;

import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface NiceLogIgnoreData {
    /**
     * 忽略入参
     */
    boolean ignoreParam() default true;

    /**
     * 忽略返回值
     */
    boolean ignoreReturnValue() default true;
}
