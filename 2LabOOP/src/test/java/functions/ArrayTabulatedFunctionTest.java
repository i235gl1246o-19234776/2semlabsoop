package functions;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;


@DisplayName("Тесты для ArrayTabulatedFunction")
public class ArrayTabulatedFunctionTest {

    // Вспомогательный метод для создания простой MathFunction
    private static MathFunction linearFunc(double slope, double intercept) {
        return x -> slope * x + intercept;
    }

    // === Конструктор с массивами ===

    @Test
    @DisplayName("Конструктор с разными длинами массивов выбрасывает исключение")
    void constructorDifferentLengthsThrows() {
        assertThrows(IllegalArgumentException.class, () ->
                new ArrayTabulatedFunction(new double[]{1, 2}, new double[]{3})
        );
    }

    @Test
    @DisplayName("Конструктор с неупорядоченными xVal выбрасывает исключение")
    void constructorUnsortedXThrows() {
        assertThrows(IllegalArgumentException.class, () ->
                new ArrayTabulatedFunction(new double[]{2, 1}, new double[]{4, 5})
        );
    }

    @Test
    @DisplayName("Конструктор с одинаковыми xVal (не строго возрастающими) выбрасывает исключение")
    void constructorDuplicateXThrows() {
        assertThrows(IllegalArgumentException.class, () ->
                new ArrayTabulatedFunction(new double[]{1, 1}, new double[]{2, 3})
        );
    }

    @Test
    @DisplayName("Конструктор с корректными массивами создает объект")
    void constructorValidArraysCreatesObject() {
        double[] x = {1.0, 2.0, 3.0};
        double[] y = {1.0, 4.0, 9.0};
        ArrayTabulatedFunction f = new ArrayTabulatedFunction(x, y);

        assertEquals(3, f.getCount());
        assertEquals(1.0, f.getX(0), 1e-10);
        assertEquals(4.0, f.getY(1), 1e-10);
    }

    // === Конструктор с MathFunction ===

    @Test
    @DisplayName("Конструктор с count < 2 выбрасывает исключение")
    void constructorWithMathFunctionCountTooSmallThrows() {
        assertThrows(IllegalArgumentException.class, () ->
                new ArrayTabulatedFunction(x -> x, 0, 1, 1)
        );
    }

    @Test
    @DisplayName("Конструктор с xFrom == xTo заполняет все значения одинаковыми")
    void constructorEqualBoundsCreatesUniformValues() {
        MathFunction f = x -> x * x;
        ArrayTabulatedFunction tab = new ArrayTabulatedFunction(f, 2.0, 2.0, 5);

        assertEquals(5, tab.getCount());
        for (int i = 0; i < 5; i++) {
            assertEquals(2.0, tab.getX(i), 1e-10);
            assertEquals(4.0, tab.getY(i), 1e-10); // 2^2 = 4
        }
    }

    @Test
    @DisplayName("Конструктор с xFrom > xTo меняет их местами корректно")
    void constructorReversesBoundsIfNecessary() {
        MathFunction f = x -> x;
        ArrayTabulatedFunction tab = new ArrayTabulatedFunction(f, 5.0, 1.0, 5);

        assertEquals(5, tab.getCount());
        assertEquals(1.0, tab.getX(0), 1e-10);
        assertEquals(5.0, tab.getX(4), 1e-10);
        assertEquals(1.0, tab.getY(0), 1e-10);
        assertEquals(5.0, tab.getY(4), 1e-10);
    }

    @Test
    @DisplayName("Конструктор с MathFunction создает правильную дискретизацию")
    void constructorWithMathFunctionCorrectDiscretization() {
        MathFunction f = x -> 2 * x + 1;
        ArrayTabulatedFunction tab = new ArrayTabulatedFunction(f, 0, 4, 5);

        // x: [0, 1, 2, 3, 4]
        // y: [1, 3, 5, 7, 9]
        for (int i = 0; i < 5; i++) {
            assertEquals(i, tab.getX(i), 1e-10);
            assertEquals(2 * i + 1, tab.getY(i), 1e-10);
        }
    }

    // === Методы доступа к данным ===

    @Test
    @DisplayName("getCount возвращает правильное количество точек")
    void getCountReturnsCorrectSize() {
        ArrayTabulatedFunction f = new ArrayTabulatedFunction(
                new double[]{1, 2, 3},
                new double[]{1, 4, 9}
        );
        assertEquals(3, f.getCount());
    }

