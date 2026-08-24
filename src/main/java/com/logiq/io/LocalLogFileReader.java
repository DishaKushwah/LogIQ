package com.logiq.io;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

public class LocalLogFileReader implements LogFileReader {

    @Override
    public Stream<String> readLines(String filePath) {
        try {
            return Files.lines(Path.of(filePath));
        } catch (IOException exception) {
            throw new LogFileReadException("Unable to read log file: " + filePath,exception);
        }
    }
}