package com.register.exercise1.model;

public record LogEntry(
        String timestamp,
        long bytes,
        String status,
        String remoteAddr
) {}
