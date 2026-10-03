// THREADING

package com.logiq.processing;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import java.util.concurrent.*;

import com.logiq.model.LogEntry;
import com.logiq.model.LogLine;
import com.logiq.model.ParseError;
import com.logiq.processing.LogProcessor;

public class ConcurrentLogProcessor implements LogProcessingStrategy{
    private final ExecutorService executor;
    private final LogProcessor processor;

    private ProcessingResult processBatch(List<LogLine> batch) {
        return processor.process(batch.stream());
    }

    public ConcurrentLogProcessor(int threadCount, LogProcessor processor) {
        this.executor =Executors.newFixedThreadPool(threadCount);
        this.processor = processor;
    }

    public Future<String> submitTestTask() {
        Callable<String> task = () -> {
            System.out.println("Task running on: " + Thread.currentThread().getName());
            return "Task completed";
        };
        return executor.submit(task);
    }

    public Future<ProcessingResult> submitBatch(List<LogLine> batch) {
        Callable<ProcessingResult> task = () -> processBatch(batch);
        return executor.submit(task);
    }

    public ProcessingResult process(java.util.stream.Stream<LogLine> lines) throws Exception {
        LogBatcher batcher = new LogBatcher();
        var iterator = lines.iterator();
        List<Future<ProcessingResult>> futures = new ArrayList<>();
        final int batchSize = 10_000;
        while (iterator.hasNext()) {
            List<LogLine> batch =
                    batcher.nextBatch(
                            iterator,
                            batchSize
                    );
            if (batch.isEmpty()) {
                break;
            }
            futures.add(
                    submitBatch(batch)
            );
        }
        return mergeResults(futures);
    }

    private ProcessingResult mergeResults(
            List<Future<ProcessingResult>> futures
    ) throws Exception {

        List<LogEntry> allEntries = new ArrayList<>();
        List<ParseError> allErrors = new ArrayList<>();

        for (Future<ProcessingResult> future : futures) {
            ProcessingResult result = future.get();
            allEntries.addAll(result.entries());
            allErrors.addAll(result.errors());
        }

        return new ProcessingResult(
                List.copyOf(allEntries),
                List.copyOf(allErrors)
        );
    }
    public void shutdown() {
        executor.shutdown();
    }
}