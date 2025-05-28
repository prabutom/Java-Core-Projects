package com.logging.framework.core;

import java.time.Instant;
import java.util.Map;

public class LogEntry {
    private final Instant timestamp;
    private final LogLevel level;
    private final String loggerName;
    private final String message;
    private final Throwable throwable;
    private final String correlationId;
    private final String applicationName;
    private final String organizationName;
    private final String environment;
    private final Map<String, Object> context;

    // Constructor, getters omitted for brevity
    // (Would include all these in actual implementation)

    public LogEntry(Instant timestamp, LogLevel level, String loggerName, String message, Throwable throwable, String correlationId, String applicationName, String organizationName, String environment, Map<String, Object> context) {
        this.timestamp = timestamp;
        this.level = level;
        this.loggerName = loggerName;
        this.message = message;
        this.throwable = throwable;
        this.correlationId = correlationId;
        this.applicationName = applicationName;
        this.organizationName = organizationName;
        this.environment = environment;
        this.context = context;
    }

    public String getCorrelationId() {
        return correlationId;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public LogLevel getLevel() {
        return level;
    }

    public String getLoggerName() {
        return loggerName;
    }

    public String getMessage() {
        return message;
    }

    public Throwable getThrowable() {
        return throwable;
    }

    public String getApplicationName() {
        return applicationName;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public String getEnvironment() {
        return environment;
    }

    public Map<String, Object> getContext() {
        return context;
    }
}