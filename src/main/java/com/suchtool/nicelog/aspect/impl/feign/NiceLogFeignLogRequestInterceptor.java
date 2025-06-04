package com.suchtool.nicelog.aspect.impl.feign;

import com.suchtool.nicelog.property.NiceLogProperty;
import com.suchtool.nicelog.util.NiceLogTraceIdUtil;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.util.StringUtils;

public class NiceLogFeignLogRequestInterceptor implements RequestInterceptor, Ordered {
    @Autowired
    private NiceLogProperty niceLogProperty;

    private final int order;

    public NiceLogFeignLogRequestInterceptor(int order) {
        this.order = order;
    }

    @Override
    public void apply(RequestTemplate requestTemplate) {
        if (niceLogProperty.getEnableFeignTraceIdRequestHeader() != null
            && niceLogProperty.getEnableFeignTraceIdRequestHeader()) {
            if (StringUtils.hasText(niceLogProperty.getFeignTraceIdRequestHeader())) {
                requestTemplate.header(
                        niceLogProperty.getFeignTraceIdRequestHeader(),
                        NiceLogTraceIdUtil.readTraceId());
            }
        }
    }

    @Override
    public int getOrder() {
        return order;
    }
}
