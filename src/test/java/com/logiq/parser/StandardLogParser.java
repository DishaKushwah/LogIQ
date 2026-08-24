package com.logiq.parser;

import com.logiq.model.LogEntry;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StandardLogParserTest {

    private final LogParser parser = new StandardLogParser();

    @Test
    void shouldParseTimestamp() {

        String line =
                "2026-08-14 09:04:20 INFO PaymentService " +
                "Payment completed";

        LogEntry entry = parser.parse(line);

        assertEquals(
                "2026-08-14T09:04:20",
                entry.timestamp().toString()
        );
    }
    @Test
    void shouldRejectInvalidLogLevel() {

    String line =
            "2026-08-14 09:22:40 SOMETHING " +
            "AuthService Test message userId=101";

    assertThrows(
            InvalidLogFormatException.class,
            () -> parser.parse(line)
        );
    }
}