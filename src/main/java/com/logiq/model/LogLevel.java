package com.logiq.model;
// enum because it is a fixed set of constants and we want to avoid unnecessary string comparisons when checking log levels
public enum LogLevel {
    DEBUG,
    INFO,
    WARN,
    ERROR
}