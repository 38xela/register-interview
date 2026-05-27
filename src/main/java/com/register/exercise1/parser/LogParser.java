package com.register.exercise1.parser;

import com.register.exercise1.model.LogEntry;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class LogParser {

    public List<LogEntry> parse(Path inputPath) throws IOException {
        List<LogEntry> entries = new ArrayList<>();

        for(String line : Files.readAllLines(inputPath)) {
            String[] fields = line.split(";", -1);
            long bytes = Long.parseLong(fields[1].trim());
            entries.add(new LogEntry(fields[0].trim(), bytes, fields[2].trim(), fields[3].trim()));
        }

        return entries;
    }
}
