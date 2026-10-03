package com.logiq.processing;
import com.logiq.model.LogLine;
import org.junit.jupiter.api.*;

import java.util.List;
import java.util.concurrent.Future;
import static org.junit.jupiter.api.Assertions.*;
import com.logiq.parser.LogParser;
import com.logiq.parser.StandardLogParser;

class ConcurrentLogProcessorTest {
    @Test
    void shouldExecuteTaskAndReturnResult() throws Exception {
        LogParser parser = new StandardLogParser();
        LogProcessor logProcessor = new LogProcessor(parser);
        ConcurrentLogProcessor processor = new ConcurrentLogProcessor(2, logProcessor);
        try {
            List<LogLine> batch1 = List.of(
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
            );

            List<LogLine> batch2 = List.of(
                    new LogLine(
                            3,
                            "2026-08-14 09:01:12 ERROR DatabaseService " +
                                    "Database connection timeout duration=5000"
                    ),
                    new LogLine(
                            4,
                            "INVALID LOG ENTRY"
                    )
            );

            Future<ProcessingResult> future1 =
                    processor.submitBatch(batch1);

            Future<ProcessingResult> future2 =
                    processor.submitBatch(batch2);

            ProcessingResult result1 =
                    future1.get();

            ProcessingResult result2 =
                    future2.get();

            assertEquals(
                    2,
                    result1.entries().size()
            );

            assertTrue(
                    result1.errors().isEmpty()
            );

            assertEquals(
                    1,
                    result2.entries().size()
            );

            assertEquals(
                    1,
                    result2.errors().size()
            );

            assertEquals(
                    4,
                    result2.errors()
                            .get(0)
                            .lineNumber()
            );
        } finally {
            processor.shutdown();
        }
    }
    @Test
    void shouldProduceSameResultAsSequentialProcessor() throws Exception {

        LogParser parser =
                new StandardLogParser();

        LogProcessor sequentialProcessor =
                new LogProcessor(parser);

        ConcurrentLogProcessor concurrentProcessor =
                new ConcurrentLogProcessor(
                        2,
                        new LogProcessor(
                                new StandardLogParser()
                        )
                );

        try {
            List<LogLine> lines = List.of(
                    new LogLine(
                            1,
                            "2026-08-14 09:00:05 INFO AuthService " +
                                    "User login successful userId=101"
                    ),
                    new LogLine(
                            2,
                            "2026-08-14 09:00:47 INFO OrderService " +
                                    "Order created userId=102 orderId=ord-3301"
                    ),
                    new LogLine(
                            3,
                            "INVALID LOG ENTRY"
                    ),
                    new LogLine(
                            4,
                            "2026-08-14 09:01:12 ERROR DatabaseService " +
                                    "Database connection timeout duration=5000"
                    ),
                    new LogLine(
                            5,
                            "2026-08-14 09:02:00 WARN AuthService " +
                                    "Suspicious login userId=103"
                    )
            );
            ProcessingResult sequentialResult = sequentialProcessor.process(lines.stream()
                    );

            ProcessingResult concurrentResult = concurrentProcessor.process(lines.stream());
            assertEquals(sequentialResult.entries().size(), concurrentResult.entries().size());
            assertEquals(sequentialResult.errors().size(), concurrentResult.errors().size());
            assertEquals(sequentialResult.errors().get(0).lineNumber(), concurrentResult.errors().get(0).lineNumber());

        } finally {
            concurrentProcessor.shutdown();
        }
    }
}