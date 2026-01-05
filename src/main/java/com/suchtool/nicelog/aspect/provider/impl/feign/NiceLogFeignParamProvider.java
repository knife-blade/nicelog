package com.suchtool.nicelog.aspect.provider.impl.feign;

import com.suchtool.nicelog.aspect.provider.NiceLogParamProvider;
import com.suchtool.nicelog.constant.NiceLogEntryTypeEnum;
import com.suchtool.nicelog.property.NiceLogProperty;
import com.suchtool.nicetool.util.web.http.url.HttpUrlUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.web.bind.annotation.RequestMapping;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

/**
 * Feign参数提供者
 */
@Slf4j
public class NiceLogFeignParamProvider extends NiceLogParamProvider {
    private final StandardEnvironment standardEnvironment;

    public NiceLogFeignParamProvider(NiceLogProperty niceLogProperty,
                                     StandardEnvironment standardEnvironment) {
        super(niceLogProperty);
        this.standardEnvironment = standardEnvironment;
    }

    @Override
    public String provideEntryType() {
        return NiceLogEntryTypeEnum.FEIGN.name();
    }

    @Override
    public String provideEntry(Method method) {
        String finalUrl = null;

        Class<?> cls = method.getDeclaringClass();
        FeignClient feignClient = cls.getAnnotation(FeignClient.class);
        String url = standardEnvironment.resolvePlaceholders(feignClient.url());
        String path = standardEnvironment.resolvePlaceholders(feignClient.path());

        String urlPrefix = null;
        RequestMapping requestMapping = AnnotatedElementUtils.getMergedAnnotation(method, RequestMapping.class);
        if (requestMapping != null) {
            String[] value = requestMapping.value();
            urlPrefix = value[0];
        }

        List<String> urlList = Arrays.asList(url, path, urlPrefix);
        finalUrl = HttpUrlUtil.joinUrl(urlList, false);

        return finalUrl;
    }
}