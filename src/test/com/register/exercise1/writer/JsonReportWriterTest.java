package com.register.exercise1.writer;

import com.register.exercise1.model.ReportEntry;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class JsonReportWriterTest {

    @TempDir
    Path tempDir;

    @Test
    void singleEntry_writesJsonArray() throws IOException {
        Path output = tempDir.resolve("report.json");
        List<ReportEntry> entries = List.of(
                new ReportEntry("1.2.3.4", 42L, 100.0, 8192L, 100.0)
        );

        new JsonReportWriter().write(entries, output);

        List<String> lines = Files.readAllLines(output);
        assertEquals("[", lines.get(0));
        assertEquals("  {\"ipAddress\":\"1.2.3.4\",\"requests\":42,\"requestsPct\":\"100.00\",\"bytes\":8192,\"bytesPct\":\"100.00\"}", lines.get(1));
        assertEquals("]", lines.get(2));
    }
    
}
