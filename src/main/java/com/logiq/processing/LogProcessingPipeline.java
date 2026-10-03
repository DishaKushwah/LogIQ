//READING

package com.logiq.processing;

import com.logiq.io.LogFileReader;
import com.logiq.model.LogLine;
import java.util.concurrent.atomic.AtomicLong;

public class LogProcessingPipeline {
    private final LogFileReader fileReader;
    private final LogProcessingStrategy processor;

    public LogProcessingPipeline(LogFileReader fileReader, LogProcessingStrategy processor) {
        this.fileReader = fileReader;
        this.processor = processor;
    }

    public ProcessingResult process(String filePath) throws Exception{
        try (var lines = fileReader.readLines(filePath)) {
            AtomicLong lineNumber = new AtomicLong(0);
            return processor.process(
                    lines.map(line ->
                            new LogLine(lineNumber.incrementAndGet(),line)
                    )
            );
        }
    }
}