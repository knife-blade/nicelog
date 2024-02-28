package com.suchtool.nicelog.aspect.impl.feign;

import com.suchtool.nicelog.property.NiceLogProperty;
import com.suchtool.nicelog.util.NiceLogTraceIdUtil;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.beans.factory.annotation.Autowired;

public class FeignLogRequestInterceptor implements RequestInterceptor {
    @Autowired
    private NiceLogProperty niceLogProperty;
    @Override
    public void apply(RequestTemplate requestTemplate) {
        requestTemplate.header(
                niceLogProperty.getFeignTraceIdHeader(),
                NiceLogTraceIdUtil.readTraceId());
    }
}
