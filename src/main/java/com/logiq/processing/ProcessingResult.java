package com.logiq.processing;

import com.logiq.model.LogEntry;
import com.logiq.model.ParseError;

import java.util.List;

public record ProcessingResult(
        List<LogEntry> entries,
        List<ParseError> errors
) {
}