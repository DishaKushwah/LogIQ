package com.logiq.parser;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import com.logiq.model.LogEntry;
import com.logiq.model.LogLevel;

public class StandardLogParser implements LogParser { // Implementation of the LogParser interface for standard log format
    private static final DateTimeFormatter TIMESTAMP_FORMATTER =
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    @Override       // override the parse method from LogParser interface to parse a log line into a LogEntry object
    public LogEntry parse(String line) {

        if (line == null || line.isBlank()) {
            throw new InvalidLogFormatException("Log line cannot be null or blank");
        }

        String[] tokens = line.trim().split("\\s+");
        if (tokens.length < 5) {
            throw new InvalidLogFormatException("Log line does not contain enough fields: " + line);
        }

        LocalDateTime timestamp =parseTimestamp(tokens[0], tokens[1]);
        LogLevel level = parseLevel(tokens[2]);
        // 3. processing service
        String service = tokens[3];
        if (service.isBlank()) {
            throw new InvalidLogFormatException("Service cannot be empty");
        }

        // 4. processing message 
        int metadataStart = findMetadataStart(tokens);
        String message = buildMessage(tokens,4,metadataStart);
        if (message.isBlank()) {
            throw new InvalidLogFormatException("Log message cannot be empty");
        }
        
        // 5. processing metadata
        Map<String, String> metadata = new HashMap<>();
        parseMetadata(tokens,metadataStart,metadata);
        return new LogEntry(timestamp, level, service, message, metadata);
    }

    //1. parsing timestamp
    private LocalDateTime parseTimestamp(String date, String time) {
    try {
        return LocalDateTime.parse(date + " " + time,TIMESTAMP_FORMATTER);
    } catch (Exception exception) {
        throw new InvalidLogFormatException("Invalid timestamp: " + date + " " + time,exception);
        }
    }
    
    //2. parsing LogLevel
    private LogLevel parseLevel(String value) {
    try {
        return LogLevel.valueOf(value.toUpperCase());
    } catch (IllegalArgumentException exception) {
        throw new InvalidLogFormatException( "Invalid log level: " + value, exception );
        }
    }


    // 5. parse metadata
    private void parseMetadata(String[] tokens,int start,Map<String, String> metadata) {
    for (int i = start; i < tokens.length; i++) {
        String token = tokens[i];
        int separatorIndex = token.indexOf('=');
        if (separatorIndex <= 0 || separatorIndex == token.length() - 1) {
            throw new InvalidLogFormatException("Invalid metadata token: " + token);
        }

        String key = token.substring(0, separatorIndex);
        String value = token.substring(separatorIndex + 1);

        if (metadata.put(key, value) != null) {
            throw new InvalidLogFormatException("Duplicate metadata key: " + key);
            }
        }
    }
    
    // building message
    private String buildMessage(String[] tokens,int start,int end) {
    StringBuilder message = new StringBuilder();
    for (int i = start; i < end; i++) {
        if (!message.isEmpty()) {
            message.append(' ');
        }
        message.append(tokens[i]);
    }
    return message.toString();
    }

    // checking if a token is a metadata token
    private boolean isMetadataToken(String token) {
        return token.matches("[A-Za-z][A-Za-z0-9_]*=.*");
    }
    
    private int findMetadataStart(String[] tokens) {
    for (int i = 4; i < tokens.length; i++) {
        if (isMetadataToken(tokens[i])) {
            return i;
            }
        }
    return tokens.length;
    }


}
