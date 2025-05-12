package com.logging.framework.core;

import com.logging.framework.context.ApplicationContext;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

public abstract class BaseLogger<T extends BaseLogger<T>> implements Logger<T> {

    private final String name;
    private final LogWriter logWriter;
    private final ApplicationContext appContext;
    protected Map<String, Object> context = new HashMap<>();

    protected BaseLogger(String name, LogWriter logWriter, ApplicationContext appContext) {
        this.name = name;
        this.logWriter = logWriter;
        this.appContext = appContext;
    }

    // Implement all log level methods
    @Override
    public void trace(String message) { log(LogLevel.TRACE, message, null); }
    @Override
    public void debug(String message) { log(LogLevel.DEBUG, message, null); }
    @Override
    public void info(String message) { log(LogLevel.INFO, message, null); }
    @Override
    public void warn(String message) { log(LogLevel.WARN, message, null); }
    @Override
    public void error(String message) { log(LogLevel.ERROR, message, null); }
    @Override
    public void error(String message, Throwable throwable) { log(LogLevel.ERROR, message, throwable); }
    @Override
    public void fatal(String message) { log(LogLevel.FATAL, message, null); }
    @Override
    public void fatal(String message, Throwable throwable) { log(LogLevel.FATAL, message, throwable); }

    // Fluent interface methods
    @SuppressWarnings("unchecked")
    public T with(String key, Object value) {
        context.put(key, value);
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    public T withMethod(String methodName) {
        return with("method", methodName);
    }

    @SuppressWarnings("unchecked")
    public T withClass(Class<?> clazz) {
        return with("class", clazz.getName());
    }

    protected void log(LogLevel level, String message, Throwable throwable) {
        LogEntry entry = new LogEntry(
                Instant.now(),
                level,
                name,
                message,
                throwable,
                appContext.getApplicationName(),
                appContext.getOrganizationName(),
                appContext.getEnvironment(),
                new HashMap<>(context)
        );
        logWriter.write(entry);
    }
}