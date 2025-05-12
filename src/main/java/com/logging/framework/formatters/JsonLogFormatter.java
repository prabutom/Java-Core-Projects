package com.logging.framework.formatters;

import com.logging.framework.core.LogEntry;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.util.HashMap;
import java.util.Map;

public class JsonLogFormatter implements LogFormatter {

    private final ObjectMapper objectMapper;

    public JsonLogFormatter() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
        this.objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    }

    @Override
    public String format(LogEntry entry) {
        Map<String, Object> logMap = new HashMap<>();

        logMap.put("timestamp", entry.getTimestamp());
        logMap.put("level", entry.getLevel().name());
        logMap.put("logger", entry.getLoggerName());
        logMap.put("message", entry.getMessage());

        if (entry.getThrowable() != null) {
            Map<String, Object> exceptionMap = new HashMap<>();
            exceptionMap.put("type", entry.getThrowable().getClass().getName());
            exceptionMap.put("message", entry.getThrowable().getMessage());
            // Would include stack trace if configured
            logMap.put("exception", exceptionMap);
        }

        // Add context
        if (!entry.getContext().isEmpty()) {
            logMap.put("context", entry.getContext());
        }

        // Add application context
        logMap.put("application", entry.getApplicationName());
        logMap.put("organization", entry.getOrganizationName());
        logMap.put("environment", entry.getEnvironment());

        try {
            return objectMapper.writeValueAsString(logMap);
        } catch (JsonProcessingException e) {
            return "{\"error\":\"Failed to format log entry\"}";
        }
    }
}