    @Test
    @DisplayName("getX возвращает значение по индексу")
    void getXReturnsCorrectValue() {
        ArrayTabulatedFunction f = new ArrayTabulatedFunction(
                new double[]{10, 20, 30},
                new double[]{1, 2, 3}
        );
        assertEquals(20, f.getX(1), 1e-10);
    }

    @Test
    @DisplayName("getX с некорректным индексом выбрасывает IndexOutOfBoundsException")
    void getXOutOfBoundsThrows() {
        ArrayTabulatedFunction f = new ArrayTabulatedFunction(
                new double[]{1, 2},
                new double[]{1, 4}
        );
        assertThrows(IndexOutOfBoundsException.class, () -> f.getX(-1));
        assertThrows(IndexOutOfBoundsException.class, () -> f.getX(2));
    }

    @Test
    @DisplayName("getY возвращает значение по индексу")
    void getYReturnsCorrectValue() {
        ArrayTabulatedFunction f = new ArrayTabulatedFunction(
                new double[]{1, 2, 3},
                new double[]{1, 4, 9}
        );
        assertEquals(4, f.getY(1), 1e-10);
    }

    @Test
    @DisplayName("getY с некорректным индексом выбрасывает IndexOutOfBoundsException")
    void getYOutOfBoundsThrows() {
        ArrayTabulatedFunction f = new ArrayTabulatedFunction(
                new double[]{1, 2},
                new double[]{1, 4}
        );
        assertThrows(IndexOutOfBoundsException.class, () -> f.getY(-1));
        assertThrows(IndexOutOfBoundsException.class, () -> f.getY(2));
    }

    @Test
    @DisplayName("setY устанавливает новое значение")
    void setYSetsCorrectValue() {
        ArrayTabulatedFunction f = new ArrayTabulatedFunction(
                new double[]{1, 2, 3},
                new double[]{1, 4, 9}
        );
        f.setY(1, 100);
        assertEquals(100, f.getY(1), 1e-10);
    }

    @Test
    @DisplayName("setY с некорректным индексом выбрасывает IndexOutOfBoundsException")
    void setYOutOfBoundsThrows() {
        ArrayTabulatedFunction f = new ArrayTabulatedFunction(
                new double[]{1, 2},
                new double[]{1, 4}
        );
        assertThrows(IndexOutOfBoundsException.class, () -> f.setY(-1, 5));
        assertThrows(IndexOutOfBoundsException.class, () -> f.setY(2, 5));
    }

    // === indexOfX и indexOfY ===

    @Test
    @DisplayName("indexOfX находит существующий x")
    void indexOfXFindsExistingX() {
        ArrayTabulatedFunction f = new ArrayTabulatedFunction(
                new double[]{1.0, 2.5, 4.0},
                new double[]{1, 6.25, 16}
        );
        assertEquals(1, f.indexOfX(2.5));
        assertEquals(0, f.indexOfX(1.0));
    }

    @Test
    @DisplayName("indexOfX возвращает -1 для отсутствующего x")
    void indexOfXReturnsMinusOneForNotFound() {
        ArrayTabulatedFunction f = new ArrayTabulatedFunction(
                new double[]{1.0, 2.0, 3.0},
                new double[]{1, 4, 9}
        );
        assertEquals(-1, f.indexOfX(1.5));
    }

    @Test
    @DisplayName("indexOfX работает с погрешностью 1e-10")
    void indexOfXWorksWithTolerance() {
        ArrayTabulatedFunction f = new ArrayTabulatedFunction(
                new double[]{1.0, 2.0, 3.0},
                new double[]{1, 4, 9}
        );
        assertEquals(1, f.indexOfX(2.0 + 1e-11)); // внутри допуска
        assertEquals(-1, f.indexOfX(2.0 + 1e-9));  // вне допуска
    }

    @Test
    @DisplayName("indexOfY находит существующий y")
    void indexOfYFindsExistingY() {
        ArrayTabulatedFunction f = new ArrayTabulatedFunction(
                new double[]{1, 2, 3},
                new double[]{1, 4, 9}
        );
        assertEquals(1, f.indexOfY(4));
        assertEquals(2, f.indexOfY(9));
    }

