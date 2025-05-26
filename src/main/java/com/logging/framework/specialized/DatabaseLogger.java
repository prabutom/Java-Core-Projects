package com.logging.framework.specialized;

import com.logging.framework.core.BaseLogger;
import com.logging.framework.core.LogWriter;
import com.logging.framework.context.AppContext;

public class DatabaseLogger extends BaseLogger<DatabaseLogger> {

    public DatabaseLogger(String name, LogWriter logWriter, AppContext appContext) {
        super(name, logWriter, appContext);
    }

    public void logQuery(String query, Object[] params) {
        this.with("query", query)
                .with("parameters", params)
                .info("Database query executed");
    }

    public void logQueryExecutionTime(String query, long executionTimeMs) {
        this.with("query", query)
                .with("executionTimeMs", executionTimeMs)
                .info("Query execution time");
    }

    public void logTransactionStart() {
        this.info("Transaction started");
    }

    public void logTransactionCommit() {
        this.info("Transaction committed");
    }

    public void logTransactionRollback(Throwable cause) {
        this.error("Transaction rolled back", cause);
    }
}
