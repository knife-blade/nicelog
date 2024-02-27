package com.suchtool.nicelog.aspect.impl.feign;

import com.suchtool.nicelog.constant.TraceIdConstant;
import com.suchtool.nicelog.util.NiceLogTraceIdUtil;
import feign.RequestInterceptor;
import feign.RequestTemplate;

public class FeignLogRequestInterceptor implements RequestInterceptor {
    @Override
    public void apply(RequestTemplate requestTemplate) {
        requestTemplate.header(TraceIdConstant.TRACE_ID_HEADER, NiceLogTraceIdUtil.readTraceId());
    }
}
