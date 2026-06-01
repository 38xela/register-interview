package com.register.exercise2;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class BigNumberTest {

    @Test
    void toDigits_singleDigit_returnsSingleElementArray() {
        assertArrayEquals(new int[]{5}, BigNumber.toDigits(5));
    }

    @Test
    void toDigits_multiDigit_returnsLsbFirst() {
        assertArrayEquals(new int[]{3, 2, 1}, BigNumber.toDigits(123));
    }

    @Test
    void toDigits_zero_returnsSingleZero() {
        assertArrayEquals(new int[]{0}, BigNumber.toDigits(0));
    }

    @Test
    void add_noCarry_returnsSum() {
        assertArrayEquals(new int[]{3}, BigNumber.add(new int[]{1}, new int[]{2}));
    }

    @Test
    void add_withCarry_propagatesCorrectly() {
        assertArrayEquals(new int[]{0, 1}, BigNumber.add(new int[]{9}, new int[]{1}));
    }
}