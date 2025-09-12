package functions;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class AbstractTabulatedFunctionTest {

    private static final double DELTA = 1e-10;

    @Test
    public void testInterpolateWithExplicitBounds() {
        MockTabulatedFunction function = new MockTabulatedFunction(0.0, 2.0, 1.0, 3.0);

        // Тестирование интерполяции в середине
        double result = function.interpolate(1.0, 0.0, 2.0, 1.0, 3.0);
        assertEquals(2.0, result, DELTA, "Interpolation at midpoint");

        // Тестирование интерполяции ближе к левой границе
        result = function.interpolate(0.5, 0.0, 2.0, 1.0, 3.0);
        assertEquals(1.5, result, DELTA, "Interpolation near left bound");

        // Тестирование интерполяции ближе к правой границе
        result = function.interpolate(1.5, 0.0, 2.0, 1.0, 3.0);
        assertEquals(2.5, result, DELTA, "Interpolation near right bound");

        // Тестирование с отрицательными значениями
        MockTabulatedFunction negativeFunction = new MockTabulatedFunction(-2.0, 2.0, -4.0, 4.0);
        result = negativeFunction.interpolate(0.0, -2.0, 2.0, -4.0, 4.0);
        assertEquals(0.0, result, DELTA, "Interpolation with negative values");
    }

    @Test
    public void testApplyWithExactMatch() {
        MockTabulatedFunction function = new MockTabulatedFunction(1.0, 3.0, 2.0, 6.0);

        // Точное совпадение с существующими x
        assertEquals(2.0, function.apply(1.0), DELTA, "Exact match at x0");
        assertEquals(6.0, function.apply(3.0), DELTA, "Exact match at x1");
    }

    @Test
    public void testApplyWithInterpolation() {
        MockTabulatedFunction function = new MockTabulatedFunction(0.0, 4.0, 0.0, 8.0);

        // Интерполяция внутри интервала
        assertEquals(2.0, function.apply(1.0), DELTA, "Interpolation at x=1");
        assertEquals(4.0, function.apply(2.0), DELTA, "Interpolation at x=2");
        assertEquals(6.0, function.apply(3.0), DELTA, "Interpolation at x=3");
    }

    @Test
    public void testApplyWithExtrapolationLeft() {
        MockTabulatedFunction function = new MockTabulatedFunction(2.0, 4.0, 4.0, 8.0);

        // Экстраполяция слева
        assertEquals(2.0, function.apply(1.0), DELTA, "Extrapolation left at x=1");
        assertEquals(0.0, function.apply(0.0), DELTA, "Extrapolation left at x=0");
        assertEquals(-4.0, function.apply(-2.0), DELTA, "Extrapolation left at x=-2");
    }

    @Test
    public void testApplyWithExtrapolationRight() {
        MockTabulatedFunction function = new MockTabulatedFunction(2.0, 4.0, 4.0, 8.0);

        // Экстраполяция справа
        assertEquals(10.0, function.apply(5.0), DELTA, "Extrapolation right at x=5");
        assertEquals(12.0, function.apply(6.0), DELTA, "Extrapolation right at x=6");
        assertEquals(16.0, function.apply(8.0), DELTA, "Extrapolation right at x=8");
    }

    @Test
    public void testApplyWithConstantFunction() {
        // Тестирование с постоянной функцией
        MockTabulatedFunction function = new MockTabulatedFunction(0.0, 10.0, 5.0, 5.0);

        // Все точки должны возвращать 5.0
        assertEquals(5.0, function.apply(-5.0), DELTA, "Constant function extrapolation left");
        assertEquals(5.0, function.apply(2.5), DELTA, "Constant function interpolation");
        assertEquals(5.0, function.apply(15.0), DELTA, "Constant function extrapolation right");
    }
}