package com.suchtool.nicelog.util.servlet;

import com.suchtool.nicelog.property.NiceLogProperty;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;

public class NiceLogServletUtilJakarta {
    private final HttpServletRequest httpServletRequest;

    private final HttpServletResponse httpServletResponse;

    private final NiceLogProperty niceLogProperty;

    public NiceLogServletUtilJakarta(HttpServletRequest httpServletRequest,
                                   HttpServletResponse httpServletResponse,
                                   NiceLogProperty niceLogProperty) {
        this.httpServletRequest = httpServletRequest;
        this.niceLogProperty = niceLogProperty;
        this.httpServletResponse = httpServletResponse;
    }

    public String readTraceIdFromHeader() {
        return httpServletRequest.getHeader(niceLogProperty.getFeignTraceIdRequestHeader());
    }

    public Map<String, String> buildRequestHeaders() {
        Map<String, String> headersMap = new HashMap<>();

        // 组装请求头
        Enumeration<String> requestHeaderNames = httpServletRequest.getHeaderNames();
        while (requestHeaderNames.hasMoreElements()) {
            String headerName = requestHeaderNames.nextElement();
            Enumeration<String> headerValues = httpServletRequest.getHeaders(headerName);
            StringJoiner headerValuesJoiner = new StringJoiner(niceLogProperty.getControllerHeaderSeparator());
            while (headerValues.hasMoreElements()) {
                headerValuesJoiner.add(headerValues.nextElement());
            }
            headersMap.put(headerName, headerValuesJoiner.toString());
        }

        return headersMap;
    }

    public Map<String, String> buildResponseHeaders() {
        // 创建一个Map来存储请求和响应头
        Map<String, String> headersMap = new HashMap<>();

        Collection<String> headerNames = httpServletResponse.getHeaderNames();
        if (!CollectionUtils.isEmpty(headerNames)) {
            for (String headerName : headerNames) {
                Collection<String> headerValues = httpServletResponse.getHeaders(headerName);
                StringJoiner headerValuesJoiner = new StringJoiner(niceLogProperty.getControllerHeaderSeparator());
                if (!CollectionUtils.isEmpty(headerValues)) {
                    for (String headerValue : headerValues) {
                        headerValuesJoiner.add(headerValue);
                    }
                    headersMap.put(headerName, headerValuesJoiner.toString());
                }
            }
        }

        return headersMap;
    }
}
