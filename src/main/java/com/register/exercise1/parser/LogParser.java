package com.register.exercise1.parser;

import com.register.exercise1.model.LogEntry;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Reads and parses the access log file.
 * Filters out non-200 status lines. Skips malformed lines with a warning to stderr.
 */
public class LogParser {

    /**
     * Parses the given log file and returns valid entries with STATUS "200".
     *
     * @param inputPath path to the semicolon-separated log file
     * @return list of parsed entries, never null
     * @throws IOException if the file cannot be read
     */
    public List<LogEntry> parse(Path inputPath) throws IOException {
        List<LogEntry> entries = new ArrayList<>();

        List<String> readAllLines = Files.readAllLines(inputPath);

        for (int i = 0; i < readAllLines.size(); i++) {
            String line = readAllLines.get(i);
            if (line.isBlank()) continue;

            String[] fields = line.split(";", -1);

            if (fields.length != 4) {
                System.err.printf("Line %d skipped: expected 4 fields but found %d", i +1, fields.length);
                continue;
            }

            String status = fields[2].trim();
            if (!status.equals("200")) continue;

            long bytes;
            try {
                bytes = Long.parseLong(fields[1].trim());
            } catch (NumberFormatException e) {
                System.err.printf("Line %d skipped: invalid bytes value '%s'%n", i + 1, fields[1].trim());
                continue;
            }

            entries.add(new LogEntry(fields[0].trim(), bytes, fields[2].trim(), fields[3].trim()));
        }

        return entries;
    }
}
