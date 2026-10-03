//PARSING

package com.logiq.processing;

import com.logiq.model.LogEntry;
import com.logiq.model.LogLine;
import com.logiq.model.ParseError;
import com.logiq.parser.InvalidLogFormatException;
import com.logiq.parser.LogParser;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class LogProcessor implements LogProcessingStrategy {
    private final LogParser parser;

    public LogProcessor(LogParser parser) {
        this.parser = parser;
    }

    public ProcessingResult process(Stream<LogLine> lines) {
        List<LogEntry> entries = new ArrayList<>();
        List<ParseError> errors = new ArrayList<>();
        for (LogLine logLine : (Iterable<LogLine>) lines::iterator) {
            try {
                LogEntry entry = parser.parse(logLine.content());
                entries.add(entry);
            } catch (InvalidLogFormatException exception) {
                errors.add(
                        new ParseError(
                                logLine.lineNumber(),
                                logLine.content(),
                                exception.getMessage()
                        )
                );
            }
        }

        return new ProcessingResult(
                List.copyOf(entries),
                List.copyOf(errors)
        );
    }
}