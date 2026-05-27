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

}