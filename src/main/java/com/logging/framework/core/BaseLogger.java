package com.logging.framework.core;

import com.logging.framework.context.AppContext;

import java.lang.management.ManagementFactory;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

public abstract class BaseLogger<T extends BaseLogger<T>> implements Logger<T> {

    private final String name;
    private final LogWriter logWriter;
    private final AppContext appContext;
    protected Map<String, Object> context = new HashMap<>();

    protected BaseLogger(String name, LogWriter logWriter, AppContext appContext) {
        this.name = name;
        this.logWriter = logWriter;
        this.appContext = appContext;
    }

    // Implement all log level methods
/*    @Override
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
    public void fatal(String message, Throwable throwable) { log(LogLevel.FATAL, message, throwable); }*/

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

/*    protected void log(LogLevel level, String message, Throwable throwable) {
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
    }*/
protected void log(LogLevel level, String message, Throwable throwable) {
    LogEntry entry = createLogEntry(level, message, throwable);
    logWriter.write(entry);
}

    private LogEntry createLogEntry(LogLevel level, String message, Throwable throwable) {
        Map<String, Object> contextData = new HashMap<>(context);

        // Add level-specific context information
        switch (level) {
            case TRACE:
                addTraceContext(contextData);
                break;
            case DEBUG:
                addDebugContext(contextData);
                break;
            case INFO:
                // Info level gets minimal additional context
                break;
            case WARN:
                addWarnContext(contextData);
                break;
            case ERROR:
            case FATAL:
                addErrorContext(contextData, throwable);
                break;
        }

        return new LogEntry(
                Instant.now(),
                level,
                name,
                message,
                throwable,
                appContext.getApplicationName(),
                appContext.getOrganizationName(),
                appContext.getEnvironment(),
                contextData
        );
    }

    private void addTraceContext(Map<String, Object> context) {
        // Add finest-grained tracing information
        context.put("thread_state", Thread.currentThread().getState().toString());
        context.put("stack_depth", Thread.currentThread().getStackTrace().length);
        context.put("memory_stats", getMemoryStatistics());
        context.put("loaded_classes", getLoadedClassCount());
        // Add any other very detailed tracing information
    }

    private void addDebugContext(Map<String, Object> context) {
        // Add debug-level information including stack trace
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        // Skip the first few elements which are logging framework internals
        int relevantElements = Math.min(stackTrace.length, 8); // Limit stack trace depth
        String[] simplifiedStackTrace = new String[relevantElements];
        for (int i = 0; i < relevantElements; i++) {
            simplifiedStackTrace[i] = stackTrace[i].toString();
        }
        context.put("stack_trace", simplifiedStackTrace);
        context.put("thread_name", Thread.currentThread().getName());
    }

    private void addWarnContext(Map<String, Object> context) {
        // Add warning-specific context
        context.put("thread_name", Thread.currentThread().getName());
        context.put("timestamp_nanos", System.nanoTime());
    }

    private void addErrorContext(Map<String, Object> context, Throwable throwable) {
        // Add error-specific context
        if (throwable != null) {
            context.put("exception_class", throwable.getClass().getName());
        }
        context.put("available_processors", Runtime.getRuntime().availableProcessors());
        context.put("free_memory", Runtime.getRuntime().freeMemory());
        context.put("total_memory", Runtime.getRuntime().totalMemory());
    }

    // Helper methods
    private Map<String, Long> getMemoryStatistics() {
        Map<String, Long> memoryStats = new HashMap<>();
        Runtime runtime = Runtime.getRuntime();
        memoryStats.put("free_memory", runtime.freeMemory());
        memoryStats.put("total_memory", runtime.totalMemory());
        memoryStats.put("max_memory", runtime.maxMemory());
        memoryStats.put("used_memory", runtime.totalMemory() - runtime.freeMemory());
        return memoryStats;
    }

    private long getLoadedClassCount() {
        return ManagementFactory.getClassLoadingMXBean().getLoadedClassCount();
    }

    // Implement all log level methods with potential for additional level-specific logic
    @Override
    public void trace(String message) {
        log(LogLevel.TRACE, message, null);
    }

    @Override
    public void debug(String message) {
        log(LogLevel.DEBUG, message, null);
    }

    @Override
    public void info(String message) {
        log(LogLevel.INFO, message, null);
    }

    @Override
    public void warn(String message) {
        log(LogLevel.WARN, message, null);
    }

    @Override
    public void error(String message) {
        log(LogLevel.ERROR, message, null);
    }

    @Override
    public void error(String message, Throwable throwable) {
        log(LogLevel.ERROR, message, throwable);
    }

    @Override
    public void fatal(String message) {
        log(LogLevel.FATAL, message, null);
    }

    @Override
    public void fatal(String message, Throwable throwable) {
        log(LogLevel.FATAL, message, throwable);
    }

}