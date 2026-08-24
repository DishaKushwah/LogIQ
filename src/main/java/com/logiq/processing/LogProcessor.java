package com.logiq.processing;

import com.logiq.model.LogEntry;
import com.logiq.model.ParseError;
import com.logiq.parser.InvalidLogFormatException;
import com.logiq.parser.LogParser;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class LogProcessor {

    private final LogParser parser;

    public LogProcessor(LogParser parser) {
        this.parser = parser;
    }

    public ProcessingResult process(Stream<String> lines) {

    List<LogEntry> entries = new ArrayList<>();
    List<ParseError> errors = new ArrayList<>();

    long lineNumber = 0;

    for (String line : (Iterable<String>) lines::iterator) {

        lineNumber++;

        try {

            LogEntry entry = parser.parse(line);

            entries.add(entry);

        } catch (InvalidLogFormatException exception) {

            errors.add(
                    new ParseError(
                            lineNumber,
                            line,
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