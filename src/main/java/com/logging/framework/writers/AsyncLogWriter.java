package com.logging.framework.writers;

import com.logging.framework.core.LogEntry;
import com.logging.framework.core.LogWriter;

import java.util.concurrent.*;

public class AsyncLogWriter implements LogWriter {
    private final LogWriter delegate;
    private final BlockingQueue<LogEntry> queue;
    private final ExecutorService executor;

    public AsyncLogWriter(LogWriter delegate, int queueCapacity) {
        this.delegate = delegate;
        this.queue = new LinkedBlockingQueue<>(queueCapacity);
        this.executor = Executors.newSingleThreadExecutor(r -> {
            Thread t = new Thread(r, "AsyncLogWriter-Thread");
            t.setDaemon(true);
            return t;
        });

        startConsumer();
    }

    private void startConsumer() {
        executor.submit(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    LogEntry entry = queue.take();
                    delegate.write(entry);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                } catch (Exception e) {
                    System.err.println("Async log write failed: " + e.getMessage());
                }
            }
            // Flush remaining logs
            queue.forEach(delegate::write);
        });
    }

    @Override
    public void write(LogEntry entry) {
        if (!queue.offer(entry)) {
            delegate.write(entry); // Fallback to sync
        }
    }

    public void shutdown() {
        executor.shutdown();
        try {
            if (!executor.awaitTermination(5, TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}
