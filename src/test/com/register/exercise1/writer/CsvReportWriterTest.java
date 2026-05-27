package com.register.exercise1.writer;

import com.register.exercise1.model.ReportEntry;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CsvReportWriterTest {

    @TempDir
    Path tempDir;

    @Test
    void singleEntry_writesHeaderAndOneRow() throws IOException {
        Path output = tempDir.resolve("report.csv");
        List<ReportEntry> entries = List.of(
                new ReportEntry("1.2.3.4", 42L, 100.0, 8192L, 100.0)
        );

        new CsvReportWriter().write(entries, output);

        List<String> lines = Files.readAllLines(output);
        assertEquals(2, lines.size());
        assertEquals("ipAddress,requests,requestsPct,bytes,bytesPct", lines.get(0));
        assertEquals("1.2.3.4,42,100.00,8192,100.00", lines.get(1));
    }
}
