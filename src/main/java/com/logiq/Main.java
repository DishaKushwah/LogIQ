package com.logiq;

import com.logiq.analytics.AnalyticsEngine;
import com.logiq.analytics.AnalyticsReport;
import com.logiq.io.LocalLogFileReader;
import com.logiq.io.LogFileReader;
import com.logiq.parser.LogParser;
import com.logiq.parser.StandardLogParser;
import com.logiq.processing.LogProcessingPipeline;
import com.logiq.processing.LogProcessor;
import com.logiq.processing.ProcessingResult;

public class Main {

    public static void main(String[] args) {

        String filePath = "logs/sample.log";

        LogFileReader fileReader =new LocalLogFileReader();
        LogParser parser =new StandardLogParser();

        LogProcessor processor =new LogProcessor(parser);
        LogProcessingPipeline pipeline =new LogProcessingPipeline(fileReader,processor);
        ProcessingResult result =pipeline.process(filePath);
        AnalyticsEngine analyticsEngine =new AnalyticsEngine();
        AnalyticsReport report =analyticsEngine.analyze(result);
        System.out.println();
        System.out.println("========== LogIQ Analytics ==========");
        System.out.println();
        System.out.println("Total valid entries: " + report.totalEntries());
        System.out.println("Total invalid entries: " +report.totalErrors());
        System.out.println();
        System.out.println("Error rate: " +String.format("%.2f", report.errorRate()) +"%");
        System.out.println("Most active service: " +report.mostActiveService());
        System.out.println();
        System.out.println("Duration statistics:");
        System.out.println("  Entries with duration: " +report.durationCount());
        System.out.println("  Average duration: " +report.averageDuration() +" ms");
        System.out.println("  Minimum duration: " +report.minimumDuration() +" ms");
        System.out.println("  Maximum duration: " +report.maximumDuration() +" ms");
        System.out.println("  Slow operations (>= 1000 ms): " +report.slowOperationCount());
        System.out.println();
        System.out.println("Entries by log level:");
        report.entriesByLevel()
                .forEach((level, count) ->
                        System.out.println(
                                "  " + level + ": " + count
                        )
                );

        System.out.println();

        System.out.println("Entries by service:");

        report.entriesByService()
                .forEach((service, count) ->
                        System.out.println(
                                "  " + service + ": " + count
                        )
                );
        System.out.println();
        System.out.println("=====================================");
    }
}