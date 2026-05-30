package com.register.exercise1.writer;

import com.register.exercise1.model.ReportEntry;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class JsonReportWriter implements ReportWriter{

    @Override
    public void write(List<ReportEntry> entries, Path outputPath) throws IOException {
        try (BufferedWriter writer = Files.newBufferedWriter(outputPath, StandardCharsets.UTF_8)) {
            writer.write("[");
            writer.newLine();
            for (int i = 0; i < entries.size(); i++) {
                ReportEntry entry = entries.get(i);
                String row = String.format(
                        "  {\"ipAddress\":\"%s\",\"requests\":%d,\"requestsPct\":\"%.2f\",\"bytes\":%d,\"bytesPct\":\"%.2f\"}",
                        entry.ipAddress(),
                        entry.requestCount(),
                        entry.requestPercentage(),
                        entry.totalBytes(),
                        entry.bytesPercentage()
                );
                writer.write(row);
                if (i < entries.size() - 1) writer.write(",");
                writer.newLine();
            }
            writer.write("]");
            writer.newLine();
        }
    }
}
