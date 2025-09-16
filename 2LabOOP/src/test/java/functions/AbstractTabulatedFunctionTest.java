package functions;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class AbstractTabulatedFunctionTest {

    private static final double DELTA = 1e-10;


    @Test
    @DisplayName("floorIndexOfX при x < leftBound должен возвращать 0 — ближайшая слева точка")
    public void testFloorIndexOfX_BelowLeftBound() {
        MockTabulatedFunction f = new MockTabulatedFunction(0.0, 2.0, 1.0, 3.0);
        assertEquals(0, f.floorIndexOfX(-1.0), "x = -1.0 < x0=0.0 → floorIndex = 0 GOOD");
        assertEquals(0, f.floorIndexOfX(0.0 - DELTA * 2), "x чуть ниже x0 → floorIndex = 0 GOOD");
    }

    @Test
    @DisplayName("floorIndexOfX при x == leftBound должен возвращать 0 — первая точка точно совпадает ")
    public void testFloorIndexOfX_AtLeftBound() {
        MockTabulatedFunction f = new MockTabulatedFunction(0.0, 2.0, 1.0, 3.0);
        assertEquals(0, f.floorIndexOfX(0.0), "x = x0 = 0.0 → floorIndex = 0 GOOD");
    }

    @Test
    @DisplayName("floorIndexOfX при x между x0 и x1 должен возвращать 0 — интервал [x0, x1)")
    public void testFloorIndexOfX_BetweenBounds() {
        MockTabulatedFunction f = new MockTabulatedFunction(0.0, 2.0, 1.0, 3.0);
        assertEquals(0, f.floorIndexOfX(0.5), "x = 0.5 ∈ [x0, x1) → floorIndex = 0 GOOD");
        assertEquals(0, f.floorIndexOfX(1.999), "x = 1.999 < x1 → floorIndex = 0 GOOD");
    }

    @Test
    @DisplayName("floorIndexOfX при x == rightBound должен возвращать count-1 = 1 — последняя точка")
    public void testFloorIndexOfX_AtRightBound() {
        MockTabulatedFunction f = new MockTabulatedFunction(0.0, 2.0, 1.0, 3.0);
        assertEquals(1, f.floorIndexOfX(2.0), "x = x1 = 2.0 → floorIndex = 1 (count-1) GOOD");
    }

    @Test
    @DisplayName("floorIndexOfX при x > rightBound должен возвращать count = 2 — за пределами диапазона")
    public void testFloorIndexOfX_AboveRightBound() {
        MockTabulatedFunction f = new MockTabulatedFunction(0.0, 2.0, 1.0, 3.0);
        assertEquals(2, f.floorIndexOfX(2.1), "x = 2.1 > x1 → floorIndex = count = 2 GOOD");
        assertEquals(2, f.floorIndexOfX(100.0), "x = 100.0 >> x1 → floorIndex = count = 2 GOOD");
    }

    @Test
    @DisplayName("interpolate должен корректно выполнять линейную интерполяцию в середине отрезка")
    public void testInterpolateWithExplicitBounds() {
        MockTabulatedFunction function = new MockTabulatedFunction(0.0, 2.0, 1.0, 3.0);

        // Интерполяция в середине: (x=1.0) → y = 1.0 + (3.0-1.0)/(2.0-0.0)*(1.0-0.0) = 2.0
        double result = function.interpolate(1.0, 0.0, 2.0, 1.0, 3.0);
        assertEquals(2.0, result, DELTA, "Интерполяция в середине (x=1.0): y=2.0 GOOD");

        // Ближе к левой границе: x=0.5 → y = 1.0 + 1.0*(0.5/2.0) = 1.5
        result = function.interpolate(0.5, 0.0, 2.0, 1.0, 3.0);
        assertEquals(1.5, result, DELTA, "Интерполяция ближе к левой границе (x=0.5): y=1.5 GOOD");

        // Ближе к правой границе: x=1.5 → y = 1.0 + 1.0*(1.5/2.0) = 2.5
        result = function.interpolate(1.5, 0.0, 2.0, 1.0, 3.0);
        assertEquals(2.5, result, DELTA, "Интерполяция ближе к правой границе (x=1.5): y=2.5 GOOD");

        // Тест с отрицательными значениями: x=0.0 между x0=-2.0 и x1=2.0, y0=-4.0, y1=4.0 → y=0.0
        MockTabulatedFunction negativeFunction = new MockTabulatedFunction(-2.0, 2.0, -4.0, 4.0);
        result = negativeFunction.interpolate(0.0, -2.0, 2.0, -4.0, 4.0);
        assertEquals(0.0, result, DELTA, "Интерполяция с отрицательными значениями: y=0.0 GOOD");
    }


    @Test
    @DisplayName("apply должен возвращать точное значение y при совпадении x с x0 или x1")
    public void testApplyWithExactMatch() {
        MockTabulatedFunction function = new MockTabulatedFunction(1.0, 3.0, 2.0, 6.0);
        assertEquals(2.0, function.apply(1.0), DELTA, "apply(x0=1.0) → y0=2.0 GOOD");
        assertEquals(6.0, function.apply(3.0), DELTA, "apply(x1=3.0) → y1=6.0 GOOD");
    }

    @Test
    @DisplayName("apply должен выполнять линейную интерполяцию между x0 и x1 при x внутри диапазона")
    public void testApplyWithInterpolation() {
        MockTabulatedFunction function = new MockTabulatedFunction(0.0, 4.0, 0.0, 8.0);
        assertEquals(2.0, function.apply(1.0), DELTA, "apply(x=1.0): интерполяция → y=2.0 GOOD");
        assertEquals(4.0, function.apply(2.0), DELTA, "apply(x=2.0): интерполяция → y=4.0 GOOD");
        assertEquals(6.0, function.apply(3.0), DELTA, "apply(x=3.0): интерполяция → y=6.0 GOOD");
    }

    @Test
    @DisplayName("apply должен выполнять экстраполяцию влево по линии, проходящей через x0 и x1")
    public void testApplyWithExtrapolationLeft() {
        MockTabulatedFunction function = new MockTabulatedFunction(2.0, 4.0, 4.0, 8.0);
        // Уравнение: y = 2x (т.к. 4.0 = 2*2, 8.0 = 2*4)
        assertEquals(2.0, function.apply(1.0), DELTA, "extrapolateLeft(x=1.0): y=2*1=2.0 GOOD");
        assertEquals(0.0, function.apply(0.0), DELTA, "extrapolateLeft(x=0.0): y=2*0=0.0 GOOD");
        assertEquals(-4.0, function.apply(-2.0), DELTA, "extrapolateLeft(x=-2.0): y=2*(-2)=-4.0 GOOD");
    }

    @Test
    @DisplayName("apply должен выполнять экстраполяцию вправо по линии, проходящей через x0 и x1")
    public void testApplyWithExtrapolationRight() {
        MockTabulatedFunction function = new MockTabulatedFunction(2.0, 4.0, 4.0, 8.0);
        // Уравнение: y = 2x
        assertEquals(10.0, function.apply(5.0), DELTA, "extrapolateRight(x=5.0): y=2*5=10.0 GOOD");
        assertEquals(12.0, function.apply(6.0), DELTA, "extrapolateRight(x=6.0): y=2*6=12.0 GOOD");
        assertEquals(16.0, function.apply(8.0), DELTA, "extrapolateRight(x=8.0): y=2*8=16.0 GOOD");
    }

    @Test
    @DisplayName("apply должен корректно работать с константной функцией — возвращает y0 всегда")
    public void testApplyWithConstantFunction() {
        MockTabulatedFunction function = new MockTabulatedFunction(0.0, 10.0, 5.0, 5.0);
        assertEquals(5.0, function.apply(-5.0), DELTA, "const function: extrapolateLeft → y=5.0 GOOD");
        assertEquals(5.0, function.apply(2.5), DELTA, "const function: interpolation → y=5.0 GOOD");
        assertEquals(5.0, function.apply(15.0), DELTA, "const function: extrapolateRight → y=5.0 GOOD");
    }


    @Test
    @DisplayName("getCount должен возвращать 2 для двухточечной функции — всегда фиксировано")
    public void testGetCount() {
        MockTabulatedFunction f = new MockTabulatedFunction(0, 1, 0, 1);
        assertEquals(2, f.getCount(), "Две точки → getCount() = 2 GOOD");
    }


    @Test
    @DisplayName("getX(0) должен возвращать x0 — первую координату")
    public void testGetX_Index0() {
        MockTabulatedFunction f = new MockTabulatedFunction(10.0, 20.0, 100.0, 200.0);
        assertEquals(10.0, f.getX(0), DELTA, "getX(0) → x0 = 10.0 GOOD");
    }

    @Test
    @DisplayName("getX(1) должен возвращать x1 — вторую координату")
    public void testGetX_Index1() {
        MockTabulatedFunction f = new MockTabulatedFunction(10.0, 20.0, 100.0, 200.0);
        assertEquals(20.0, f.getX(1), DELTA, "getX(1) → x1 = 20.0 GOOD");
    }

    @Test
    @DisplayName("getX с индексом >= 2 должен выбрасывать IllegalArgumentException — индекс вне диапазона")
    public void testGetX_IndexOutOfBounds() {
        MockTabulatedFunction f = new MockTabulatedFunction(0, 1, 0, 1);
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> f.getX(2));
        assertEquals("Index out of bounds: 2", thrown.getMessage(), "Ошибка при выходе за границы: 'Index out of bounds: 2' GOOD");
    }

    @Test
    @DisplayName("getY(0) должен возвращать y0 — первую функциональную величину")
    public void testGetY_Index0() {
        MockTabulatedFunction f = new MockTabulatedFunction(10.0, 20.0, 100.0, 200.0);
        assertEquals(100.0, f.getY(0), DELTA, "getY(0) → y0 = 100.0 GOOD");
    }

    @Test
    @DisplayName("getY(1) должен возвращать y1 — вторую функциональную величину")
    public void testGetY_Index1() {
        MockTabulatedFunction f = new MockTabulatedFunction(10.0, 20.0, 100.0, 200.0);
        assertEquals(200.0, f.getY(1), DELTA, "getY(1) → y1 = 200.0 GOOD");
    }

    @Test
    @DisplayName("getY с индексом >= 2 должен выбрасывать IllegalArgumentException — индекс вне диапазона")
    public void testGetY_IndexOutOfBounds() {
        MockTabulatedFunction f = new MockTabulatedFunction(0, 1, 0, 1);
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> f.getY(2));
        assertEquals("Index out of bounds: 2", thrown.getMessage(), "Ошибка при выходе за границы: 'Index out of bounds: 2' GOOD");
    }


    @Test
    @DisplayName("setY должен выбрасывать UnsupportedOperationException — Mock-функция неизменяема")
    public void testSetY_ThrowsUnsupportedOperationException() {
        MockTabulatedFunction f = new MockTabulatedFunction(0, 1, 0, 1);
        UnsupportedOperationException thrown = assertThrows(UnsupportedOperationException.class, () -> f.setY(0, 5.0));
        assertEquals("Mock object is immutable", thrown.getMessage(), "Ожидаемое сообщение: 'Mock object is immutable' GOOD");
    }


    @Test
    @DisplayName("indexOfX должен находить индекс 0 при точном совпадении с x0")
    public void testIndexOfX_ExactMatch_x0() {
        MockTabulatedFunction f = new MockTabulatedFunction(1.0, 3.0, 2.0, 6.0);
        assertEquals(0, f.indexOfX(1.0), "indexOfX(x0=1.0) → index=0 GOOD");
    }

    @Test
    @DisplayName("indexOfX должен находить индекс 1 при точном совпадении с x1")
    public void testIndexOfX_ExactMatch_x1() {
        MockTabulatedFunction f = new MockTabulatedFunction(1.0, 3.0, 2.0, 6.0);
        assertEquals(1, f.indexOfX(3.0), "indexOfX(x1=3.0) → index=1 GOOD");
    }

    @Test
    @DisplayName("indexOfX должен находить x0 при значении в допуске 1e-10 от x0")
    public void testIndexOfX_CloseTo_x0() {
        MockTabulatedFunction f = new MockTabulatedFunction(1.0, 3.0, 2.0, 6.0);
        assertEquals(0, f.indexOfX(1.0 + DELTA * 0.9999), "x = x0 + 0.9999*δ → совпадение GOOD");
        assertEquals(0, f.indexOfX(1.0 - DELTA * 0.9999), "x = x0 - 0.9999*δ → совпадение GOOD");
    }

    @Test
    @DisplayName("indexOfX должен находить x1 при значении в допуске 1e-10 от x1")
    public void testIndexOfX_CloseTo_x1() {
        MockTabulatedFunction f = new MockTabulatedFunction(1.0, 3.0, 2.0, 6.0);
        assertEquals(1, f.indexOfX(3.0 + DELTA * 0.9999), "x = x1 + 0.9999*δ → совпадение GOOD");
        assertEquals(1, f.indexOfX(3.0 - DELTA * 0.9999), "x = x1 - 0.9999*δ → совпадение GOOD");
    }

    @Test
    @DisplayName("indexOfX должен возвращать -1, если x не совпадает ни с x0, ни с x1")
    public void testIndexOfX_NotFound() {
        MockTabulatedFunction f = new MockTabulatedFunction(1.0, 3.0, 2.0, 6.0);
        assertEquals(-1, f.indexOfX(2.0), "x=2.0 отсутствует → -1 GOOD");
        assertEquals(-1, f.indexOfX(0.0), "x=0.0 отсутствует → -1 GOOD");
        assertEquals(-1, f.indexOfX(4.0), "x=4.0 отсутствует → -1 GOOD");
    }


    @Test
    @DisplayName("indexOfY должен находить индекс 0 при точном совпадении с y0 ")
    public void testIndexOfY_ExactMatch_y0() {
        MockTabulatedFunction f = new MockTabulatedFunction(1.0, 3.0, 2.0, 6.0);
        assertEquals(0, f.indexOfY(2.0), "indexOfY(y0=2.0) → index=0 GOOD");
    }

    @Test
    @DisplayName("indexOfY должен находить индекс 1 при точном совпадении с y1")
    public void testIndexOfY_ExactMatch_y1() {
        MockTabulatedFunction f = new MockTabulatedFunction(1.0, 3.0, 2.0, 6.0);
        assertEquals(1, f.indexOfY(6.0), "indexOfY(y1=6.0) → index=1 GOOD");
    }

    @Test
    @DisplayName("indexOfY должен находить y0 при значении в допуске 1e-10 от y0")
    public void testIndexOfY_CloseTo_y0() {
        MockTabulatedFunction f = new MockTabulatedFunction(1.0, 3.0, 2.0, 6.0);
        assertEquals(0, f.indexOfY(2.0 + DELTA * 0.9999), "y = y0 + 0.9999*δ → совпадение GOOD");
        assertEquals(0, f.indexOfY(2.0 - DELTA * 0.9999), "y = y0 - 0.9999*δ → совпадение GOOD");
    }

    @Test
    @DisplayName("indexOfY должен находить y1 при значении в допуске 1e-10 от y1")
    public void testIndexOfY_CloseTo_y1() {
        MockTabulatedFunction f = new MockTabulatedFunction(1.0, 3.0, 2.0, 6.0);
        assertEquals(1, f.indexOfY(6.0 + DELTA * 0.9999), "y = y1 + 0.9999*δ → совпадение GOOD");
        assertEquals(1, f.indexOfY(6.0 - DELTA * 0.9999), "y = y1 - 0.9999*δ → совпадение GOOD");
    }

    @Test
    @DisplayName("indexOfY должен возвращать -1, если y не совпадает ни с y0, ни с y1")
    public void testIndexOfY_NotFound() {
        MockTabulatedFunction f = new MockTabulatedFunction(1.0, 3.0, 2.0, 6.0);
        assertEquals(-1, f.indexOfY(3.0), "y=3.0 отсутствует → -1 GOOD");
        assertEquals(-1, f.indexOfY(1.0), "y=1.0 отсутствует → -1 GOOD");
        assertEquals(-1, f.indexOfY(7.0), "y=7.0 отсутствует → -1 GOOD");
    }


    @Test
    @DisplayName("leftBound должен возвращать минимальное значение x (x0)")
    public void testLeftBound() {
        MockTabulatedFunction f = new MockTabulatedFunction(-5.0, 10.0, 0, 0);
        assertEquals(-5.0, f.leftBound(), DELTA, "leftBound = x0 = -5.0 GOOD");
    }

    @Test
    @DisplayName("rightBound должен возвращать максимальное значение x (x1)")
    public void testRightBound() {
        MockTabulatedFunction f = new MockTabulatedFunction(-5.0, 10.0, 0, 0);
        assertEquals(10.0, f.rightBound(), DELTA, "rightBound = x1 = 10.0 GOOD");
    }

    @Test
    @DisplayName("Конструктор должен выбрасывать IllegalArgumentException, если x0 > x1 — недопустимый диапазон")
    public void testConstructor_ThrowsOnInvalidRange() {
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
                () -> new MockTabulatedFunction(2.0, 1.0, 0, 0));
        assertEquals("x0 > x1, NOT GOOD", thrown.getMessage(), "Ошибка при x0 > x1: 'x0 > x1' ");
    }
}