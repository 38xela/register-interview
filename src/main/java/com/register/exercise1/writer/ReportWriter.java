package com.register.exercise1.writer;

import com.register.exercise1.model.ReportEntry;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

/**
 * Writes a list of report entries to a file.
 * Implementations define the output format (CSV, JSON, etc.).
 */
public interface ReportWriter {

    /**
     * Writes the report to the given path.
     *
     * @param entries    aggregated report data, sorted by caller
     * @param outputPath destination file path (parent directory must exist)
     * @throws IOException if the file cannot be written
     */
    void write(List<ReportEntry> entries, Path outputPath) throws IOException;
}
