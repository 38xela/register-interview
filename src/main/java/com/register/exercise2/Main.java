package com.register.exercise2;

public class Main {

    public static void main(String[] args) {
        // Exercise example: 15 x 2
        int[] result = Multiplier.multiply(15, 2);
        System.out.println("15 x 2 = " + BigNumber.toString(result));

        // 100!
        int[] fact100 = Multiplier.factorial(100);
        System.out.println("100! = " + BigNumber.toString(fact100));
    }
}
