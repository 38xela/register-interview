package com.register.exercise1.model;

public record ReportEntry(
        String ipAddress,
        long requestCount,
        double requestPercentage,
        long totalBytes,
        double bytesPercentage
) {}
