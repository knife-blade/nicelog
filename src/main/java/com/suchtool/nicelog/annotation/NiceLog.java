package com.suchtool.nicelog.annotation;

import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface NiceLog {
    /**
     * 指定类的tag
     */
    String value() default "";
}