    @Test
    @DisplayName("indexOfY возвращает -1 для отсутствующего y")
    void indexOfYReturnsMinusOneForNotFound() {
        ArrayTabulatedFunction f = new ArrayTabulatedFunction(
                new double[]{1, 2, 3},
                new double[]{1, 4, 9}
        );
        assertEquals(-1, f.indexOfY(5));
    }

    @Test
    @DisplayName("indexOfY работает с погрешностью 1e-10")
    void indexOfYWorksWithTolerance() {
        ArrayTabulatedFunction f = new ArrayTabulatedFunction(
                new double[]{1, 2, 3},
                new double[]{1, 4, 9}
        );
        assertEquals(1, f.indexOfY(4.0 + 1e-11));
        assertEquals(-1, f.indexOfY(4.0 + 1e-9));
    }

    // === leftBound и rightBound ===

    @Test
    @DisplayName("leftBound возвращает минимальный x")
    void leftBoundReturnsFirstX() {
        ArrayTabulatedFunction f = new ArrayTabulatedFunction(
                new double[]{10, 20, 30},
                new double[]{1, 2, 3}
        );
        assertEquals(10, f.leftBound(), 1e-10);
    }

    @Test
    @DisplayName("rightBound возвращает максимальный x")
    void rightBoundReturnsLastX() {
        ArrayTabulatedFunction f = new ArrayTabulatedFunction(
                new double[]{10, 20, 30},
                new double[]{1, 2, 3}
        );
        assertEquals(30, f.rightBound(), 1e-10);
    }

    // === floorIndexOfX ===

    @Test
    @DisplayName("floorIndexOfX возвращает 0 при x <= первому значению")
    void floorIndexOfXUnderLeftBound() {
        ArrayTabulatedFunction f = new ArrayTabulatedFunction(
                new double[]{1, 2, 3},
                new double[]{1, 4, 9}
        );
        assertEquals(0, f.floorIndexOfX(0.5));
        assertEquals(0, f.floorIndexOfX(1.0));
    }

    @Test
    @DisplayName("floorIndexOfX возвращает последний индекс при x >= последнему значению")
    void floorIndexOfXOverRightBound() {
        ArrayTabulatedFunction f = new ArrayTabulatedFunction(
                new double[]{1, 2, 3},
                new double[]{1, 4, 9}
        );
        assertEquals(2, f.floorIndexOfX(3.0));
        assertEquals(2, f.floorIndexOfX(4.0));
    }

    @Test
    @DisplayName("floorIndexOfX возвращает корректный индекс внутри диапазона")
    void floorIndexOfXInMiddle() {
        ArrayTabulatedFunction f = new ArrayTabulatedFunction(
                new double[]{1, 2, 3, 4},
                new double[]{1, 4, 9, 16}
        );
        assertEquals(0, f.floorIndexOfX(1.5));
        assertEquals(1, f.floorIndexOfX(2.3));
        assertEquals(2, f.floorIndexOfX(3.9));
    }

    @Test
    @DisplayName("floorIndexOfX с двумя точками")
    void floorIndexOfXWithTwoPoints() {
        ArrayTabulatedFunction f = new ArrayTabulatedFunction(
                new double[]{0, 1},
                new double[]{0, 1}
        );
        assertEquals(0, f.floorIndexOfX(0.5));
        assertEquals(0, f.floorIndexOfX(0.0));
        assertEquals(1, f.floorIndexOfX(1.0));
        assertEquals(1, f.floorIndexOfX(2.0));
    }

    // === interpolate ===

    @Test
    @DisplayName("interpolate с корректным floorIndex возвращает линейно интерполированное значение")
    void interpolateCorrectly() {
        ArrayTabulatedFunction f = new ArrayTabulatedFunction(
                new double[]{0, 2},
                new double[]{0, 4}
        );
        // Линейно: y = 2x
        assertEquals(2, f.interpolate(1, 0), 1e-10); // между 0 и 2
    }

    @Test
    @DisplayName("interpolate с floorIndex = 0 при двух точках")
    void interpolateWithFloorZero() {
        ArrayTabulatedFunction f = new ArrayTabulatedFunction(
                new double[]{1, 3},
                new double[]{1, 5}
        );
        assertEquals(3, f.interpolate(2, 0), 1e-10); // середина между 1 и 3 → y=3
    }

