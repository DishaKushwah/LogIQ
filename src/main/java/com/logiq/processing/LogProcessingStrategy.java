package com.logiq.processing;

import com.logiq.model.LogLine;

import java.util.stream.Stream;

public interface LogProcessingStrategy {

    ProcessingResult process(
            Stream<LogLine> lines
    ) throws Exception;
}