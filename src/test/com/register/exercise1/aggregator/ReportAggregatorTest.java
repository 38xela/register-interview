package com.register.exercise1.aggregator;

import com.register.exercise1.model.LogEntry;
import com.register.exercise1.model.ReportEntry;
import org.junit.jupiter.api.Test;


import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ReportAggregatorTest {

    @Test
    void singleIp_gets100PercentOfRequestsAndBytes() {
        List<LogEntry> entries = List.of(
                new LogEntry("2024-01-01", 500L, "200", "1.2.3.4"),
                new LogEntry("2024-01-01", 300L, "200", "1.2.3.4")
        );

        List<ReportEntry> result = new ReportAggregator().aggregate(entries);

        assertEquals(1, result.size());
        assertEquals("1.2.3.4", result.getFirst().ipAddress());
        assertEquals(2L, result.getFirst().requestCount());
        assertEquals(100.0, result.getFirst().requestPercentage());
        assertEquals(800L, result.getFirst().totalBytes());
        assertEquals(100.0, result.getFirst().bytesPercentage());
    }

    @Test
    void multipleIps_sortedDesc() {
        List<LogEntry> entries = List.of(
                new LogEntry("2024-01-01", 500L, "200", "1.2.3.4"),
                new LogEntry("2024-01-01", 300L, "200", "1.2.3.4"),
                new LogEntry("2024-01-01", 400L, "200", "1.2.3.5"),
                new LogEntry("2024-01-01", 800L, "200", "1.2.3.5"),
                new LogEntry("2024-01-01", 500L, "200", "1.2.3.5"),
                new LogEntry("2024-01-01", 100L, "200", "1.2.3.1")
        );

        List<ReportEntry> result = new ReportAggregator().aggregate(entries);

        assertEquals(3, result.size());

        assertEquals("1.2.3.5", result.getFirst().ipAddress());
        assertEquals(3L, result.getFirst().requestCount());
        assertEquals(50.0, result.getFirst().requestPercentage());
        assertEquals(1700L, result.getFirst().totalBytes());
        assertEquals(65.38461538461539, result.getFirst().bytesPercentage());

        assertEquals("1.2.3.4", result.get(1).ipAddress());
        assertEquals(2L, result.get(1).requestCount());
        assertEquals(33.333333333333336, result.get(1).requestPercentage());
        assertEquals(800L, result.get(1).totalBytes());
        assertEquals(30.76923076923077, result.get(1).bytesPercentage());

        assertEquals("1.2.3.1", result.get(2).ipAddress());
        assertEquals(1L, result.get(2).requestCount());
        assertEquals(16.666666666666668, result.get(2).requestPercentage());
        assertEquals(100L, result.get(2).totalBytes());
        assertEquals(3.8461538461538463, result.get(2).bytesPercentage());

    }

    @Test
    void emptyInput() {
        List<ReportEntry> result = new ReportAggregator().aggregate(List.of());

        assertEquals(0, result.size());
    }

}
