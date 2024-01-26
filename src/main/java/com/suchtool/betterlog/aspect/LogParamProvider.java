package com.suchtool.betterlog.aspect;

import com.suchtool.betterlog.constant.AspectTypeEnum;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * 日志参数提供者
 */
public interface LogParamProvider {
    AspectTypeEnum provideType();

    String provideEntry();

    default String provideClassName(Method method) {
        return method.getDeclaringClass().getName();
    }

    default String provideClassTag(Method method) {
        return null;
    }

    default String provideMethodName(Method method) {
        return method.getName();
    }

    default String provideMethodTag(Method method) {
        return null;
    }

    default Object provideParam(Object[] args) {
        if (args == null) {
            return null;
        }

        if (args.length == 1) {
            return args[0];
        }

        Map<String, Object> map = new HashMap<>();
        int i = 1;
        for (Object arg : args) {
            String key = "arg" + i;
            map.put(key, arg);
            i++;
        }
        return map;
    }
}
