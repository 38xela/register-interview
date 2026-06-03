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
mvn test -Dtest="com.register.exercise1.**"
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

---

# Exercise 2 - Big Number Multiplication via Addition

Multiplies integers using only the addition operator, storing numbers as arrays of digits.
Supports arbitrarily large numbers — demonstrated by computing 100! (158 digits).

## Build

```bash
mvn -q -DskipTests package
```

## Run

```bash
java -cp target/register-interview-0.1.0-SNAPSHOT.jar com.register.exercise2.Main
```

Expected output:

```
15 x 2 = 30
100! = 93326215443944152681699238856266700490715968264381621468592963895217599993229915608941463976156518286253697920827223758251185210916864000000000000000000000000
```

## Run Tests

```bash
mvn test -Dtest="com.register.exercise2.**"
```

## Project Structure

```
src/main/java/com/register/exercise2/
├── Main.java          # Entry point, prints 15x2 and 100!
├── BigNumber.java     # Digit array representation: toDigits, add, toString
└── Multiplier.java    # multiply, addNTimes (private), factorial
```

