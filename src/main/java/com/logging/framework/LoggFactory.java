package com.logging.framework;

import com.logging.framework.config.LoggingConfiguration;
import com.logging.framework.context.AppContext;
import com.logging.framework.core.BaseLogger;
import com.logging.framework.core.LogWriter;
import com.logging.framework.core.Logger;
import com.logging.framework.specialized.DatabaseLogger;
import com.logging.framework.specialized.KafkaLogger;
import com.logging.framework.specialized.RestApiLogger;
import com.logging.framework.writers.AsyncLogWriter;

public class LoggFactory {

    private static LoggingConfiguration config;
    private static AppContext appContext;
    private static AsyncLogWriter asyncWriter;

    public static void initialize(LoggingConfiguration configuration,
                                  AppContext applicationContext) {
        config = configuration;
        appContext = applicationContext;

        if (config.isAsyncLogging()) {
            LogWriter baseWriter = config.getBaseLogWriter(); // Need to add this method
            asyncWriter = new AsyncLogWriter(baseWriter, config.getQueueCapacity());
        }

    }

    public static void shutdown() {
        if (asyncWriter != null) {
            asyncWriter.shutdown();
        }
    }

    private static LogWriter getActiveWriter() {
        return config.isAsyncLogging() ? asyncWriter : config.getLogWriter();
    }


    // Solution for basic logger - using a concrete subclass
    private static class SimpleLogger extends BaseLogger<SimpleLogger> {
        SimpleLogger(String name, LogWriter logWriter, AppContext appContext) {
            super(name, logWriter, appContext);
        }
    }
    public static Logger<SimpleLogger> getLogger(Class<?> clazz) {
        if (config == null || appContext == null) {
            throw new IllegalStateException("LoggerFactory not initialized. Call initialize() first.");
        }
        checkInitialization();
        return new SimpleLogger(clazz.getName(), config.getLogWriter(), appContext)
                .withClass(clazz);
    }

    public static RestApiLogger getRestApiLogger(Class<?> clazz) {
        if (config == null || appContext == null) {
            throw new IllegalStateException("LoggerFactory not initialized. Call initialize() first.");
        }
        checkInitialization();
        return new RestApiLogger(clazz.getName(), config.getLogWriter(), appContext)
                .withClass(clazz);
    }

    public static DatabaseLogger getDatabaseLogger(Class<?> clazz) {
        if (config == null || appContext == null) {
            throw new IllegalStateException("LoggerFactory not initialized. Call initialize() first.");
        }
        checkInitialization();
        return new DatabaseLogger(clazz.getName(), config.getLogWriter(), appContext)
                .withClass(clazz);
    }

    public static KafkaLogger getKafkaLogger(Class<?> clazz) {
        if (config == null || appContext == null) {
            throw new IllegalStateException("LoggerFactory not initialized. Call initialize() first.");
        }
        checkInitialization();
        return new KafkaLogger(clazz.getName(), config.getLogWriter(), appContext)
                .withClass(clazz);
    }

    private static void checkInitialization() {
        if (config == null || appContext == null) {
            throw new IllegalStateException("LoggerFactory not initialized. Call initialize() first.");
        }
    }

}