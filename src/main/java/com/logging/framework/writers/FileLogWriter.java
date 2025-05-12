package com.logging.framework.writers;

import com.logging.framework.core.LogEntry;
import com.logging.framework.core.LogWriter;
import com.logging.framework.formatters.LogFormatter;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class FileLogWriter implements LogWriter {

    private final String filePath;
    private final LogFormatter formatter;

    public FileLogWriter(String filePath, LogFormatter formatter) {
        this.filePath = filePath;
        this.formatter = formatter;
        initializeFile();
    }

    private void initializeFile() {
        try {
            Path path = Paths.get(filePath);
            if (!Files.exists(path)) {
                Files.createDirectories(path.getParent());
                Files.createFile(path);
            }
        } catch (IOException e) {
            System.err.println("Failed to initialize log file: " + e.getMessage());
        }
    }

    @Override
    public void write(LogEntry entry) {
        try (BufferedWriter writer = new BufferedWriter(
                new FileWriter(filePath, true))) {

            String formatted = formatter.format(entry);
            writer.write(formatted);
            writer.newLine();
        } catch (IOException e) {
            System.err.println("Failed to write to log file: " + e.getMessage());
        }
    }
}
