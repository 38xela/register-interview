package com.register.exercise1.parser;

import com.register.exercise1.model.LogEntry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    private static final Logger log = LoggerFactory.getLogger(LogParser.class);

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

            //Skip blank line
            if (line.isBlank()) continue;

            String[] fields = line.split(";", -1);

            //Skip fields number != 4
            if (fields.length != 4) {
                log.warn("Line {} skipped: expected 4 fields but found {}", i +1, fields.length);
                //System.err.printf("Line %d skipped: expected 4 fields but found %d", i +1, fields.length);
                continue;
            }

            //Skip not 200 status
            String status = fields[2].trim();
            if (!status.equals("200")) continue;

            //Parse bytes, skip in case of parsing error
            long bytes;
            try {
                bytes = Long.parseLong(fields[1].trim());
            } catch (NumberFormatException e) {
                log.warn("Line {} skipped: invalid bytes value '{}'", i + 1, fields[1].trim());
                //System.err.printf("Line %d skipped: invalid bytes value '%s'%n", i + 1, fields[1].trim());
                continue;
            }

            entries.add(new LogEntry(fields[0].trim(), bytes, fields[2].trim(), fields[3].trim()));
        }

        return entries;
    }
}
