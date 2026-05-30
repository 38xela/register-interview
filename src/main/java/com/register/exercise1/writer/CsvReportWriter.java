package com.register.exercise1.writer;

import com.register.exercise1.model.ReportEntry;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * Writes the report as a CSV file with a header row.
 * Percentages are formatted to two decimal places.
 */
public class CsvReportWriter implements ReportWriter{

    @Override
    public void write(List<ReportEntry> entries, Path outputPath) throws IOException {
        try (BufferedWriter writer = Files.newBufferedWriter(outputPath, StandardCharsets.UTF_8)) {
            writer.write("ipAddress,requests,requestsPct,bytes,bytesPct");
            writer.newLine();
            for (ReportEntry entry : entries) {
                writer.write(String.format("%s,%d,%.2f,%d,%.2f",
                        entry.ipAddress(),
                        entry.requestCount(),
                        entry.requestPercentage(),
                        entry.totalBytes(),
                        entry.bytesPercentage()
                ));
                writer.newLine();
            }
        }
    }
}
