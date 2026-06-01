# Exercise 2 - Big Number Multiplication via Addition: Design Spec

## Overview

Implement integer multiplication using only the addition operator (+), storing numbers as arrays of decimal digits. The implementation must support computing 100! (factorial of 100), whose result (~9.33 x 10^157) overflows all Java primitive types.

**Core insight:** represent arbitrarily large numbers as arrays of single digits, add digit by digit with carry, and implement multiplication as repeated addition.

---

## Why arrays are required

100! has 158 digits. No Java primitive can hold it:

| Type | Max value |
|---|---|
| `int` | ~2.1 x 10^9 |
| `long` | ~9.2 x 10^18 |
| `double` | ~15-17 significant digits |

Arrays of digits have no upper bound on size.

---

## Array representation

Numbers are stored least significant digit first (LSB-first):

```
123  ->  [3, 2, 1]
  0  ->  [0]
  9  ->  [9]
100  ->  [0, 0, 1]
```

LSB-first means carry always propagates left to right (index 0 upward). Array grows naturally at the end with no index gymnastics.

---

## Package structure

```
src/main/java/com/register/
└── exercise2/
    ├── BigNumber.java     # digit array representation: toDigits, add, toString
    ├── Multiplier.java    # arithmetic: multiply (public), addNTimes (private), factorial
    └── Main.java          # entry point, demonstrates 15x2 and 100!

src/test/java/com/register/
└── exercise2/
    ├── BigNumberTest.java
    └── MultiplierTest.java
```

---

## Class design

### `BigNumber` - representation and addition

`BigNumber` is a static utility class. It owns the digit array format and the only operation that touches individual digits.

```java
/** Converts a non-negative int to LSB-first digit array. */
public static int[] toDigits(int n)

/** Adds two LSB-first digit arrays. Returns a new LSB-first array. */
public static int[] add(int[] a, int[] b)

/** Converts LSB-first digit array back to a readable string. */
public static String toString(int[] digits)
```

**`toDigits` algorithm:** uses `%` and `/` to extract digits naturally in LSB order:
```
if n == 0: return [0]
while n > 0:
    digits.add(n % 10)   // % gives least significant digit first
    n = n / 10
```

No string conversion needed. `n % 10` naturally produces LSB-first order.

**`add` algorithm:** single loop over digit positions, carry handled separately:
```
for i = 0 to max(a.length, b.length) - 1:
    digitA = a[i] if i < a.length else 0
    digitB = b[i] if i < b.length else 0
    sum = digitA + digitB + carry
    result[i] = sum % 10
    carry = sum / 10

result[maxLen] = carry   // handle final carry after loop
```

Loop has single condition `i < maxLen`. Final carry handled explicitly after. Each `+` operates on single digits [0-9] plus carry [0-1]. Max intermediate sum: 9 + 9 + 1 = 19.

**`toString` algorithm:** appends digits forward then reverses:
```java
StringBuilder sb = new StringBuilder();
for (int d : digits) sb.append(d);   // LSB first = reversed string
return sb.reverse().toString();       // flip to readable order
```

### `Multiplier` - multiplication and factorial

`Multiplier` is a static utility class. It builds on `BigNumber.add` to implement multiplication as repeated addition.

```java
/** Multiplies two non-negative ints. Returns LSB-first digit array. */
public static int[] multiply(int a, int b)

/** Adds big to itself n times. Core of multiplication. */
private static int[] addNTimes(int[] big, int n)

/** Computes n! as a LSB-first digit array. */
public static int[] factorial(int n)
```

**`addNTimes` algorithm:**
```
result = [0]
repeat n times:
    result = BigNumber.add(result, big)
return result
```

**`factorial` algorithm:**
```
result = [1]
for k = 2 to n:
    result = addNTimes(result, k)
return result
```

**Performance for 100!:**
Total additions = 1 + 2 + 3 + ... + 99 = 4950.
Each addition touches at most 158 digits.
Total single-digit operations: ~782,100. Runs in milliseconds.

### `Main` - entry point

Demonstrates the exercise example (15 x 2) and computes 100!:

```java
int[] result = Multiplier.multiply(15, 2);
System.out.println("15 x 2 = " + BigNumber.toString(result));

int[] fact100 = Multiplier.factorial(100);
System.out.println("100! = " + BigNumber.toString(fact100));
```

---

## Edge cases

| Input | Expected output |
|---|---|
| `multiply(0, n)` | `[0]` |
| `multiply(n, 0)` | `[0]` |
| `multiply(1, n)` | `toDigits(n)` |
| `factorial(0)` | `[1]` (by convention: 0! = 1) |
| `factorial(1)` | `[1]` |
| `toDigits(0)` | `[0]` |

---

## Testing strategy

JUnit 5. No I/O, no filesystem. All methods are pure functions - same input, same output.

### `BigNumberTest`

| Test | Input | Expected |
|---|---|---|
| `toDigits` single digit | `toDigits(5)` | `[5]` |
| `toDigits` multi digit | `toDigits(123)` | `[3, 2, 1]` |
| `toDigits` zero | `toDigits(0)` | `[0]` |
| `add` no carry | `add([1], [2])` | `[3]` |
| `add` with carry | `add([9], [1])` | `[0, 1]` |
| `add` carry propagates | `add([9,9], [1])` | `[0, 0, 1]` |
| `add` different lengths | `add([5,2], [8])` | `[3, 3]` |
| `toString` | `toString([3,2,1])` | `"123"` |

### `MultiplierTest`

| Test | Input | Expected |
|---|---|---|
| Exercise example | `multiply(15, 2)` | `[0, 3]` (= 30) |
| Multiply by zero | `multiply(5, 0)` | `[0]` |
| Multiply by one | `multiply(7, 1)` | `[7]` |
| Commutativity | `multiply(3, 4)` == `multiply(4, 3)` | `[2, 1]` (= 12) |
| `factorial(0)` | `factorial(0)` | `[1]` |
| `factorial(5)` | `factorial(5)` | `[0, 2, 1]` (= 120) |
| `factorial(10)` | `factorial(10)` | `[0,0,8,8,2,6,3]` (= 3628800) |
| `factorial(100)` | result has 158 digits | verify length and first/last digits |

---

## Build & run

```bash
# Build
mvn -q -DskipTests package

# Run
java -cp target/register-interview-0.1.0-SNAPSHOT.jar com.register.exercise2.Main

# Run tests
mvn test
```
