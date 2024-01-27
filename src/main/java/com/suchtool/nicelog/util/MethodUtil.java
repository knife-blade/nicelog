package com.suchtool.nicelog.util;

import java.lang.reflect.Method;

public class MethodUtil {
    public static String parseMethodDetail(Method method) {
        // 方法全名（包含返回值、类、方法名、参数类型等）
        String methodGenericString = method.toGenericString();
        // 去掉返回值信息，只取类、方法名、参数类型等
        String[] strings = methodGenericString.split(" ");
        return strings[strings.length - 1];
    }
}
