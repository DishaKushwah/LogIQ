package com.logiq.analytics;

import com.logiq.model.LogLevel;

import java.util.Map;

public record AnalyticsReport(
        long totalEntries,
        long totalErrors,
        Map<LogLevel, Long> entriesByLevel,
        Map<String, Long> entriesByService,
        long durationCount,
        double averageDuration,
        long minimumDuration,
        long maximumDuration,
        long slowOperationCount,
        String mostActiveService,
        double errorRate
) {
}