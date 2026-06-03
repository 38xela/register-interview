package com.register.exercise2;

/**
 * Implements integer multiplication using only the addition operator.
 * Supports arbitrarily large results via LSB-first digit arrays from {@link BigNumber}.
 */
public class Multiplier {

    /**
     * Multiplies two non-negative integers using repeated addition.
     *
     * @param a first operand
     * @param b second operand
     * @return product as an LSB-first digit array
     */
    public static int[] multiply(int a, int b) {
        return addNTimes(BigNumber.toDigits(a), b);
    }

    /**
     * Adds a big number to itself n times (equivalent to big * n).
     *
     * @param big LSB-first digit array to add repeatedly
     * @param n   number of times to add
     * @return result as a new LSB-first digit array
     */
    private static int[] addNTimes(int[] big, int n) {
        int[] result = new int[]{0};
        for (int i = 0; i < n; i++) {
            result = BigNumber.add(result, big);
        }
        return result;
    }

    /**
     * Computes the factorial of n (n!) using repeated multiplication via addition.
     * Supports large values: factorial(100) produces a 158-digit result.
     *
     * @param n non-negative integer
     * @return n! as an LSB-first digit array
     */
    public static int[] factorial(int n) {
        int[] result = BigNumber.toDigits(1);
        for (int k = 2; k <= n; k++) {
            result = addNTimes(result, k);
        }
        return result;
    }
}
