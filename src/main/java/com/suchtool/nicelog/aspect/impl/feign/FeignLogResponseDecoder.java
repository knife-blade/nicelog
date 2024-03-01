package com.suchtool.nicelog.aspect.impl.feign;

import com.suchtool.nicelog.util.log.context.feign.NiceLogFeignContextThreadLocal;
import feign.FeignException;
import feign.Response;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.support.SpringDecoder;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;


@Configuration
public class FeignLogResponseDecoder extends SpringDecoder {
    public FeignLogResponseDecoder(ObjectFactory<HttpMessageConverters> messageConverters) {
        super(messageConverters);
    }

    @Override
    public Object decode(final Response response, Type type) throws IOException, FeignException {
        Response.Body body = response.body();
        String bodyString = StreamUtils.copyToString(body.asInputStream(), StandardCharsets.UTF_8);
        // 这里将body保存下来
        NiceLogFeignContextThreadLocal.saveOriginFeignResponseBody(bodyString);

        // body流只能读一次，必须重新封装一下
        Response newResponse = response.toBuilder().body(bodyString, StandardCharsets.UTF_8).build();
        return super.decode(newResponse, type);
    }
}