    @Test
    @DisplayName("interpolate с некорректным floorIndex выбрасывает IndexOutOfBoundsException")
    void interpolateInvalidFloorIndexThrows() {
        ArrayTabulatedFunction f = new ArrayTabulatedFunction(
                new double[]{1, 2, 3},
                new double[]{1, 4, 9}
        );
        assertThrows(IndexOutOfBoundsException.class, () -> f.interpolate(1.5, -1));
        assertThrows(IndexOutOfBoundsException.class, () -> f.interpolate(1.5, 2));
    }

    @Test
    @DisplayName("interpolate с одной точкой возвращает её y")
    void interpolateSinglePoint() {
        ArrayTabulatedFunction f = new ArrayTabulatedFunction(
                new double[]{1},
                new double[]{5}
        );
        assertEquals(5, f.interpolate(10, 0), 1e-10); // даже если floorIndex не подходит — возвращает y[0]
    }

    // === extrapolateLeft и extrapolateRight ===

    @Test
    @DisplayName("extrapolateLeft с одной точкой возвращает её y")
    void extrapolateLeftSinglePoint() {
        ArrayTabulatedFunction f = new ArrayTabulatedFunction(
                new double[]{1},
                new double[]{5}
        );
        assertEquals(5, f.extrapolateLeft(0), 1e-10);
        assertEquals(5, f.extrapolateLeft(10), 1e-10);
    }

    @Test
    @DisplayName("extrapolateLeft с несколькими точками использует первый отрезок")
    void extrapolateLeftUsesFirstSegment() {
        ArrayTabulatedFunction f = new ArrayTabulatedFunction(
                new double[]{1, 2},
                new double[]{1, 3}
        );
        // Линия: y = 2x - 1
        assertEquals(-1, f.extrapolateLeft(0), 1e-10); // 2*0 - 1 = -1
    }

    @Test
    @DisplayName("extrapolateRight с одной точкой возвращает её y")
    void extrapolateRightSinglePoint() {
        ArrayTabulatedFunction f = new ArrayTabulatedFunction(
                new double[]{1},
                new double[]{5}
        );
        assertEquals(5, f.extrapolateRight(0), 1e-10);
        assertEquals(5, f.extrapolateRight(10), 1e-10);
    }

    @Test
    @DisplayName("extrapolateRight с несколькими точками использует последний отрезок")
    void extrapolateRightUsesLastSegment() {
        ArrayTabulatedFunction f = new ArrayTabulatedFunction(
                new double[]{1, 2},
                new double[]{1, 3}
        );
        // Линия: y = 2x - 1
        assertEquals(5, f.extrapolateRight(3), 1e-10); // 2*3 - 1 = 5
    }

    // === andThen ===

    @Test
    @DisplayName("andThen применяет функцию после табулированной")
    void andThenAppliesAfterFunction() {
        ArrayTabulatedFunction f = new ArrayTabulatedFunction(
                new double[]{0, 1, 2},
                new double[]{0, 1, 2}
        );

        MathFunction after = x -> x * 2;
        MathFunction composed = f.andThen(after);

        // composed(0) = after(f(0)) = after(0) = 0
        // composed(1) = after(f(1)) = after(1) = 2
        // Проверяем через вызов apply
        assertEquals(0, composed.apply(0), 1e-10);
        assertEquals(2, composed.apply(1), 1e-10);
    }

    // === Дополнительные тесты: сложные случаи ===

    @Test
    @DisplayName("Интерполяция с большим количеством точек")
    void interpolateWithManyPoints() {
        double[] x = {0, 1, 2, 3, 4};
        double[] y = {0, 2, 4, 6, 8}; // y = 2x
        ArrayTabulatedFunction f = new ArrayTabulatedFunction(x, y);

        // Интерполируем между 1 и 2: x=1.5 → y ≈ 2.25
        assertEquals(3, f.interpolate(1.5, 1), 1e-10);
    }

    @Test
    @DisplayName("Экстраполяция слева с более чем 2 точками")
    void extrapolateLeftWithManyPoints() {
        ArrayTabulatedFunction f = new ArrayTabulatedFunction(
                new double[]{1, 2, 3},
                new double[]{1, 4, 9}
        );
        // Линейная экстраполяция по первым двум точкам: y = 3x - 2
        assertEquals(1, f.extrapolateLeft(1), 1e-10); // граница
        assertEquals(-2, f.extrapolateLeft(0), 1e-10); // 3*0 - 2 = -2
    }

