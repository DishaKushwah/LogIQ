package com.logiq.model;

import java.time.LocalDateTime;
import java.util.Map;
// NOTE: using record instead of class because it is immutable and has a better performance than class, also one log entry is not going to be modified after creation so it is a good fit for record

public record LogEntry(
        LocalDateTime timestamp, // using java api so we dont need continuous parsing of date strings
        LogLevel level, // using enum to avoid unnecessary string comparisons   
        String service,
        String message,
        Map<String, String> metadata
) {
}