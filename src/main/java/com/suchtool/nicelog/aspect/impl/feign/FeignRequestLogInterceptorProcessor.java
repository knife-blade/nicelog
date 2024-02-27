package com.suchtool.nicelog.aspect.impl.feign;

import com.suchtool.nicelog.aspect.LogAspectProcessor;
import com.suchtool.nicelog.aspect.LogCommonAspectExecutor;
import com.suchtool.nicelog.constant.EntryTypeEnum;
import com.suchtool.niceutil.util.base.JsonUtil;
import feign.MethodMetadata;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.core.Ordered;

import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class FeignRequestLogInterceptorProcessor extends LogAspectProcessor implements RequestInterceptor, Ordered {
    private final LogCommonAspectExecutor logCommonAspectExecutor;

    private final int order;

    public FeignRequestLogInterceptorProcessor(int order) {
        this.logCommonAspectExecutor = new LogCommonAspectExecutor(this);
        this.order = order;
    }

    @Override
    public void apply(RequestTemplate requestTemplate) {
        MethodMetadata methodMetadata = requestTemplate.methodMetadata();
        Method method = methodMetadata.method();

        String param = buildParam(requestTemplate);

        logCommonAspectExecutor.before(method, null, param);
    }

    @Override
    public int getOrder() {
        return order;
    }

    @Override
    public void returningOrThrowingProcess() {

    }

    @Override
    public EntryTypeEnum provideEntryType() {
        return EntryTypeEnum.FEIGN;
    }

    private String buildParam(RequestTemplate requestTemplate) {
        byte[] body = requestTemplate.body();
        String urlParam = requestTemplate.queryLine();
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("urlParam", urlParam);
        paramMap.put("body", new String(body, StandardCharsets.UTF_8));
        return JsonUtil.toJsonString(paramMap);
    }

}