    @Test
    @DisplayName("Экстраполяция справа с более чем 2 точками")
    void extrapolateRightWithManyPoints() {
        ArrayTabulatedFunction f = new ArrayTabulatedFunction(
                new double[]{1, 2, 3},
                new double[]{1, 4, 9}
        );
        // Последний отрезок: от (2,4) до (3,9) → наклон = 5 → y = 5x - 6
        assertEquals(9, f.extrapolateRight(3), 1e-10);
        assertEquals(14, f.extrapolateRight(4), 1e-10); // 5*4 - 6 = 14
    }

    // === Тест на идемпотентность и неизменность входных данных ===

    @Test
    @DisplayName("Конструктор не модифицирует входные массивы")
    void constructorDoesNotModifyInputArrays() {
        double[] originalX = {1, 2, 3};
        double[] originalY = {1, 4, 9};
        double[] copyX = Arrays.copyOf(originalX, originalX.length);
        double[] copyY = Arrays.copyOf(originalY, originalY.length);

        new ArrayTabulatedFunction(originalX, originalY);

        // Убеждаемся, что оригинальные массивы остались неизменными
        assertArrayEquals(copyX, originalX, 1e-10);
        assertArrayEquals(copyY, originalY, 1e-10);
    }

    // === Тест на инициализацию при равных границах с count=2 ===
    @Test
    @DisplayName("Конструктор с xFrom == xTo и count=2 создаёт две одинаковые точки")
    void constructorEqualBoundsCountTwo() {
        MathFunction f = x -> x * x;
        ArrayTabulatedFunction tab = new ArrayTabulatedFunction(f, 2.0, 2.0, 2);

        assertEquals(2, tab.getCount());
        assertEquals(2.0, tab.getX(0), 1e-10);
        assertEquals(2.0, tab.getX(1), 1e-10);
        assertEquals(4.0, tab.getY(0), 1e-10);
        assertEquals(4.0, tab.getY(1), 1e-10);
    }

    @Test
    void testArrayInsertAtBeginning() {
        double[] xValues = {2.0, 3.0, 4.0};
        double[] yValues = {20.0, 30.0, 40.0};
        ArrayTabulatedFunction function = new ArrayTabulatedFunction(xValues, yValues);

        // Вставка в начало
        function.insert(1.0, 10.0);

        assertEquals(4, function.getCount(), "Количество точек должно увеличиться");
        assertEquals(1.0, function.getX(0), 1e-10, "Новая точка в начале");
        assertEquals(10.0, function.getY(0), 1e-10, "Значение новой точки");
        assertEquals(2.0, function.getX(1), 1e-10, "Следующая точка сдвинута");
    }

    @Test
    void testArrayInsertAtEnd() {
        double[] xValues = {1.0, 2.0, 3.0};
        double[] yValues = {10.0, 20.0, 30.0};
        ArrayTabulatedFunction function = new ArrayTabulatedFunction(xValues, yValues);

        // Вставка в конец
        function.insert(4.0, 40.0);

        assertEquals(4, function.getCount(), "Количество точек должно увеличиться");
        assertEquals(4.0, function.getX(3), 1e-10, "Новая точка в конце");
        assertEquals(40.0, function.getY(3), 1e-10, "Значение новой точки");
    }

    @Test
    void testArrayInsertInMiddle() {
        double[] xValues = {1.0, 3.0, 4.0};
        double[] yValues = {10.0, 30.0, 40.0};
        ArrayTabulatedFunction function = new ArrayTabulatedFunction(xValues, yValues);

        // Вставка в середину
        function.insert(2.0, 20.0);

        assertEquals(4, function.getCount(), "Количество точек должно увеличиться");
        assertEquals(2.0, function.getX(1), 1e-10, "Новая точка в середине");
        assertEquals(20.0, function.getY(1), 1e-10, "Значение новой точки");
        assertEquals(3.0, function.getX(2), 1e-10, "Следующая точка сдвинута");
    }

    @Test
    void testArrayUpdateExisting() {
        double[] xValues = {1.0, 2.0, 3.0};
        double[] yValues = {10.0, 20.0, 30.0};
        ArrayTabulatedFunction function = new ArrayTabulatedFunction(xValues, yValues);

        // Обновление существующего значения
        function.insert(2.0, 25.0);

        assertEquals(3, function.getCount(), "Количество точек не должно измениться");
        assertEquals(2.0, function.getX(1), 1e-10, "Точка должна остаться на месте");
        assertEquals(25.0, function.getY(1), 1e-10, "Значение должно обновиться");
    }
}