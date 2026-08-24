package com.logiq.io;

public class LogFileReadException extends RuntimeException {

    public LogFileReadException(
            String message,
            Throwable cause
    ) {
        super(message, cause);
    }
}