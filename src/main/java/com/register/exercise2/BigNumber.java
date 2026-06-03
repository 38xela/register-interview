package com.register.exercise2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Utility class for arbitrary-precision integer arithmetic.
 * Numbers are represented as int arrays with the least significant digit at index 0.
 * Example: 123 is stored as [3, 2, 1].
 */
public class BigNumber {

    /**
     * Converts a non-negative integer to an LSB-first digit array.
     *
     * @param n non-negative integer to convert
     * @return LSB-first digit array, e.g. 123 -> [3, 2, 1]
     */
    public static int[] toDigits(int n) {
        if (n == 0) return new int[]{0};

        List<Integer> digits = new ArrayList<>();
        while (n > 0) {
            digits.add(n % 10);
            n /= 10;
        }
        return digits.stream().mapToInt(Integer::intValue).toArray();
    }


    /**
     * Adds two LSB-first digit arrays using digit-by-digit addition with carry.
     * Each individual addition operates on single digits [0-9] plus carry [0-1].
     *
     * @param a first operand as LSB-first digit array
     * @param b second operand as LSB-first digit array
     * @return sum as a new LSB-first digit array
     */
    public static int[] add(int[] a, int[] b) {
        int maxLen = Math.max(a.length, b.length);
        int[] result = new int[maxLen + 1];
        int carry = 0;

        for (int i = 0; i < maxLen; i++) {
            int digitA = i < a.length ? a[i] : 0;
            int digitB = i < b.length ? b[i] : 0;
            int sum = digitA + digitB + carry;
            result[i] = sum % 10;
            carry = sum / 10;
        }

        result[maxLen] = carry;

        return carry > 0 ? result : Arrays.copyOf(result, maxLen);
    }

    /**
     * Converts an LSB-first digit array to a human-readable string.
     *
     * @param digits LSB-first digit array
     * @return number as a string, e.g. [3, 2, 1] -> "123"
     */
    public static String toString(int[] digits) {
        StringBuilder sb = new StringBuilder();
        for (int d : digits) {
            sb.append(d);
        }
        return sb.reverse().toString();
    }
}