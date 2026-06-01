package com.register.exercise2;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MultiplierTest {

    @Test
    void multiply_exerciseExample_15times2returns30() {
        assertArrayEquals(new int[]{0, 3}, Multiplier.multiply(15, 2));
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

    
}