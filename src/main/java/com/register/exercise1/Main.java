package com.register.exercise1;

import com.register.exercise1.aggregator.ReportAggregator;
import com.register.exercise1.model.LogEntry;
import com.register.exercise1.model.ReportEntry;
import com.register.exercise1.parser.LogParser;
import com.register.exercise1.writer.CsvReportWriter;
import com.register.exercise1.writer.JsonReportWriter;
import com.register.exercise1.writer.ReportWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * Entry point for the IP address daily report batch job.
 * Reads access logs from {@code logfiles/requests.log},
 * aggregates traffic by IP, and writes the report to {@code reports/ipaddr.<format>}.
 * Usage:
 *   java -cp ... com.register.exercise1.Main [--format csv|json]
 */
public class Main {
    private static final Logger log = LoggerFactory.getLogger(Main.class);

    private static final String INPUT_PATH = "logfiles/requests.log";
    private static final String OUTPUT_DIR = "reports";
    private static final String DEFAULT_FORMAT = "csv";

    public static void main(String[] args) {
        String format = DEFAULT_FORMAT;

        for (int i = 0; i < args.length - 1; i++) {
            if (args[i].equals("--format")) {
                format = args[++i];
            }
        }

        if (!format.equals("csv") && !format.equals("json")) {
            log.error("Unknown format: {}. Supported: csv, json", format);
            //System.err.println("Unknown format: " + format + ". Supported: csv, json");
            System.exit(1);
        }

        Path inputPath  = Paths.get(INPUT_PATH);
        Path outputPath = Paths.get(OUTPUT_DIR, "ipaddr." + format);

        ReportWriter writer = switch (format) {
            case "csv"  -> new CsvReportWriter();
            case "json" -> new JsonReportWriter();
            default     -> throw new IllegalStateException("unreachable");
        };

        try {
            Files.createDirectories(outputPath.getParent());

            //Extract
            List<LogEntry> entries = new LogParser().parse(inputPath);
            //Transform
            List<ReportEntry> report  = new ReportAggregator().aggregate(entries);
            //Load
            writer.write(report, outputPath);

            log.info("Report written to: {}", outputPath);
            //System.out.println("Report written to: " + outputPath);
        } catch (IOException e) {
            log.error("Error: {}", e.getMessage());
            //System.err.println("Error: " + e.getMessage());
            System.exit(1);
        }
    }
}
