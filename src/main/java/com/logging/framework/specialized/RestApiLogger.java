package com.logging.framework.specialized;

import com.logging.framework.core.BaseLogger;
import com.logging.framework.core.LogWriter;
import com.logging.framework.context.ApplicationContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class RestApiLogger extends BaseLogger<RestApiLogger> {

    public RestApiLogger(String name, LogWriter logWriter, ApplicationContext appContext) {
        super(name, logWriter, appContext);
    }

    // No need to override with() methods - they're inherited properly

    public RestApiLogger logRequest(HttpServletRequest request) {
        return this.with("method", request.getMethod())
                .with("path", request.getRequestURI())
                .with("remoteAddr", request.getRemoteAddr());
    }

    public void logResponse(HttpServletRequest request, HttpServletResponse response, long durationMs) {
        this.with("method", request.getMethod())
                .with("path", request.getRequestURI())
                .with("status", response.getStatus())
                .with("durationMs", durationMs)
                .info("HTTP response sent");
    }

    public void logError(HttpServletRequest request, HttpServletResponse response, Throwable error) {
        this.with("method", request.getMethod())
                .with("path", request.getRequestURI())
                .with("status", response.getStatus())
                .error("Error processing HTTP request", error);
    }
}