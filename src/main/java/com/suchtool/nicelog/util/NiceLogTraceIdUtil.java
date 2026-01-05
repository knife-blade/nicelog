package com.suchtool.nicelog.util;

import com.suchtool.nicelog.util.log.context.NiceLogContext;
import com.suchtool.nicelog.util.log.context.NiceLogContextThreadLocal;
import com.suchtool.nicelog.util.servlet.NiceLogServletUtil;
import com.suchtool.nicetool.util.spring.ApplicationContextHolder;
import org.apache.skywalking.apm.toolkit.trace.TraceContext;
import org.springframework.util.StringUtils;

import java.util.UUID;

/**
 * traceId工具类
 */
public class NiceLogTraceIdUtil {
    public static String readTraceId() {
        String traceId = null;

        NiceLogContext niceLogContext = NiceLogContextThreadLocal.read();
        if (niceLogContext != null) {
            traceId = niceLogContext.getTraceId();
            if (!StringUtils.hasText(traceId)) {
                traceId = readOrCreateTraceId();
                niceLogContext.setTraceId(traceId);
            }
        }

        if (!StringUtils.hasText(traceId)) {
            traceId = readOrCreateTraceId();
        }

        return traceId;
    }

    private static String readOrCreateTraceId() {
        String traceId = null;

        // 如果header里有traceId，则取出
        if (ApplicationContextHolder.getContext().getBeansOfType(NiceLogServletUtil.class).size() > 0) {
            NiceLogServletUtil niceLogServletUtil = ApplicationContextHolder.getContext().getBean(NiceLogServletUtil.class);
            traceId = niceLogServletUtil.readTraceIdFromHeader();
        }

        if (!StringUtils.hasText(traceId)) {
            // 如果使用了skywalking，则使用它的traceId
            String skywalkingTraceId = TraceContext.traceId();
            if (StringUtils.hasText(skywalkingTraceId)) {
                int length = skywalkingTraceId.length();

                // 如果太短，说明有问题。正常traceId超过25
                if (length > 25) {
                    traceId = skywalkingTraceId;
                }
            }
        }

        if (!StringUtils.hasText(traceId)) {
            // 通过uuid创建一个traceId
            traceId = UUID.randomUUID().toString().replace("-", "");
        }

        return traceId;
    }
}
