package com.register.exercise2;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class BigNumberTest {

    @Test
    void toDigits_singleDigit_returnsSingleElementArray() {
        assertArrayEquals(new int[]{5}, BigNumber.toDigits(5));
    }
}