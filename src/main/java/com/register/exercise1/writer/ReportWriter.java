package com.register.exercise1.writer;

import com.register.exercise1.model.ReportEntry;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public interface ReportWriter {

    void write(List<ReportEntry> entries, Path outputPath) throws IOException;
}
