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
}