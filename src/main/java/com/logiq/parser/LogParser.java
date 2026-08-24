package com.logiq.parser;
import com.logiq.model.LogEntry;

public interface LogParser {
    LogEntry parse(String logLine);// parse a log line and return a LogEntry object
    
}

