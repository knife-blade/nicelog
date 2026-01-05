package com.suchtool.nicelog.aspect.provider.impl.controller;

import com.suchtool.nicelog.property.NiceLogProperty;
import com.suchtool.nicelog.util.servlet.NiceLogServletUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;

/**
 * swagger的v3
 */
@Slf4j
public class NiceLogControllerParamProviderSwagger3 extends NiceLogControllerParamProvider {

    public NiceLogControllerParamProviderSwagger3(NiceLogProperty niceLogProperty,
                                                  NiceLogServletUtil niceLogServletUtil) {
        super(niceLogProperty, niceLogServletUtil);
    }

    @Override
    public String provideEntryClassTag(Method method) {
        return provideClassTag(method);
    }

    @Override
    public String provideEntryMethodTag(Method method) {
        return provideMethodTag(method);
    }

    @Override
    public String provideClassTag(Method method) {
        String classTag = super.provideClassTag(method);
        if (!StringUtils.hasText(classTag)) {
            Class<?> declaringClass = method.getDeclaringClass();
            if (declaringClass.isAnnotationPresent(Tag.class)) {
                Tag api = method.getDeclaringClass().getAnnotation(Tag.class);
                classTag = api.name();
            }
        }

        return classTag;
    }

    @Override
    public String provideMethodTag(Method method) {
        String methodTag = super.provideMethodTag(method);
        if (!StringUtils.hasText(methodTag)) {
            if (method.isAnnotationPresent(Operation.class)) {
                Operation operation = method.getAnnotation(Operation.class);
                String summary = operation.summary();
                if (StringUtils.hasText(summary)) {
                    return methodTag;
                }

                String[] tags = operation.tags();
                methodTag = String.join("+", tags);
                if (StringUtils.hasText(methodTag)) {
                    return methodTag;
                }
            }
        }

        return methodTag;
    }
}