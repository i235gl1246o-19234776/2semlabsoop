package functions;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ConstantFunctionTest {

    @Test
    void testApplyWithPositiveConstant() {
        // f(x) = 5.0 (всегда возвращает 5.0)
        ConstantFunction function = new ConstantFunction(5.0);

        assertEquals(5.0, function.apply(0.0), 1e-10, "Должен возвращать 5.0 для x=0");
        assertEquals(5.0, function.apply(10.0), 1e-10, "Должен возвращать 5.0 для x=10");
        assertEquals(5.0, function.apply(-5.0), 1e-10, "Должен возвращать 5.0 для x=-5");
        assertEquals(5.0, function.apply(100.0), 1e-10, "Должен возвращать 5.0 для x=100");
    }

    @Test
    void testApplyWithNegativeConstant() {
        // f(x) = -3.14 (всегда возвращает -3.14)
        ConstantFunction function = new ConstantFunction(-3.14);

        assertEquals(-3.14, function.apply(0.0), 1e-10, "Должен возвращать -3.14 для x=0");
        assertEquals(-3.14, function.apply(1.0), 1e-10, "Должен возвращать -3.14 для x=1");
        assertEquals(-3.14, function.apply(-1.0), 1e-10, "Должен возвращать -3.14 для x=-1");
    }

    @Test
    void testApplyWithZeroConstant() {
        // f(x) = 0.0 (всегда возвращает 0.0)
        ConstantFunction function = new ConstantFunction(0.0);

        assertEquals(0.0, function.apply(0.0), 1e-10, "Должен возвращать 0.0 для x=0");
        assertEquals(0.0, function.apply(123.45), 1e-10, "Должен возвращать 0.0 для x=123.45");
        assertEquals(0.0, function.apply(-67.89), 1e-10, "Должен возвращать 0.0 для x=-67.89");
    }

    @Test
    void testApplyWithFractionalConstant() {
        // f(x) = 2.5 (всегда возвращает 2.5)
        ConstantFunction function = new ConstantFunction(2.5);

        assertEquals(2.5, function.apply(0.0), 1e-10, "Должен возвращать 2.5 для x=0");
        assertEquals(2.5, function.apply(10.0), 1e-10, "Должен возвращать 2.5 для x=10");
        assertEquals(2.5, function.apply(-10.0), 1e-10, "Должен возвращать 2.5 для x=-10");
    }

    @Test
    void testGetConstantMethod() {
        ConstantFunction function1 = new ConstantFunction(7.0);
        ConstantFunction function2 = new ConstantFunction(-2.0);
        ConstantFunction function3 = new ConstantFunction(0.0);
        ConstantFunction function4 = new ConstantFunction(3.14159);

        assertEquals(7.0, function1.getConstant(), 1e-10, "getConstant() должен возвращать 7.0");
        assertEquals(-2.0, function2.getConstant(), 1e-10, "getConstant() должен возвращать -2.0");
        assertEquals(0.0, function3.getConstant(), 1e-10, "getConstant() должен возвращать 0.0");
        assertEquals(3.14159, function4.getConstant(), 1e-10, "getConstant() должен возвращать 3.14159");
    }

    @Test
    void testApplyWithExtremeValues() {
        ConstantFunction maxValue = new ConstantFunction(Double.MAX_VALUE);
        ConstantFunction minValue = new ConstantFunction(Double.MIN_VALUE);
        ConstantFunction positiveInfinity = new ConstantFunction(Double.POSITIVE_INFINITY);
        ConstantFunction negativeInfinity = new ConstantFunction(Double.NEGATIVE_INFINITY);
        ConstantFunction nan = new ConstantFunction(Double.NaN);

        assertEquals(Double.MAX_VALUE, maxValue.apply(0.0), 1e-10, "Должен возвращать Double.MAX_VALUE");
        assertEquals(Double.MIN_VALUE, minValue.apply(0.0), 1e-10, "Должен возвращать Double.MIN_VALUE");
        assertEquals(Double.POSITIVE_INFINITY, positiveInfinity.apply(0.0), 1e-10, "Должен возвращать POSITIVE_INFINITY");
        assertEquals(Double.NEGATIVE_INFINITY, negativeInfinity.apply(0.0), 1e-10, "Должен возвращать NEGATIVE_INFINITY");
        assertTrue(Double.isNaN(nan.apply(0.0)), "Должен возвращать NaN");
    }

    @Test
    void testImplementsMathFunctionInterface() {
        ConstantFunction function = new ConstantFunction(1.0);
        assertTrue(function instanceof MathFunction, "ConstantFunction должен реализовывать MathFunction");
    }
}