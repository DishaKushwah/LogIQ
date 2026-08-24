package com.logiq.model;

public record ParseError(
        long lineNumber,
        String rawLine,
        String reason
) {
}