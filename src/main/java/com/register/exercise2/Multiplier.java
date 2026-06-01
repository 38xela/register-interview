package com.register.exercise2;

public class Multiplier {
    public static int[] multiply(int a, int b) {
        return addNTimes(BigNumber.toDigits(a), b);
    }

    private static int[] addNTimes(int[] big, int n) {
        int[] result = new int[]{0};
        for (int i = 0; i < n; i++) {
            result = BigNumber.add(result, big);
        }
        return result;
    }
}
