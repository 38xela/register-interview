package com.register.exercise1.model;

/**
 * Represents a single parsed line from the access log.
 * Only lines with STATUS "200" are emitted by {@link com.register.exercise1.parser.LogParser}.
 */
public record LogEntry(
        String timestamp,
        long bytes,
        String status,
        String remoteAddr
) {}
