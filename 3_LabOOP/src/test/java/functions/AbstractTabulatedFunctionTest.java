package functions;

import exception.ArrayIsNotSortedException;
import exception.DifferentLengthOfArraysException;
import exception.InterpolationException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class AbstractTabulatedFunctionTest {

    private static final double DELTA = 1e-10;

    // ========================
    // Тестирование floorIndexOfX
    // ========================

    @Test
    public void testFloorIndexOfX_BelowLeftBound() {
        MockTabulatedFunction f = new MockTabulatedFunction(0.0, 2.0, 1.0, 3.0);
        assertEquals(0, f.floorIndexOfX(-1.0), "x < x0 should return 0");
        assertEquals(0, f.floorIndexOfX(0.0 - DELTA * 2), "x just below x0 should return 0");
    }

    @Test
    public void testFloorIndexOfX_AtLeftBound() {
        MockTabulatedFunction f = new MockTabulatedFunction(0.0, 2.0, 1.0, 3.0);
        assertEquals(0, f.floorIndexOfX(0.0), "x == x0 should return 0");
    }

    @Test
    public void testFloorIndexOfX_BetweenBounds() {
        MockTabulatedFunction f = new MockTabulatedFunction(0.0, 2.0, 1.0, 3.0);
        assertEquals(0, f.floorIndexOfX(0.5), "x between x0 and x1 should return 0");
        assertEquals(0, f.floorIndexOfX(1.999), "x just below x1 should return 0");
    }

    @Test
    public void testFloorIndexOfX_AtRightBound() {
        MockTabulatedFunction f = new MockTabulatedFunction(0.0, 2.0, 1.0, 3.0);
        assertEquals(1, f.floorIndexOfX(2.0), "x == x1 should return 1");
    }

    @Test
    public void testFloorIndexOfX_AboveRightBound() {
        MockTabulatedFunction f = new MockTabulatedFunction(0.0, 2.0, 1.0, 3.0);
        assertEquals(2, f.floorIndexOfX(2.1), "x > x1 should return count");
        assertEquals(2, f.floorIndexOfX(100.0), "should return count");
    }


    // ========================
    // Тестирование interpolate(double, double, double, double, double)
    // ========================

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

    // ========================
    // Тестирование apply()
    // ========================

    @Test
    public void testApplyWithExactMatch() {
        MockTabulatedFunction function = new MockTabulatedFunction(1.0, 3.0, 2.0, 6.0);
        assertEquals(2.0, function.apply(1.0), DELTA, "Exact match at x0");
        assertEquals(6.0, function.apply(3.0), DELTA, "Exact match at x1");
    }

    @Test
    public void testApplyWithInterpolation() {
        MockTabulatedFunction function = new MockTabulatedFunction(0.0, 4.0, 0.0, 8.0);
        assertEquals(2.0, function.apply(1.0), DELTA, "Interpolation at x=1");
        assertEquals(4.0, function.apply(2.0), DELTA, "Interpolation at x=2");
        assertEquals(6.0, function.apply(3.0), DELTA, "Interpolation at x=3");
    }

    @Test
    public void testApplyWithExtrapolationLeft() {
        MockTabulatedFunction function = new MockTabulatedFunction(2.0, 4.0, 4.0, 8.0);
        assertEquals(2.0, function.apply(1.0), DELTA, "Extrapolation left at x=1");
        assertEquals(0.0, function.apply(0.0), DELTA, "Extrapolation left at x=0");
        assertEquals(-4.0, function.apply(-2.0), DELTA, "Extrapolation left at x=-2");
    }

    @Test
    public void testApplyWithExtrapolationRight() {
        MockTabulatedFunction function = new MockTabulatedFunction(2.0, 4.0, 4.0, 8.0);
        assertEquals(10.0, function.apply(5.0), DELTA, "Extrapolation right at x=5");
        assertEquals(12.0, function.apply(6.0), DELTA, "Extrapolation right at x=6");
        assertEquals(16.0, function.apply(8.0), DELTA, "Extrapolation right at x=8");
    }

    @Test
    public void testApplyWithConstantFunction() {
        MockTabulatedFunction function = new MockTabulatedFunction(0.0, 10.0, 5.0, 5.0);
        assertEquals(5.0, function.apply(-5.0), DELTA, "Constant function extrapolation left");
        assertEquals(5.0, function.apply(2.5), DELTA, "Constant function interpolation");
        assertEquals(5.0, function.apply(15.0), DELTA, "Constant function extrapolation right");
    }

    // ========================
    // Тестирование getCount()
    // ========================

    @Test
    public void testGetCount() {
        MockTabulatedFunction f = new MockTabulatedFunction(0, 1, 0, 1);
        assertEquals(2, f.getCount(), "Count should be 2 for two points");
    }

    // ========================
    // Тестирование getX / getY
    // ========================

    @Test
    public void testGetX_Index0() {
        MockTabulatedFunction f = new MockTabulatedFunction(10.0, 20.0, 100.0, 200.0);
        assertEquals(10.0, f.getX(0), DELTA, "getX(0) should return x0");
    }

    @Test
    public void testGetX_Index1() {
        MockTabulatedFunction f = new MockTabulatedFunction(10.0, 20.0, 100.0, 200.0);
        assertEquals(20.0, f.getX(1), DELTA, "getX(1) should return x1");
    }

    @Test
    public void testGetX_IndexOutOfBounds() {
        MockTabulatedFunction f = new MockTabulatedFunction(0, 1, 0, 1);
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> f.getX(2));
        assertEquals("Index out of bounds: 2", thrown.getMessage(), "Exception message for invalid index");
    }

    @Test
    public void testGetY_Index0() {
        MockTabulatedFunction f = new MockTabulatedFunction(10.0, 20.0, 100.0, 200.0);
        assertEquals(100.0, f.getY(0), DELTA, "getY(0) should return y0");
    }

    @Test
    public void testGetY_Index1() {
        MockTabulatedFunction f = new MockTabulatedFunction(10.0, 20.0, 100.0, 200.0);
        assertEquals(200.0, f.getY(1), DELTA, "getY(1) should return y1");
    }

    @Test
    public void testGetY_IndexOutOfBounds() {
        MockTabulatedFunction f = new MockTabulatedFunction(0, 1, 0, 1);
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> f.getY(2));
        assertEquals("Index out of bounds: 2", thrown.getMessage(), "Exception message for invalid index");
    }

    // ========================
    // Тестирование setY
    // ========================

    @Test
    public void testSetY_ThrowsUnsupportedOperationException() {
        MockTabulatedFunction f = new MockTabulatedFunction(0, 1, 0, 1);
        UnsupportedOperationException thrown = assertThrows(UnsupportedOperationException.class, () -> f.setY(0, 5.0));
        assertEquals("Mock object is immutable", thrown.getMessage(), "Expected exception message");
    }

    // ========================
    // Тестирование indexOfX
    // ========================

    @Test
    public void testIndexOfX_ExactMatch_x0() {
        MockTabulatedFunction f = new MockTabulatedFunction(1.0, 3.0, 2.0, 6.0);
        assertEquals(0, f.indexOfX(1.0), "indexOfX(x0) should return 0");
    }

    @Test
    public void testIndexOfX_ExactMatch_x1() {
        MockTabulatedFunction f = new MockTabulatedFunction(1.0, 3.0, 2.0, 6.0);
        assertEquals(1, f.indexOfX(3.0), "indexOfX(x1) should return 1");
    }

    @Test
    public void testIndexOfX_CloseTo_x0() {
        MockTabulatedFunction f = new MockTabulatedFunction(1.0, 3.0, 2.0, 6.0);
        assertEquals(0, f.indexOfX(1.0 + DELTA * 0.9999), "Close to x0 should still match");
        assertEquals(0, f.indexOfX(1.0 - DELTA * 0.9999), "Close to x0 from below should still match");
    }

    @Test
    public void testIndexOfX_CloseTo_x1() {
        MockTabulatedFunction f = new MockTabulatedFunction(1.0, 3.0, 2.0, 6.0);
        assertEquals(1, f.indexOfX(3.0 + DELTA * 0.9999), "Close to x1 should still match");
        assertEquals(1, f.indexOfX(3.0 - DELTA * 0.9999), "Close to x1 from below should still match");
    }

    @Test
    public void testIndexOfX_NotFound() {
        MockTabulatedFunction f = new MockTabulatedFunction(1.0, 3.0, 2.0, 6.0);
        assertEquals(-1, f.indexOfX(2.0), "x=2.0 not in table should return -1");
        assertEquals(-1, f.indexOfX(0.0), "x=0.0 not in table should return -1");
        assertEquals(-1, f.indexOfX(4.0), "x=4.0 not in table should return -1");
    }

    // ========================
    // Тестирование indexOfY
    // ========================

    @Test
    public void testIndexOfY_ExactMatch_y0() {
        MockTabulatedFunction f = new MockTabulatedFunction(1.0, 3.0, 2.0, 6.0);
        assertEquals(0, f.indexOfY(2.0), "indexOfY(y0) should return 0");
    }

    @Test
    public void testIndexOfY_ExactMatch_y1() {
        MockTabulatedFunction f = new MockTabulatedFunction(1.0, 3.0, 2.0, 6.0);
        assertEquals(1, f.indexOfY(6.0), "indexOfY(y1) should return 1");
    }

    @Test
    public void testIndexOfY_CloseTo_y0() {
        MockTabulatedFunction f = new MockTabulatedFunction(1.0, 3.0, 2.0, 6.0);
        assertEquals(0, f.indexOfY(2.0 + DELTA * 0.9999), "Close to y0 should still match");
        assertEquals(0, f.indexOfY(2.0 - DELTA * 0.9999), "Close to y0 from below should still match");
    }

    @Test
    public void testIndexOfY_CloseTo_y1() {
        MockTabulatedFunction f = new MockTabulatedFunction(1.0, 3.0, 2.0, 6.0);
        assertEquals(1, f.indexOfY(6.0 + DELTA * 0.9999), "Close to y1 should still match");
        assertEquals(1, f.indexOfY(6.0 - DELTA * 0.9999), "Close to y1 from below should still match");
    }

    @Test
    public void testIndexOfY_NotFound() {
        MockTabulatedFunction f = new MockTabulatedFunction(1.0, 3.0, 2.0, 6.0);
        assertEquals(-1, f.indexOfY(3.0), "y=3.0 not in table should return -1");
        assertEquals(-1, f.indexOfY(1.0), "y=1.0 not in table should return -1");
        assertEquals(-1, f.indexOfY(7.0), "y=7.0 not in table should return -1");
    }

    // ========================
    // Тестирование leftBound / rightBound
    // ========================

    @Test
    public void testLeftBound() {
        MockTabulatedFunction f = new MockTabulatedFunction(-5.0, 10.0, 0, 0);
        assertEquals(-5.0, f.leftBound(), DELTA, "leftBound should return x0");
    }

    @Test
    public void testRightBound() {
        MockTabulatedFunction f = new MockTabulatedFunction(-5.0, 10.0, 0, 0);
        assertEquals(10.0, f.rightBound(), DELTA, "rightBound should return x1");
    }

    // ========================
    // Тестирование конструктора
    // ========================

    @Test
    public void testConstructor_ThrowsOnInvalidRange() {
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
                () -> new MockTabulatedFunction(2.0, 1.0, 0, 0));
        assertEquals("x0 > x1, NOT GOOD", thrown.getMessage());
    }




        // ========================
        // Тесты для checkLengthIsTheSame
        // ========================

    @Test
    void testCheckLengthIsTheSame_SameLength_Success() {
            // Должен пройти без ошибок
        double[] x = {1.0, 2.0, 3.0};
        double[] y = {1.0, 4.0, 9.0};
        assertDoesNotThrow(() -> AbstractTabulatedFunction.checkLengthIsTheSame(x, y));
    }

    @Test
    void testCheckLengthIsTheSame_DifferentLength_ThrowsException() {
        double[] x = {1.0, 2.0};           // длина 2
        double[] y = {1.0, 2.0, 3.0};      // длина 3

        DifferentLengthOfArraysException exception = assertThrows(
                DifferentLengthOfArraysException.class,
                () -> AbstractTabulatedFunction.checkLengthIsTheSame(x, y)
        );
    }





        // ========================
        // Тесты для checkSorted
        // ========================

    @Test
    void testCheckSorted_StrictlyIncreasing_Success() {
        // Строго возрастающий массив — должен пройти
        double[] x = {1.0, 2.0, 3.0, 5.5, 10.0};
        assertDoesNotThrow(() -> AbstractTabulatedFunction.checkSorted(x));
    }

    @Test
    void testCheckSorted_NotStrictlyIncreasing_ThrowsException() {
        // Нарушение строгого возрастания — должно бросить исключение

        // Элемент равен предыдущему
        double[] x1 = {1.0, 2.0, 2.0, 3.0};
        assertThrows(ArrayIsNotSortedException.class,
                () -> AbstractTabulatedFunction.checkSorted(x1));

            // Элемент меньше предыдущего
        double[] x2 = {1.0, 3.0, 2.0, 4.0};
        assertThrows(ArrayIsNotSortedException.class,
                () -> AbstractTabulatedFunction.checkSorted(x2));

            // Все элементы одинаковые
        double[] x3 = {5.0, 5.0, 5.0};
        assertThrows(ArrayIsNotSortedException.class,
                () -> AbstractTabulatedFunction.checkSorted(x3));
    }


    @Test
    void testCheckSorted_NegativeAndPositive_Success() {
        double[] x = {-5.0, -2.0, 0.0, 1.5, 10.0};
        assertDoesNotThrow(() -> AbstractTabulatedFunction.checkSorted(x));
    }
        // 3. InterpolationException
        // ========================

    @Test
    void testInterpolate_OutsideInterval_ArrayImpl() {
        double[] x = {1.0, 3.0, 5.0};
        double[] y = {1.0, 9.0, 25.0};
        ArrayTabulatedFunction af = new ArrayTabulatedFunction(x, y);

        // Интерполяция между 1.0 и 3.0 → floorIndex=0
        // Но x=6.0 — вне [1.0, 3.0]
        assertThrows(InterpolationException.class,
                () -> af.interpolate(6.0, 0));

        // x=0.5 — тоже вне [1.0, 3.0]
        assertThrows(InterpolationException.class,
                () -> af.interpolate(0.5, 0));

        // Между 3.0 и 5.0 → floorIndex=1
        assertThrows(InterpolationException.class,
                () -> af.interpolate(7.0, 1));
    }

    @Test
    void testInterpolate_OutsideInterval_LinkedListImpl() {
        double[] x = {1.0, 3.0, 5.0};
        double[] y = {1.0, 9.0, 25.0};
        LinkedListTabulatedFunction llf = new LinkedListTabulatedFunction(x, y);

        assertThrows(InterpolationException.class,
                () -> llf.interpolate(6.0, 0));
        assertThrows(InterpolationException.class,
                () -> llf.interpolate(0.5, 0));
        assertThrows(InterpolationException.class,
                () -> llf.interpolate(7.0, 1));
    }


    @Test
    void testInterpolate_InsideInterval_Success() {
        double[] x = {1.0, 3.0, 5.0};
        double[] y = {1.0, 9.0, 25.0};
        ArrayTabulatedFunction af = new ArrayTabulatedFunction(x, y);

            // x=2.0 между 1.0 и 3.0 — OK
        assertEquals(5.0, af.interpolate(2.0, 0), 1e-10); // (1->3): (2-1)/(3-1)=0.5 → 1 + 0.5*(9-1)=5.0

            // x=4.0 между 3.0 и 5.0 — OK
        assertEquals(17.0, af.interpolate(4.0, 1), 1e-10); // (3->5): (4-3)/2=0.5 → 9 + 0.5*16=17
    }
}


