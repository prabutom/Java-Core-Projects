package com.logging.framework.formatters;

import com.logging.framework.core.LogEntry;
import java.time.format.DateTimeFormatter;
import java.util.Map;

public class TextLogFormatter implements LogFormatter {

    private static final DateTimeFormatter TIMESTAMP_FORMATTER =
            DateTimeFormatter.ISO_INSTANT;

    @Override
    public String format(LogEntry entry) {
        StringBuilder sb = new StringBuilder();

        sb.append(TIMESTAMP_FORMATTER.format(entry.getTimestamp()))
                .append(" [")
                .append(entry.getLevel())
                .append("] ")
                .append(entry.getLoggerName())
                .append(" - ")
                .append(entry.getMessage());

        if (entry.getThrowable() != null) {
            sb.append("\n").append(entry.getThrowable().toString());
            // Would include stack trace if configured
        }

        // Add context
        if (!entry.getContext().isEmpty()) {
            sb.append("\nContext:");
            for (Map.Entry<String, Object> contextEntry : entry.getContext().entrySet()) {
                sb.append("\n  ")
                        .append(contextEntry.getKey())
                        .append(": ")
                        .append(contextEntry.getValue());
            }
        }

        // Add application context
        sb.append("\nApplication: ").append(entry.getApplicationName())
                .append(", Organization: ").append(entry.getOrganizationName())
                .append(", Environment: ").append(entry.getEnvironment());

        return sb.toString();
    }
}
