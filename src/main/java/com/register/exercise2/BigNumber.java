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
}