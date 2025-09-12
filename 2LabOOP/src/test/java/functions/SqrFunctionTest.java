package functions;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SqrFunctionTest {

    @Test
    void testApplyWithZero(){
        SqrFunction function = new SqrFunction();
        assertEquals(0.0, function.apply(0.0), 1.23e-6, "square 0 = 0, GOOD");
    }

    @Test
    void testApplyWithPositiveNumbers(){
        SqrFunction function = new SqrFunction();
        assertEquals(25.0, function.apply(5.0), 1.23e-6, "square 5 = 25, GOOD");
        assertEquals(625.0, function.apply(25.0), 1.23e-6, "square 25 = 625, GOOD");
        assertEquals(144.0, function.apply(12.0), 1.23e-6, "square 12 = 144, GOOD");
        assertEquals(9801.0, function.apply(99.0), 1.23e-6, "square 99 = 9801, GOOD");
        assertEquals(3721.0, function.apply(61.0), 1.23e-6, "square 61 = 3721, GOOD");
        assertEquals(1089.0, function.apply(33.0), 1.23e-6, "square 33 = 1089, GOOD");
    }

    @Test
    void testApplyWithFractionalPositiveNumbers(){
        SqrFunction function = new SqrFunction();
        assertEquals(30.25, function.apply(5.5), 1.23e-6, "square 5.5 = 25.25, GOOD");
        assertEquals(630.562321, function.apply(25.111), 1.23e-6, "square 25.111 = 630.562321, GOOD");
        assertEquals(149.0841, function.apply(12.21), 1.23e-6, "square 12.21 = 149.0841, GOOD");
        assertEquals(9998.0001, function.apply(99.99), 1.23e-6, "square 99.99 = 9998.0001, GOOD");
        assertEquals(3776.8399360000003, function.apply(61.456), 1.23e-6, "square 61.456 = 3776.8399360000003, GOOD");
        assertEquals(1111.1088888900001, function.apply(33.3333), 1.23e-6, "square 33.3333 = 1111.1088888900001, GOOD");
    }

    @Test
    void testApplyWithNegativeNumbers(){
        SqrFunction function = new SqrFunction();
        assertEquals(25.0, function.apply(-5.0), 1.23e-6, "square -5 = 25, GOOD");
        assertEquals(625.0, function.apply(-25.0), 1.23e-6, "square -25 = 625, GOOD");
        assertEquals(144.0, function.apply(-12.0), 1.23e-6, "square -12 = 144, GOOD");
        assertEquals(9801.0, function.apply(-99.0), 1.23e-6, "square -99 = 9801, GOOD");
        assertEquals(3721.0, function.apply(-61.0), 1.23e-6, "square -61 = 3721, GOOD");
        assertEquals(1089.0, function.apply(-33.0), 1.23e-6, "square -33 = 1089, GOOD");
    }

    @Test
    void testApplyWithFractionalNegativeNumbers(){
        SqrFunction function = new SqrFunction();
        assertEquals(30.25, function.apply(-5.5), 1.23e-6, "square -5.5 = 30.25, GOOD");
        assertEquals(630.562321, function.apply(-25.111), 1.23e-6, "square -25.111 = 630.562321, GOOD");
        assertEquals(149.0841, function.apply(-12.21), 1.23e-6, "square -12.21 = 149.0841, GOOD");
        assertEquals(9998.0001, function.apply(-99.99), 1.23e-6, "square -99.99 = 9998.0001, GOOD");
        assertEquals(3776.8399360000003, function.apply(-61.456), 1.23e-6, "square -61.456 = 3776.8399360000003, GOOD");
        assertEquals(1111.1088888900001, function.apply(-33.3333), 1.23e-6, "square -33.3333 = 1111.1088888900001, GOOD");
    }

    @Test
    void testApplyWithLargeNumbers(){
        SqrFunction function = new SqrFunction();
        assertEquals(1e6, function.apply(1e3), 1e23-6, "square 1e3 = 1e6, GOOD");
        assertEquals(1e12, function.apply(1e6), 1e23-6, "square 1e6 = 1e12, GOOD");
        assertEquals(1e18, function.apply(1e9), 1e23-6, "square 1e9 = 1e18, GOOD");
        assertEquals(1e14, function.apply(1e7), 1e23-6, "square 1e7 = 1e14, GOOD");
        assertEquals(1e10, function.apply(1e5), 1e23-6, "square 1e5 = 1e10, GOOD");
        assertEquals(1e16, function.apply(1e8), 1e23-6, "square 1e8 = 1e16, GOOD");
    }

    @Test
    void testApplyWithSmallNumbers(){
        SqrFunction function = new SqrFunction();
        assertEquals(1e-6, function.apply(1e-3), 1e23-6, "square 1e-3 = 1e-6, GOOD");
        assertEquals(1e-12, function.apply(1e-6), 1e23-6, "square 1e-6 = 1e-12, GOOD");
        assertEquals(1e-18, function.apply(1e-9), 1e23-6, "square 1e-9 = 1e-18, GOOD");
        assertEquals(1e-14, function.apply(1e-7), 1e23-6, "square 1e-7 = 1e-14, GOOD");
        assertEquals(1e-10, function.apply(1e-5), 1e23-6, "square 1e-5 = 1e-10, GOOD");
        assertEquals(1e-16, function.apply(1e-8), 1e23-6, "square 1e-8 = 1e-16, GOOD");
    }

    @Test
    void testApplyWithNaN(){
        SqrFunction function = new SqrFunction();
        assertTrue(Double.isNaN(function.apply(Double.NaN)), "square NaN = NaN, GOOD");
    }

    @Test
    void testApplyWithInfinity(){
        SqrFunction function = new SqrFunction();
        assertEquals(Double.POSITIVE_INFINITY, function.apply(Double.POSITIVE_INFINITY), 1.23e-6, "square +∞ = +∞, GOOD");
        assertEquals(Double.POSITIVE_INFINITY, function.apply(Double.NEGATIVE_INFINITY), 1.23e-6, "square -∞ = +∞, GOOD");
    }

    @Test
    void testApplyWithConsistency(){
        SqrFunction function = new SqrFunction();

        for(int i = 1; i <= 10; i++){
            double x = i * 0.7;
            assertEquals(function.apply(x), function.apply(-x), 1e23-6, "square x = square -x, GOOD");
        }
    }

    @Test
    void testApplyWithPrecision(){
        SqrFunction function = new SqrFunction();

        double[] testValues = {1.234, 4.567, 8.901, 2.345};
        double[] expectedValues = {1.522756, 20.857489, 79.227801, 5.499025};

        for (int i = 0; i < testValues.length; i++) {
            assertEquals(expectedValues[i], function.apply(testValues[i]), 0.000001,
                    "square " + testValues[i] + " = " + expectedValues[i] + ", GOOD");
        }
    }

    @Test
    void testMultipleCalls() {
        SqrFunction function = new SqrFunction();

        // Многократные вызовы с разными значениями
        for (int i = -50; i <= 50; i++) {
            double x = i * 0.1;
            assertEquals(x*x, function.apply(x), 0.0001,
                    "square " + x + " = " + x*x + ", GOOD");
        }
    }

}