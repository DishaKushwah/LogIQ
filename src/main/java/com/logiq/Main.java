package com.logiq;

import com.logiq.analytics.AnalyticsEngine;
import com.logiq.analytics.AnalyticsReport;
import com.logiq.io.LocalLogFileReader;
import com.logiq.model.LogLevel;
import com.logiq.parser.StandardLogParser;
import com.logiq.processing.ConcurrentLogProcessor;
import com.logiq.processing.LogProcessingPipeline;
import com.logiq.processing.LogProcessor;
import com.logiq.processing.ProcessingResult;

import java.util.Map;

public class Main {

    public static void main(String[] args) throws Exception {

        String filePath = "logs/benchmark.log";

        LocalLogFileReader fileReader =
                new LocalLogFileReader();

        StandardLogParser parser =
                new StandardLogParser();

        LogProcessor logProcessor =
                new LogProcessor(parser);

        ConcurrentLogProcessor concurrentProcessor =
                new ConcurrentLogProcessor(
                        4,
                        logProcessor
                );

        LogProcessingPipeline pipeline =
                new LogProcessingPipeline(
                        fileReader,
                        concurrentProcessor
                );

        try {

            long startTime =
                    System.nanoTime();

            ProcessingResult result =
                    pipeline.process(filePath);

            long endTime =
                    System.nanoTime();

            long durationMs =
                    (endTime - startTime)
                            / 1_000_000;

            AnalyticsEngine analyticsEngine =
                    new AnalyticsEngine();

            AnalyticsReport report =
                    analyticsEngine.analyze(result);

            System.out.println(
                    "Total valid entries: "
                            + report.totalEntries()
            );

            System.out.println(
                    "Total invalid entries: "
                            + report.totalErrors()
            );

            System.out.println(
                    "Processing time: "
                            + durationMs
                            + " ms"
            );

            System.out.println(
                    "Average duration: "
                            + report.averageDuration()
                            + " ms"
            );

            System.out.println(
                    "Minimum duration: "
                            + report.minimumDuration()
                            + " ms"
            );

            System.out.println(
                    "Maximum duration: "
                            + report.maximumDuration()
                            + " ms"
            );

            System.out.println(
                    "Slow operations: "
                            + report.slowOperationCount()
            );

            System.out.println(
                    "Error rate: "
                            + report.errorRate()
                            + "%"
            );

            System.out.println(
                    "Most active service: "
                            + report.mostActiveService()
            );

            System.out.println(
                    "\nEntries by level:"
            );

            for (Map.Entry<LogLevel, Long> entry :
                    report.entriesByLevel().entrySet()) {

                System.out.println(
                        entry.getKey()
                                + " = "
                                + entry.getValue()
                );
            }

            System.out.println(
                    "\nEntries by service:"
            );

            for (Map.Entry<String, Long> entry :
                    report.entriesByService().entrySet()) {

                System.out.println(
                        entry.getKey()
                                + " = "
                                + entry.getValue()
                );
            }

        } finally {

            concurrentProcessor.shutdown();
        }
    }
}