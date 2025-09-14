package functions;

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

    @Test
    public void testConstructor_AllowsEqualBounds() { // ❗️ЗАМЕЧАНИЕ: по условию x0 >= x1 — запрещено
        // Но в условии написано: "if(x0 >= x1)" — значит, равенство тоже запрещено.
        // Поэтому тест выше уже покрывает x0 == x1.
    }

    @Test
    void testArrayWithArray(){
        //f(x) = x + 1
        double[] x1 = {0.0, 1.0, 2.0, 3.0, 4.0};
        double[] y1 = {1.0, 2.0, 3.0, 4.0, 5.0};
        ArrayTabulatedFunction f = new ArrayTabulatedFunction(x1, y1);

        //g(x) = 2x + 3
        double[] x2 = {0.0, 1.0, 2.0, 3.0, 4.0};
        double[] y2 = {3.0, 5.0, 7.0, 9.0, 11.0};
        ArrayTabulatedFunction g = new ArrayTabulatedFunction(x2, y2);

        CompositeFunction composition = new CompositeFunction(g, f);
        // f(g(x)) = (2x + 3) + 1 = 2x + 4
        assertEquals(4.0, composition.apply(0.0), 1e-8, "f(g(0)) = f(3) = 4");
        assertEquals(6.0, composition.apply(1.0), 1e-8, "f(g(1)) = f(5) = 6");
        assertEquals(8.0, composition.apply(2.0), 1e-8, "f(g(2)) = f(7) = 8");
        assertEquals(10.0, composition.apply(3.0), 1e-8, "f(g(3)) = f(9) = 10");

        assertEquals(4.5, composition.apply(0.25), 1e-8, "f(g(0.5)) интерполяция");
        assertEquals(5.0, composition.apply(0.5), 1e-8, "f(g(3.5)) интерполяций");
        assertEquals(5.5, composition.apply(0.75), 1e-8, "f(g(3.5)) интерполяций");
        assertEquals(6.5, composition.apply(1.25), 1e-8, "f(g(3.5)) интерполяций");
        assertEquals(7.0, composition.apply(1.5), 1e-8, "f(g(3.5)) интерполяций");
    }

    @Test
    void testExponentialWithLogarithmicComposition() {
        //f(x) = x + 1
        double[] x1 = {0.0, 0.5, 1.0, 1.5, 2.0};
        double[] y1 = {1.0, 1.5, 2.0, 2.5, 3.0};
        ArrayTabulatedFunction f = new ArrayTabulatedFunction(x1, y1);

        //g(x) = ln(x + 1)
        double[] x2 = {0.0, 1.0, 2.0, 3.0, 4.0};
        double[] y2 = {0.0, 0.6931, 1.0986, 1.3863, 1.6094};
        ArrayTabulatedFunction g = new ArrayTabulatedFunction(x2, y2);

        //h(x) = f(g(x)) = ln(x + 1) + 1
        CompositeFunction composition = new CompositeFunction(g, f);

        assertEquals(1.0, composition.apply(0.0), 0.1, "f(g(0)) = f(0) = 1");
        assertEquals(1.6931, composition.apply(1.0), 0.1, "f(g(1)) = f(ln2) ≈ 2");
        assertEquals(2.0986, composition.apply(2.0), 0.1, "f(g(2)) = f(ln3) ≈ 3");
        assertEquals(2.3863, composition.apply(3.0), 0.1, "f(g(3)) = f(ln4) ≈ 4");
        assertEquals(2.6094, composition.apply(4.0), 0.1, "f(g(4)) = f(ln5) ≈ 5");
    }

    @Test
    void testTrigonometricComposition() {
        //f(x) = sin(x)
        double[] x1 = {0.0, java.lang.Math.PI/6, java.lang.Math.PI/4, java.lang.Math.PI/3, java.lang.Math.PI/2};
        double[] y1 = {0.0, 0.5, 0.7071, 0.8660, 1.0};
        ArrayTabulatedFunction f = new ArrayTabulatedFunction(x1, y1);

        //g(x) = cos(x)
        double[] x2 = {0.0, java.lang.Math.PI/6, java.lang.Math.PI/4, java.lang.Math.PI/3, java.lang.Math.PI/2};
        double[] y2 = {1.0, 0.8660, 0.7071, 0.5, 0.0};
        ArrayTabulatedFunction g = new ArrayTabulatedFunction(x2, y2);

        //h(x) = f(g(x)) = sin(cos(x))
        CompositeFunction composition = new CompositeFunction(g, f);

        assertEquals(java.lang.Math.sin(1.0), composition.apply(0.0), 0.1, "sin(cos(0)) = sin(1)");
        assertEquals(java.lang.Math.sin(0.8660), composition.apply(Math.PI/6), 0.1, "sin(cos(π/6))");
        assertEquals(java.lang.Math.sin(0.7071), composition.apply(Math.PI/4), 0.1, "sin(cos(π/4))");
        assertEquals(java.lang.Math.sin(0.5), composition.apply(Math.PI/3), 0.1, "sin(cos(π/3))");
        assertEquals(java.lang.Math.sin(0.0), composition.apply(Math.PI/2), 0.1, "sin(cos(π/2)) = sin(0)");
    }

    @Test
    void testCompositeWithExtrapolation() {
        //f(x) = √x
        double[] x1 = {0.0, 1.0, 2.0, 3.0, 4.0};
        double[] y1 = {0.0, 1.0, 1.4142, 1.7321, 2.0};
        ArrayTabulatedFunction f = new ArrayTabulatedFunction(x1, y1);

        //g(x) = x - 3
        double[] x2 = {0.0, 1.0, 2.0, 3.0, 4.0, 5.0, 6.0};
        double[] y2 = {-3.0, -2.0, -1.0, 0.0, 1.0, 2.0, 3.0};
        ArrayTabulatedFunction g = new ArrayTabulatedFunction(x2, y2);

        //h(x) = f(g(x)) = f(x - 3) = √(x - 3)
        CompositeFunction composition = new CompositeFunction(g, f);
        //Экстраполяция влево (x - 3 < 0)
        assertTrue(composition.apply(0.0) < 0, "f(g(0)) = f(-3) экстраполяция влево");
        assertTrue(composition.apply(1.0) < 0, "f(g(1)) = f(-2) экстраполяция влево");
        assertTrue(composition.apply(2.0) < 0, "f(g(2)) = f(-1) экстраполяция влево");

        //Граничные значения
        assertEquals(0.0, composition.apply(3.0), 1e-8, "f(g(3)) = f(0) = 0");
        assertEquals(1.0, composition.apply(4.0), 1e-8, "f(g(4)) = f(1) = 1");
        assertEquals(1.4142, composition.apply(5.0), 0.1, "f(g(5)) = f(2) = √2");
        assertEquals(1.7321, composition.apply(6.0), 0.1, "f(g(6)) = f(3) = √3");

        //Экстраполяция вправо (x - 3 > 4)
        assertEquals(2.236, composition.apply(8.0), 0.1, "f(g(8)) = f(5) экстраполяция вправо");
    }
}