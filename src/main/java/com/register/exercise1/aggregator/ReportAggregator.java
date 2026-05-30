package com.register.exercise1.aggregator;

import com.register.exercise1.model.LogEntry;
import com.register.exercise1.model.ReportEntry;

import java.util.*;

/**
 * Aggregates log entries by IP address and computes request/byte statistics.
 */
public class ReportAggregator {

    /**
     * Groups entries by IP, computes counts, totals, and percentages.
     * Results are sorted by request count descending, then by IP alphabetically.
     *
     * @param logEntries list of filtered log entries
     * @return aggregated report rows, empty list if input is empty
     */
    public List<ReportEntry> aggregate(List<LogEntry> logEntries) {
        if(logEntries.isEmpty()) return Collections.emptyList();

        int totalEntries = logEntries.size();
        long totalBytes = logEntries.stream()
                .mapToLong(LogEntry::bytes)
                .sum();

        Map<String, long[]> aggregatePerIpValues = new HashMap<>();

        logEntries.forEach(logEntry -> {
            aggregatePerIpValues.compute(logEntry.remoteAddr(), (k, v) ->
                    v == null
                            ? new long[]{1L, logEntry.bytes()}
                            : new long[]{v[0] + 1L, v[1] + logEntry.bytes()}
            );
        });


        return aggregatePerIpValues.entrySet().stream().map(entry -> {
                    long requestCount = entry.getValue()[0];
                    long bytesCount = entry.getValue()[1];
                    double requestPerc = (requestCount * 100.0) / totalEntries;
                    double bytesPerc = (bytesCount * 100.0) / totalBytes;
                    return new ReportEntry(entry.getKey(), entry.getValue()[0], requestPerc, bytesCount, bytesPerc);
                }).sorted(Comparator.comparingLong(ReportEntry::requestCount).reversed()
                        .thenComparing(ReportEntry::ipAddress))
                .toList();

    }
}
