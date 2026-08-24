package com.logiq.io;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class LocalLogFileReaderTest {

    private final LogFileReader reader =
            new LocalLogFileReader();

    @Test
    void shouldReadLogFile() {

        try (Stream<String> lines =
                     reader.readLines("logs/sample.log")) {

            List<String> logLines = lines.toList();

            assertFalse(logLines.isEmpty());

            assertTrue(
                    logLines.get(0).contains("AuthService")
            );
        }
    }
}