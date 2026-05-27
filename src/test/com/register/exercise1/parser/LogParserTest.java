package com.register.exercise1.parser;

import com.register.exercise1.model.LogEntry;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class LogParserTest {

    @TempDir
    Path tempDir;

    @Test
    void validLine_status200_returnsOneEntry() throws IOException {
        Path input = tempDir.resolve("requests.log");
        Files.writeString(input, "2024-01-01T10:00:00;1024;200;1.2.3.4\n");

        List<LogEntry> result = new LogParser().parse(input);

        assertEquals(1, result.size());
        assertEquals("1.2.3.4", result.get(0).remoteAddr());
        assertEquals(1024L, result.get(0).bytes());
        assertEquals("200", result.get(0).status());
    }

    @Test
    void nonOkStatus_filtered_returnsEmpty() throws IOException {
        Path input = tempDir.resolve("requests.log");
        Files.writeString(input, "2024-01-01T10:00:00;1024;404;1.2.3.4\n");

        List<LogEntry> result = new LogParser().parse(input);

        assertEquals(0, result.size());
    }

    @Test
    void blankLine_skipped_returnsEmpty() throws IOException {
        Path input = tempDir.resolve("requests.log");
        Files.writeString(input, "\n\n\n");

        List<LogEntry> result = new LogParser().parse(input);

        assertEquals(0, result.size());
    }

    @Test
    void malformedLine_wrongFieldCount_skipped() throws IOException {
        Path input = tempDir.resolve("requests.log");
        Files.writeString(input, "2024-01-01T10:00:00;1024;200\n");

        List<LogEntry> result = new LogParser().parse(input);

        assertEquals(0, result.size());
    }

    @Test
    void nonNumericBytes_skipped() throws IOException {
        Path input = tempDir.resolve("requests.log");
        Files.writeString(input, "2024-01-01T10:00:00;abc;200;1.2.3.4\n");

        List<LogEntry> result = new LogParser().parse(input);

        assertEquals(0, result.size());
    }

    @Test
    void mixedLines_onlyOkReturned() throws IOException {
        Path input = tempDir.resolve("requests.log");
        Files.writeString(input,
                "2024-01-01T10:00:00;1024;200;1.2.3.4\n" +
                        "2024-01-01T10:00:01;512;404;5.6.7.8\n" +
                        "2024-01-01T10:00:02;2048;200;9.10.11.12\n"
        );

        List<LogEntry> result = new LogParser().parse(input);

        assertEquals(2, result.size());
        assertEquals("1.2.3.4", result.get(0).remoteAddr());
        assertEquals("9.10.11.12", result.get(1).remoteAddr());
    }

}