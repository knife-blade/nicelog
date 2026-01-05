package com.suchtool.nicelog.util.servlet;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

/**
 * Servlet工具（区分SpringBoot2和SpringBoot3）
 */
public class NiceLogServletUtil {
    @Autowired(required = false)
    private NiceLogServletUtilJavax niceLogServletUtilJavax;

    @Autowired(required = false)
    private NiceLogServletUtilJakarta niceLogServletUtilJakarta;

    public String readTraceIdFromHeader() {
        if (niceLogServletUtilJavax != null) {
            return niceLogServletUtilJavax.readTraceIdFromHeader();
        }

        if (niceLogServletUtilJakarta != null) {
            return niceLogServletUtilJakarta.readTraceIdFromHeader();
        }

        return null;
    }

    public Map<String, String> buildRequestHeaders() {
        if (niceLogServletUtilJavax != null) {
            return niceLogServletUtilJavax.buildRequestHeaders();
        }

        if (niceLogServletUtilJakarta != null) {
            return niceLogServletUtilJakarta.buildRequestHeaders();
        }

        return null;
    }

    public Map<String, String> buildResponseHeaders() {
        if (niceLogServletUtilJavax != null) {
            return niceLogServletUtilJavax.buildResponseHeaders();
        }

        if (niceLogServletUtilJakarta != null) {
            return niceLogServletUtilJakarta.buildResponseHeaders();
        }

        return null;
    }
}
