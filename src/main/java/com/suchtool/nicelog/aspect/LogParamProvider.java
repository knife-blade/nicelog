package com.suchtool.nicelog.aspect;

import com.suchtool.nicelog.constant.AspectTypeEnum;
import com.suchtool.niceutil.util.MethodUtil;

import java.lang.reflect.Method;

/**
 * 日志参数提供者
 */
public interface LogParamProvider {
    AspectTypeEnum provideType();

    String provideEntry(Method method);

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

    default Object provideParam(Object[] args, Method method) {
        return MethodUtil.parseParam(method, args);
    }
}
