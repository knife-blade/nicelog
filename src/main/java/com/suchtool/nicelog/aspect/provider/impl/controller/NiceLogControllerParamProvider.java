package com.suchtool.nicelog.aspect.provider.impl.controller;

import com.suchtool.nicelog.aspect.provider.NiceLogParamProvider;
import com.suchtool.nicelog.constant.NiceLogEntryTypeEnum;
import com.suchtool.nicelog.property.NiceLogProperty;
import com.suchtool.nicelog.util.servlet.NiceLogServletUtil;
import com.suchtool.nicetool.util.base.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.lang.reflect.Method;

/**
 * Controller参数提供者
 */
@Slf4j
public class NiceLogControllerParamProvider extends NiceLogParamProvider {
    private final NiceLogProperty niceLogProperty;

    private final NiceLogServletUtil niceLogServletUtil;

    public NiceLogControllerParamProvider(NiceLogProperty niceLogProperty,
                                          NiceLogServletUtil niceLogServletUtil) {
        super(niceLogProperty);
        this.niceLogProperty = niceLogProperty;
        this.niceLogServletUtil = niceLogServletUtil;
    }

    @Override
    public String provideEntryType() {
        return NiceLogEntryTypeEnum.CONTROLLER.name();
    }

    @Override
    public String provideEntry(Method method) {
        return ServletUriComponentsBuilder.fromCurrentRequest().build().getPath();
    }

    @Override
    public String provideRequestHeader() {
        ServletRequestAttributes servletRequestAttributes =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (servletRequestAttributes == null) {
            return null;
        }
        return JsonUtil.toJsonString(niceLogServletUtil.buildRequestHeaders());
    }

    @Override
    public String provideResponseHeader() {
        ServletRequestAttributes servletRequestAttributes =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (servletRequestAttributes == null) {
            return null;
        }
        return JsonUtil.toJsonString(niceLogServletUtil.buildResponseHeaders());
    }
}