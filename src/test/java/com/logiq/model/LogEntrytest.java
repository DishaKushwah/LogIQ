package com.logiq.model;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

class LogEntryTest {
    @Test
    void shouldCreateLogEntry() {
        LocalDateTime timestamp =
                LocalDateTime.of(2026, 8, 14, 9, 4, 20);

        Map<String, String> metadata = Map.of(
                "userId", "102",
                "requestId", "req-504"
        );
        LogEntry entry = new LogEntry(
                timestamp,
                LogLevel.INFO,
                "PaymentService",
                "Payment completed",
                metadata
        );

        assertEquals(timestamp, entry.timestamp());
        assertEquals(LogLevel.INFO, entry.level());
        assertEquals("PaymentService", entry.service());
        assertEquals("Payment completed", entry.message());
        assertEquals("102", entry.metadata().get("userId"));
    }
    @Test
    void metadataShouldBeImmutable() {
    
        Map<String, String> metadata = new java.util.HashMap<>();
        metadata.put("userId", "101");
    
        LogEntry entry = new LogEntry(
                LocalDateTime.of(2026, 8, 14, 9, 0, 0),
                LogLevel.INFO,
                "AuthService",
                "User login successful",
                metadata
        );
    
        metadata.put("userId", "999");
    
        assertEquals("101", entry.metadata().get("userId"));
    
        assertThrows(
                UnsupportedOperationException.class,
                () -> entry.metadata().put("userId", "500")
        );
    }
}