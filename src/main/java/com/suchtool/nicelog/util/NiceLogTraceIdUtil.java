package com.suchtool.nicelog.util;

import com.suchtool.nicelog.constant.TraceIdConstant;
import org.apache.skywalking.apm.toolkit.trace.TraceContext;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

/**
 * traceId工具类
 */
public class NiceLogTraceIdUtil {
    public static String readTraceId() {
        String traceId = null;

        // 如果header里有traceId，则取出
        ServletRequestAttributes servletRequestAttributes =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (servletRequestAttributes != null) {
            HttpServletRequest request = servletRequestAttributes.getRequest();
            String traceIdOfHeader = request.getHeader(TraceIdConstant.TRACE_ID_HEADER);
            if (StringUtils.hasText(traceIdOfHeader)) {
                traceId = traceIdOfHeader;
            }
        }

        // 如果使用了skywalking，则使用它的traceId
        String skywalkingTraceId = TraceContext.traceId();
        if (StringUtils.hasText(skywalkingTraceId)) {
            int length = skywalkingTraceId.length();
            if (length > 25) {
                traceId = skywalkingTraceId;
            }
        }

        // 通过uuid创建一个traceId
        if (!StringUtils.hasText(traceId)) {
            traceId = UUID.randomUUID().toString().replace("-", "");
        }

        return traceId;
    }
}
