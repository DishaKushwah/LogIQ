package com.logiq.model;

public record LogLine(
        long lineNumber,
        String content
) {
}