# Exercise 1 - IP Address Daily Report: Design Spec

## Overview

Batch job that reads an HTTP access log, aggregates traffic by IP address, and writes a daily report in CSV or JSON format. Runs once per day, no user interaction at runtime.

**Pattern:** ETL pipeline (Extract -> Transform -> Load)

**Input:** `logfiles/requests.log` (relative to project root)
**Output:** `reports/ipaddr.csv` (default) or `reports/ipaddr.json`

---

## Log format

Semicolon-separated, one record per line:

```
TIMESTAMP;BYTES;STATUS;REMOTE_ADDR
```

- `TIMESTAMP`: when the event occurred (kept as String, not parsed)
- `BYTES`: bytes sent to client (`long`)
- `STATUS`: HTTP numeric status code (`"200"`, `"404"`, etc.)
- `REMOTE_ADDR`: client IP address

**Filter:** include only rows where `STATUS.equals("200")` (RFC 2616, 200 OK).

---

## Package structure

```
src/main/java/com/register/
└── exercise1/
    ├── Main.java
    ├── model/
    │   ├── LogEntry.java
    │   └── ReportEntry.java
    ├── parser/
    │   └── LogParser.java
    ├── aggregator/
    │   └── ReportAggregator.java
    └── writer/
        ├── ReportWriter.java
        ├── CsvReportWriter.java
        └── JsonReportWriter.java

src/test/java/com/register/
└── exercise1/
    ├── parser/LogParserTest.java
    ├── aggregator/ReportAggregatorTest.java
    └── writer/
        ├── CsvReportWriterTest.java
        └── JsonReportWriterTest.java
```

---

## Data models

Both are Java records: immutable value objects with no logic.

### `LogEntry`

```java
public record LogEntry(
    String timestamp,
    long bytes,
    String status,
    String remoteAddr
) {}
```

### `ReportEntry`

```java
public record ReportEntry(
    String ipAddress,
    long requestCount,
    double requestPercentage,
    long totalBytes,
    double bytesPercentage
) {}
```

Percentages are computed in `ReportAggregator`, not in writers. Writers are display-only.

---

## Pipeline stages

### Stage 1: Extract (`LogParser`)

```java
public List<LogEntry> parse(Path inputPath) throws IOException
```

Per-line logic:
1. Skip blank lines (silent)
2. Split by `";"` with limit `-1` (preserves trailing empty fields for correct count check)
3. If `fields.length != 4` -> warn to stderr, skip
4. If `STATUS != "200"` -> skip silently (expected data, not corrupt)
5. If `BYTES` is not a valid `long` -> warn to stderr, skip
6. Otherwise -> emit `LogEntry`

Warnings use format: `Line N skipped: <reason>`

The parser uses an indexed `for` loop (not streams) to track line numbers for warnings.

### Stage 2: Transform (`ReportAggregator`)

```java
public List<ReportEntry> aggregate(List<LogEntry> entries)
```

Algorithm:
1. Guard: empty input -> return empty list (avoids division by zero)
2. Compute grand totals: `totalRequests = entries.size()`, `grandTotalBytes = sum of bytes`
3. Accumulate into `Map<String, long[]>` keyed by `remoteAddr`: `[requestCount, totalBytes]`
4. Build `List<ReportEntry>` with percentages (`requestCount / totalRequests * 100.0`, etc.)
5. Sort by `requestCount` DESC, ties broken alphabetically by IP:

```java
Comparator.comparingLong(ReportEntry::requestCount).reversed()
    .thenComparing(ReportEntry::ipAddress)
```

Pure function: no I/O, no side effects. Same input always produces same output.

### Stage 3: Load (`ReportWriter`)

```java
public interface ReportWriter {
    void write(List<ReportEntry> entries, Path outputPath) throws IOException;
}
```

#### `CsvReportWriter`

- Header: `ipAddress,requests,requestsPct,bytes,bytesPct`
- One row per entry, percentages formatted `"%.2f"`
- `BufferedWriter` + `StandardCharsets.UTF_8`
- No external library needed (IP addresses and numbers never contain commas)

#### `JsonReportWriter`

- Output: JSON array of objects, built manually (no Jackson/Gson)
- Fields: `ipAddress`, `requests`, `requestsPct`, `bytes`, `bytesPct`
- Comma handling via index iteration (no trailing comma on last element)
- Percentages formatted `"%.2f"` as strings, consistent with CSV

Example:
```json
[
  {"ipAddress":"1.2.3.4","requests":3,"requestsPct":"60.00","bytes":7168,"bytesPct":"79.93"},
  {"ipAddress":"5.6.7.8","requests":1,"requestsPct":"20.00","bytes":1500,"bytesPct":"16.73"}
]
```

---

## Main (Orchestrator)

Wires the pipeline, parses the optional `--format` arg, handles top-level errors.

### CLI args

| Arg | Default | Values |
|---|---|---|
| `--format <csv\|json>` | `csv` | `csv` \| `json` |

Input and output paths are fixed, not configurable via CLI.

### Paths

```java
private static final String INPUT_PATH = "logfiles/requests.log";
private static final String OUTPUT_DIR = "reports";
```

Paths resolve relative to the working directory. Run from the project root.

### Pipeline sequence

```
1. parse --format arg (default: csv)
2. validate format (exit 1 if unknown)
3. Files.createDirectories(outputPath.getParent())
4. LogParser.parse(inputPath)        -> List<LogEntry>
5. ReportAggregator.aggregate(...)   -> List<ReportEntry>
6. ReportWriter.write(..., outputPath)
7. System.exit(0)
```

### Writer selection

```java
if (!format.equals("csv") && !format.equals("json")) {
    System.err.println("Unknown format: " + format + ". Supported: csv, json");
    System.exit(1);
}

ReportWriter writer = switch (format) {
    case "csv"  -> new CsvReportWriter();
    case "json" -> new JsonReportWriter();
    default     -> throw new IllegalStateException("unreachable");
};
```

### Error handling

- `IOException` (missing file, permission denied, unwritable output) -> stderr message + `System.exit(1)`
- Unknown `--format` -> stderr + `System.exit(1)`
- Exit code `0` = success, non-zero = failure

---

## Testing strategy

JUnit 5 (already in `pom.xml`). No mocking framework. All classes take plain inputs and return plain outputs.

| Test class | Technique | Key cases |
|---|---|---|
| `LogParserTest` | `@TempDir` temp files | valid line, non-200 filtered, bad field count, unparseable bytes, blank lines, mixed lines |
| `ReportAggregatorTest` | inline `List<LogEntry>` | single IP, multiple IPs sorted DESC + alpha tie-break, correct percentages, empty input |
| `CsvReportWriterTest` | write to `@TempDir`, read back | header present, field order, 2dp percentages, empty list = header only, multiple rows |
| `JsonReportWriterTest` | write to `@TempDir`, read back | valid array, correct field names, no trailing comma, empty list = `[]`, multiple entries |

`Main` is not unit tested. Pure orchestration with no logic to assert on.

---

## Build & run

```bash
# Setup (once)
mkdir -p logfiles reports
# Place logfiles/requests.log before running

# Build
mvn -q -DskipTests package

# Run - CSV (default)
java -cp target/register-interview-0.1.0-SNAPSHOT.jar com.register.exercise1.Main

# Run - JSON
java -cp target/register-interview-0.1.0-SNAPSHOT.jar com.register.exercise1.Main --format json

# Run tests
mvn test
```
