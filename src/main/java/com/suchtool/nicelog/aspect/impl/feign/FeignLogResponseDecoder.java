package com.suchtool.nicelog.aspect.impl.feign;

import com.suchtool.nicelog.util.log.context.NiceLogContext;
import com.suchtool.nicelog.util.log.context.NiceLogContextThreadLocal;
import feign.FeignException;
import feign.Response;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.support.SpringDecoder;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;

public class FeignLogResponseDecoder extends SpringDecoder {

    public FeignLogResponseDecoder(ObjectFactory<HttpMessageConverters> messageConverters) {
        super(messageConverters);
    }

    @Override
    public Object decode(Response response, Type type) throws IOException, FeignException {
        Response.Body body = response.body();
        String bodyString = StreamUtils.copyToString(body.asInputStream(), StandardCharsets.UTF_8);

        NiceLogContext niceLogContext = NiceLogContextThreadLocal.read();
        if (niceLogContext != null) {
            niceLogContext.setFeignOriginResponseBody(bodyString);
        }

        // body流只能读取一次，所以必须再次构造
        return super.decode(response.toBuilder().body(bodyString, StandardCharsets.UTF_8).build(), type);
    }
}
