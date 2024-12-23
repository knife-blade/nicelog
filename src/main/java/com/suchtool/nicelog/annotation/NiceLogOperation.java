package com.suchtool.nicelog.annotation;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface NiceLogOperation {
    /**
     * 指定方法的tag
     */
    String value() default "";

    /**
     * 指定业务单号（SpEL）
     * <p>如果参数是对象，这样写：#对象名.字段名，例如：#user.userName</p>
     * <p>如果参数不是对象，这样写：#字段名。例如：#orderNo</p>
     */
    String businessNoSpEL() default "";
}
