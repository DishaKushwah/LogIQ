package com.logiq.processing;

import com.logiq.io.LogFileReader;

public class LogProcessingPipeline {

    private final LogFileReader fileReader;
    private final LogProcessor processor;

    public LogProcessingPipeline(
            LogFileReader fileReader,
            LogProcessor processor
    ) {
        this.fileReader = fileReader;
        this.processor = processor;
    }

    public ProcessingResult process(String filePath) {

        try (var lines = fileReader.readLines(filePath)) {

            return processor.process(lines);
        }
    }
}