package com.register.exercise1.model;

/**
 * Represents an aggregated traffic row for a single IP address.
 * Produced by {@link com.register.exercise1.aggregator.ReportAggregator}
 * and consumed by {@link com.register.exercise1.writer.ReportWriter} implementations.
 */
public record ReportEntry(
        String ipAddress,
        long requestCount,
        double requestPercentage,
        long totalBytes,
        double bytesPercentage
) {}
