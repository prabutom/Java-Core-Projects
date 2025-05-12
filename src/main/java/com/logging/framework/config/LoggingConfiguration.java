package com.logging.framework.config;

import com.logging.framework.core.LogWriter;
import com.logging.framework.writers.ConsoleLogWriter;
import com.logging.framework.writers.FileLogWriter;
import com.logging.framework.formatters.JsonLogFormatter;
import com.logging.framework.formatters.TextLogFormatter;

public class LoggingConfiguration {

    private LogWriter logWriter;
    private boolean includeStackTrace = true;
    private boolean includeContext = true;

    public LoggingConfiguration() {
        // Default to console writer with text formatter
        this.logWriter = new ConsoleLogWriter(new TextLogFormatter());
    }

    public LoggingConfiguration withConsoleOutput(boolean useJsonFormat) {
        if (useJsonFormat) {
            this.logWriter = new ConsoleLogWriter(new JsonLogFormatter());
        } else {
            this.logWriter = new ConsoleLogWriter(new TextLogFormatter());
        }
        return this;
    }

    public LoggingConfiguration withFileOutput(String filePath, boolean useJsonFormat) {
        if (useJsonFormat) {
            this.logWriter = new FileLogWriter(filePath, new JsonLogFormatter());
        } else {
            this.logWriter = new FileLogWriter(filePath, new TextLogFormatter());
        }
        return this;
    }

    public LoggingConfiguration withCustomWriter(LogWriter logWriter) {
        this.logWriter = logWriter;
        return this;
    }

    public LoggingConfiguration includeStackTrace(boolean include) {
        this.includeStackTrace = include;
        return this;
    }

    public LoggingConfiguration includeContext(boolean include) {
        this.includeContext = include;
        return this;
    }

    // Getters
    public LogWriter getLogWriter() {
        return logWriter;
    }

    public boolean isIncludeStackTrace() {
        return includeStackTrace;
    }

    public boolean isIncludeContext() {
        return includeContext;
    }
}