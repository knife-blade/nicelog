package com.suchtool.nicelog.aspect.provider.impl.controller;

import com.suchtool.nicelog.property.NiceLogProperty;
import com.suchtool.nicelog.util.servlet.NiceLogServletUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;

/**
 * swagger的v1-v2
 */
@Slf4j
public class NiceLogControllerParamProviderSwagger2 extends NiceLogControllerParamProvider {

    public NiceLogControllerParamProviderSwagger2(NiceLogProperty niceLogProperty,
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
                Tag tag = declaringClass.getAnnotation(Tag.class);
                classTag = tag.name();
                if (StringUtils.hasText(classTag)) {
                    return classTag;
                }
            }

            if (declaringClass.isAnnotationPresent(Api.class)) {
                Api api = declaringClass.getAnnotation(Api.class);
                String[] tags = api.tags();
                String value = api.value();
                String tagJoin = String.join("+", tags);
                if (StringUtils.hasText(tagJoin)) {
                    classTag = tagJoin;
                } else {
                    classTag = value;
                }
            }
        }

        return classTag;
    }

    @Override
    public String provideMethodTag(Method method) {
        String methodTag = super.provideMethodTag(method);
        if (!StringUtils.hasText(methodTag)) {
            if (method.isAnnotationPresent(ApiOperation.class)) {
                methodTag = method.getAnnotation(ApiOperation.class).value();
            }
        }

        return methodTag;
    }
}