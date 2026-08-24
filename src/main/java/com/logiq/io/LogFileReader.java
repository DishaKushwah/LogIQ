package com.logiq.io;

import java.util.stream.Stream;
// Interface for reading log files, providing a method to read lines from a specified file path
public interface LogFileReader {
    Stream<String> readLines(String filePath);
}