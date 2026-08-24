package com.logiq;

import com.logiq.io.LocalLogFileReader;
import com.logiq.io.LogFileReader;
import com.logiq.processing.LogProcessingPipeline;
import com.logiq.processing.LogProcessor;
import com.logiq.processing.ProcessingResult;
import com.logiq.parser.LogParser;
import com.logiq.parser.StandardLogParser;

public class Main {

    public static void main(String[] args) {

        String filePath = "logs/sample.log";

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
                pipeline.process(filePath);

        System.out.println("LogIQ processing complete.");
        System.out.println(
                "Valid entries: " +
                        result.entries().size()
        );

        System.out.println(
                "Invalid entries: " +
                        result.errors().size()
        );
    }
}