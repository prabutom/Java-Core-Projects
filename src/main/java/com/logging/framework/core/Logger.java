package com.logging.framework.core;

public interface Logger<T extends Logger<T>> {
    void trace(String message);
    void debug(String message);
    void info(String message);
    void warn(String message);
    void error(String message);
    void error(String message, Throwable throwable);
    void fatal(String message);
    void fatal(String message, Throwable throwable);

    T with(String key, Object value);
    T withMethod(String methodName);
    T withClass(Class<?> clazz);
}