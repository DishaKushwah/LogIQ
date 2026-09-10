package com.logiq.analytics;

import com.logiq.model.LogEntry;
import com.logiq.model.LogLevel;
import com.logiq.model.ParseError;
import com.logiq.processing.ProcessingResult;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class AnalyticsEngineTest {

    @Test
    void shouldCalculateBasicStatistics() {

        LogEntry login = new LogEntry(
                LocalDateTime.of(2026, 8, 14, 9, 0, 5),
                LogLevel.INFO,
                "AuthService",
                "User login successful",
                Map.of("userId", "101")
        );

        LogEntry order = new LogEntry(
                LocalDateTime.of(2026, 8, 14, 9, 0, 47),
                LogLevel.INFO,
                "OrderService",
                "Order created",
                Map.of(
                        "userId", "102",
                        "orderId", "ord-3301"
                )
        );

        LogEntry error = new LogEntry(
                LocalDateTime.of(2026, 8, 14, 9, 11, 12),
                LogLevel.ERROR,
                "DatabaseService",
                "Database connection timeout",
                Map.of(
                        "requestId", "req-507",
                        "duration", "5000"
                )
        );

        ParseError parseError = new ParseError(
                11,
                "INVALID LOG ENTRY WITH NO STRUCTURE",
                "Invalid timestamp: INVALID LOG"
        );

        ProcessingResult result = new ProcessingResult(
                List.of(login, order, error),
                List.of(parseError)
        );

        AnalyticsEngine engine =
                new AnalyticsEngine();

        AnalyticsReport report =
                engine.analyze(result);

        assertEquals(3, report.totalEntries());
        assertEquals(1, report.totalErrors());

        assertEquals(
                2,
                report.entriesByLevel()
                        .get(LogLevel.INFO)
        );

        assertEquals(
                1,
                report.entriesByLevel()
                        .get(LogLevel.ERROR)
        );

        assertEquals(
                1,
                report.entriesByService()
                        .get("AuthService")
        );

        assertEquals(
                1,
                report.entriesByService()
                        .get("OrderService")
        );

        assertEquals(
                1,
                report.entriesByService()
                        .get("DatabaseService")
        );
        assertEquals(
                1,
                report.durationCount()
        );
        
        assertEquals(
                5000.0,
                report.averageDuration()
        );
        
        assertEquals(
                5000,
                report.minimumDuration()
        );
        
        assertEquals(
                5000,
                report.maximumDuration()
        );
        
        assertEquals(
                1,
                report.slowOperationCount()
        );
    }
}