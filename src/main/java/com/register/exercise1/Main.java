package com.register.exercise1;

import com.register.exercise1.aggregator.ReportAggregator;
import com.register.exercise1.config.Config;
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

    public static void main(String[] args) {
        Config config = new Config();

        String format = config.defaultFormat();

        for (int i = 0; i < args.length - 1; i++) {
            if (args[i].equals("--format")) {
                format = args[++i];
            }
        }

        if (!format.equals("csv") && !format.equals("json")) {
            log.error("Unknown format: {}. Supported: csv, json", format);
            System.exit(1);
        }

        Path inputPath  = Paths.get(config.inputPath());
        Path outputPath = Paths.get(config.outputDir(), config.outputFilename() + "." + format);

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
        } catch (IOException e) {
            log.error("Error: {}", e.getMessage());
            System.exit(1);
        }
    }
}
