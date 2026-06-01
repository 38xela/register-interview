package com.register.exercise2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BigNumber {

    public static int[] toDigits(int n) {
        if (n == 0) return new int[]{0};

        List<Integer> digits = new ArrayList<>();
        while (n > 0) {
            digits.add(n % 10);  // % naturally gives LSB first
            n /= 10;
        }
        return digits.stream().mapToInt(Integer::intValue).toArray();
    }


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
}