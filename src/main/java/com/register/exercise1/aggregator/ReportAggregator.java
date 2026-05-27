package com.register.exercise1.aggregator;


import com.register.exercise1.model.LogEntry;
import com.register.exercise1.model.ReportEntry;

import java.util.*;

public class ReportAggregator {

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
                    return new ReportEntry(entry.getKey(), entry.getValue()[0], requestCount / totalEntries * 100.0, bytesCount, bytesCount / totalBytes * 100.0);
                }).sorted(Comparator.comparingLong(ReportEntry::requestCount).reversed())
                .toList();

    }

    private long[] sumValues(long[] current, LogEntry logEntry){
        return new long[]{
                current[0]++,
                current[1] + logEntry.bytes()
        };
    }
}
