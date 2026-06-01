package com.register.exercise2;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MultiplierTest {

    @Test
    void multiply_exerciseExample_15times2returns30() {
        assertArrayEquals(new int[]{0, 3}, Multiplier.multiply(15, 2));
    }
}