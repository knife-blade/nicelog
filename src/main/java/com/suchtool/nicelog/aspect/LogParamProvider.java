package com.suchtool.nicelog.aspect;

import com.suchtool.nicelog.annotation.NiceLog;
import com.suchtool.nicelog.constant.EntryTypeEnum;
import com.suchtool.niceutil.util.reflect.MethodUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.lang.reflect.Method;

/**
 * 日志参数提供者
 */
public interface LogParamProvider {
    EntryTypeEnum provideEntryType();

    default String provideEntry(Method method) {
        return provideClassTag(method);
    }

    default String provideEntryClassTag(Method method) {
        return provideClassTag(method);
    }

    default String provideEntryMethodTag(Method method) {
        return provideMethodTag(method);
    }

    default String provideClassName(Method method) {
        return method.getDeclaringClass().getName();
    }

    default String provideClassTag(Method method) {
        String classTag = null;

        Class<?> declaringClass = method.getDeclaringClass();

        if (declaringClass.isAnnotationPresent(NiceLog.class)) {
            NiceLog niceLog = declaringClass.getAnnotation(NiceLog.class);
            classTag = niceLog.value();
        } else {
            if (EntryTypeEnum.CONTROLLER.equals(provideEntryType())
                    && declaringClass.isAnnotationPresent(Api.class)) {
                Api api = declaringClass.getAnnotation(Api.class);
                String[] tags = api.tags();
                String value = api.value();
                String tagJoin = String.join("+", tags);
                if (tags.length > 0) {
                    classTag = tagJoin;
                } else {
                    classTag = value;
                }
            }
        }

        return classTag;
    }

    default String provideMethodName(Method method) {
        return method.getName();
    }

    default String provideMethodTag(Method method) {
        String methodTag = null;

        if (method.isAnnotationPresent(NiceLog.class)) {
            methodTag = method.getAnnotation(NiceLog.class).value();
        } else if (EntryTypeEnum.CONTROLLER.equals(provideEntryType())
                && method.isAnnotationPresent(ApiOperation.class)) {
            methodTag = method.getAnnotation(ApiOperation.class).value();
        }

        return methodTag;
    }

    default Object provideParam(Object[] args, Method method) {
        return MethodUtil.parseParam(method, args);
    }
}
