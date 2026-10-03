package com.logiq.processing;

import com.logiq.parser.LogParser;
import com.logiq.parser.StandardLogParser;
import org.junit.jupiter.api.Test;
import com.logiq.model.LogLine;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class LogProcessorTest {

    @Test
    void shouldProcessValidLogLines() {

        LogParser parser = new StandardLogParser();
        LogProcessor processor = new LogProcessor(parser);

        try (Stream<LogLine> lines = Stream.of(
                new LogLine(
                        1,
                        "2026-08-14 09:00:05 INFO AuthService " +
                                "User login successful userId=101"
                ),

                new LogLine(
                        2,
                        "2026-08-14 09:00:47 INFO OrderService " +
                                "Order created userId=102 orderId=ord-3301"
                )
        )) {
            ProcessingResult result = processor.process(lines);

            assertEquals(2, result.entries().size());
            assertTrue(result.errors().isEmpty());

            assertEquals(
                    "AuthService",
                    result.entries().get(0).service()
            );

            assertEquals(
                    "OrderService",
                    result.entries().get(1).service()
            );

            assertEquals(
                    "101",
                    result.entries()
                            .get(0)
                            .metadata()
                            .get("userId")
            );

            assertEquals(
                    "ord-3301",
                    result.entries()
                            .get(1)
                            .metadata()
                            .get("orderId")
            );
        }
    }

    @Test
    void shouldContinueProcessingAfterInvalidLine() {

        LogParser parser = new StandardLogParser();
        LogProcessor processor = new LogProcessor(parser);

        try (Stream<LogLine> lines = Stream.of(
                new LogLine(
                        1,
                        "2026-08-14 09:00:05 INFO AuthService " +
                                "User login successful userId=101"
                ),
        
                new LogLine(
                        2,
                        "INVALID LOG ENTRY WITH NO STRUCTURE"
                ),
        
                new LogLine(
                        3,
                        "2026-08-14 09:00:47 INFO OrderService " +
                                "Order created userId=102 orderId=ord-3301"
                )
        )) {
            ProcessingResult result = processor.process(lines);

            assertEquals(2, result.entries().size());
            assertEquals(1, result.errors().size());

            assertEquals(
                    2,
                    result.errors().get(0).lineNumber()
            );

            assertEquals(
                    "INVALID LOG ENTRY WITH NO STRUCTURE",
                    result.errors().get(0).rawLine()
            );
        }
    }
}