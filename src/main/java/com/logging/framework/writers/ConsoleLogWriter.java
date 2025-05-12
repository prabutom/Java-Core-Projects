package com.logging.framework.writers;

import com.logging.framework.core.LogEntry;
import com.logging.framework.core.LogWriter;
import com.logging.framework.formatters.LogFormatter;

public class ConsoleLogWriter implements LogWriter {

    private final LogFormatter formatter;

    public ConsoleLogWriter(LogFormatter formatter) {
        this.formatter = formatter;
    }

    @Override
    public void write(LogEntry entry) {
        String formatted = formatter.format(entry);
        System.out.println(formatted);
    }
}
