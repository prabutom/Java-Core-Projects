package com.logging.framework.formatters;

import com.logging.framework.core.LogEntry;

/**
 * Interface for formatting log entries into different output formats.
 */
public interface LogFormatter {

    /**
     * Formats a log entry into a string representation.
     *
     * @param entry The log entry to format
     * @return The formatted log message as a string
     */
    String format(LogEntry entry);
}
