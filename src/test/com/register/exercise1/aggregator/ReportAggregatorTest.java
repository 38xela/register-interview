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

}
