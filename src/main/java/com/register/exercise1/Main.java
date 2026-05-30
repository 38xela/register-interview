package com.register.exercise1;

import com.register.exercise1.aggregator.ReportAggregator;
import com.register.exercise1.model.LogEntry;
import com.register.exercise1.model.ReportEntry;
import com.register.exercise1.parser.LogParser;
import com.register.exercise1.writer.CsvReportWriter;
import com.register.exercise1.writer.JsonReportWriter;
import com.register.exercise1.writer.ReportWriter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class Main {

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
            System.err.println("Unknown format: " + format + ". Supported: csv, json");
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

            List<LogEntry> entries = new LogParser().parse(inputPath);
            List<ReportEntry> report  = new ReportAggregator().aggregate(entries);
            writer.write(report, outputPath);

            System.out.println("Report written to: " + outputPath);
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
            System.exit(1);
        }
    }
}
