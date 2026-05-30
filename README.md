# register-interview
Repo for sharing interview exercises with the Register team

# Exercise 1 — IP Address Daily Report

Batch job that reads an HTTP access log, aggregates traffic by IP address,
and writes a daily report in CSV or JSON format.

## Prerequisites

- JDK 21+
- Maven 3.8+

Create input/output directories from project root:

```bash
mkdir -p logfiles reports
```

## Input Format

Place your log file at `logfiles/requests.log`.

Each line is semicolon-separated:

```
TIMESTAMP;BYTES;STATUS;REMOTE_ADDR
```

Example:

```
2024-01-01T10:00:00;1024;200;1.2.3.4
2024-01-01T10:00:01;512;404;5.6.7.8
```

Lines where STATUS is not `200` are excluded from the report.
Malformed lines are skipped with a warning printed to stderr.

## Build

```bash
mvn -q -DskipTests package
```

## Run

Default (CSV output to `reports/ipaddr.csv`):

```bash
java -cp target/register-interview-0.1.0-SNAPSHOT.jar com.register.exercise1.Main
```

JSON output to `reports/ipaddr.json`:

```bash
java -cp target/register-interview-0.1.0-SNAPSHOT.jar com.register.exercise1.Main --format json
```

## Output

Report sorted by number of requests (DESC), ties broken alphabetically by IP.

**CSV** (`reports/ipaddr.csv`):

```
ipAddress,requests,requestsPct,bytes,bytesPct
1.2.3.4,3,60.00,7168,79.93
5.6.7.8,1,20.00,1500,16.73
```

**JSON** (`reports/ipaddr.json`):

```json
[
  {"ipAddress":"1.2.3.4","requests":3,"requestsPct":"60.00","bytes":7168,"bytesPct":"79.93"},
  {"ipAddress":"5.6.7.8","requests":1,"requestsPct":"20.00","bytes":1500,"bytesPct":"16.73"}
]
```

## Run Tests

```bash
mvn test
```

## Project Structure

```
src/main/java/com/register/exercise1/
├── Main.java                 # Entry point, CLI args, pipeline wiring
├── model/
│   ├── LogEntry.java         # Raw parsed log line
│   └── ReportEntry.java      # Aggregated row per IP
├── parser/
│   └── LogParser.java        # Reads and parses requests.log
├── aggregator/
│   └── ReportAggregator.java # Groups by IP, computes percentages
└── writer/
    ├── ReportWriter.java     # Interface
    ├── CsvReportWriter.java  # CSV implementation
    └── JsonReportWriter.java # JSON implementation
```

