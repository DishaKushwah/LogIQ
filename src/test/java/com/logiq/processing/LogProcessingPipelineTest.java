package com.logiq.processing;

import com.logiq.io.LocalLogFileReader;
import com.logiq.io.LogFileReader;
import com.logiq.parser.LogParser;
import com.logiq.parser.StandardLogParser;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LogProcessingPipelineTest {

    @Test
    void shouldProcessActualLogFile() throws Exception {

        LogFileReader fileReader =
                new LocalLogFileReader();

        LogParser parser =
                new StandardLogParser();

        LogProcessor processor =
                new LogProcessor(parser);

        LogProcessingPipeline pipeline =
                new LogProcessingPipeline(
                        fileReader,
                        processor
                );

        ProcessingResult result =
                pipeline.process("logs/sample.log");

        assertFalse(
                result.entries().isEmpty()
        );

        assertFalse(
                result.errors().isEmpty()
        );

        assertEquals(
                "AuthService",
                result.entries()
                        .get(0)
                        .service()
        );

        assertEquals(
                "101",
                result.entries()
                        .get(0)
                        .metadata()
                        .get("userId")
        );
    }
}