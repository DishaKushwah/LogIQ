package com.logiq.analytics;
import com.logiq.model.LogEntry;
import com.logiq.model.LogLevel;
import com.logiq.processing.ProcessingResult;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

public class AnalyticsEngine {

    private static final long SLOW_OPERATION_THRESHOLD_MS = 1000;

    public AnalyticsReport analyze(ProcessingResult result) {
        Map<LogLevel, Long> entriesByLevel =new EnumMap<>(LogLevel.class);
        Map<String, Long> entriesByService = new HashMap<>();

        long durationCount = 0;
        long totalDuration = 0;
        long minimumDuration = Long.MAX_VALUE;
        long maximumDuration = Long.MIN_VALUE;
        long slowOperationCount = 0;

        for (LogEntry entry : result.entries()) {

            entriesByLevel.merge(entry.level(),1L,Long::sum);
            entriesByService.merge(entry.service(),1L,Long::sum);
            String durationValue =entry.metadata().get("duration");
            if (durationValue != null) {
                long duration =Long.parseLong(durationValue);
                durationCount++;
                totalDuration += duration;
                minimumDuration =Math.min(minimumDuration,duration);

                maximumDuration =Math.max(maximumDuration,duration);
                if (duration >=SLOW_OPERATION_THRESHOLD_MS) {
                    slowOperationCount++;
                }
            }
        }
        long errorCount =entriesByLevel.getOrDefault(LogLevel.ERROR,0L);
        double errorRate =result.entries().isEmpty()? 0.0: (double) errorCount /  result.entries().size() * 100;
        String mostActiveService =
        entriesByService.entrySet()
                .stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("N/A");
        double averageDuration =(durationCount == 0)? 0.0: (double) totalDuration /  durationCount;
        if (durationCount == 0) {
            minimumDuration = 0;
            maximumDuration = 0;
        }

        return new AnalyticsReport(
                result.entries().size(),
                result.errors().size(),
                Map.copyOf(entriesByLevel),
                Map.copyOf(entriesByService),
                durationCount,
                averageDuration,
                minimumDuration,
                maximumDuration,
                slowOperationCount,
                mostActiveService,
                errorRate
        );
    }
}