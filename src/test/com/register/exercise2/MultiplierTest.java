package com.register.exercise2;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MultiplierTest {

    @Test
    void multiply_exerciseExample_15times2returns30() {
        assertArrayEquals(new int[]{0, 3}, Multiplier.multiply(15, 2));
    }

    @Test
    void multiply_exerciseExample_15times7returns105() {
        assertArrayEquals(new int[]{0, 3}, Multiplier.multiply(15, 7));
    }

    @Test
    void multiply_byZero_returnsZero() {
        assertArrayEquals(new int[]{0}, Multiplier.multiply(5, 0));
    }

    @Test
    void multiply_byOne_returnsOriginalNumber() {
        assertArrayEquals(new int[]{7}, Multiplier.multiply(7, 1));
    }

    @Test
    void multiply_isCommutative() {
        assertArrayEquals(Multiplier.multiply(3, 4), Multiplier.multiply(4, 3));
    }

    @Test
    void factorial_zero_returnsOne() {
        assertArrayEquals(new int[]{1}, Multiplier.factorial(0));
    }

    @Test
    void factorial_one_returnsOne() {
        assertArrayEquals(new int[]{1}, Multiplier.factorial(1));
    }

    @Test
    void factorial_five_returns120() {
        assertArrayEquals(new int[]{0, 2, 1}, Multiplier.factorial(5));
    }

    @Test
    void factorial_ten_returns3628800() {
        assertArrayEquals(new int[]{0, 0, 8, 8, 2, 6, 3}, Multiplier.factorial(10));
    }

    @Test
    void factorial_100_has158Digits() {
        int[] result = Multiplier.factorial(100);
        assertEquals(158, result.length);
        assertEquals(0, result[0]);   // last digit is 0
        assertEquals(9, result[157]); // first digit is 9
    }
}