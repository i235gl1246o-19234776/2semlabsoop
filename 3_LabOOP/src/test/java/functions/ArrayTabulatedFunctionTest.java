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

    // Вспомогательный метод для создания тестового объекта
    private ArrayTabulatedFunction createTestFunction() {
        double[] x = {1.0, 2.0, 3.0, 4.0, 5.0};
        double[] y = {1.0, 4.0, 6.0, 8.0, 10.0};
        return new ArrayTabulatedFunction(x, y);
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
    @DisplayName("getY возвращает значение по индексу")
    void getYReturnsCorrectValue() {
        ArrayTabulatedFunction f = new ArrayTabulatedFunction(
                new double[]{1, 2, 3},
                new double[]{1, 4, 9}
        );
        assertEquals(4, f.getY(1), 1e-10);
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


    @Test
    @DisplayName("floorIndexOfX возвращает последний индекс при x >= последнему значению")
    void floorIndexOfXOverRightBound() {
        ArrayTabulatedFunction f = new ArrayTabulatedFunction(
                new double[]{1, 2, 3},
                new double[]{1, 4, 9}
        );
        assertEquals(2, f.floorIndexOfX(3.0));
        //assertEquals(3, f.floorIndexOfX(4.0)); //все меньше данного - count = 3
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

        assertEquals(0, composed.apply(0), 1e-10);
        assertEquals(2, composed.apply(1), 1e-10);
    }

    @Test
    @DisplayName("andThen не изменяет оригинал")
    void andThenDoesNotModifyOriginal() {
        ArrayTabulatedFunction f = createTestFunction();
        MathFunction after = x -> x * 2;
        MathFunction composed = f.andThen(after);

        // Проверяем, что f не изменился
        assertEquals(5, f.getCount());
        assertEquals(1.0, f.getX(0), 1e-10);
        assertEquals(10.0, f.getY(4), 1e-10);
    }

    // === getxVal и getyVal ===

    @Test
    @DisplayName("getxVal возвращает копию массива x с правильной длиной")
    void getxValReturnsCopyAndCorrectLength() {
        ArrayTabulatedFunction f = createTestFunction();
        double[] xCopy = f.getxVal();

        assertArrayEquals(new double[]{1.0, 2.0, 3.0, 4.0, 5.0}, xCopy, 1e-10);
        assertEquals(5, xCopy.length);

        // Изменяем копию — оригинал не должен меняться
        xCopy[0] = 999;
        assertEquals(1.0, f.getX(0), 1e-10);
    }

    @Test
    @DisplayName("getyVal возвращает копию массива y с правильной длиной")
    void getyValReturnsCopyAndCorrectLength() {
        ArrayTabulatedFunction f = createTestFunction();
        double[] yCopy = f.getyVal();

        assertArrayEquals(new double[]{1.0, 4.0, 6.0, 8.0, 10.0}, yCopy, 1e-10);
        assertEquals(5, yCopy.length);

        // Изменяем копию — оригинал не должен меняться
        yCopy[0] = 999;
        assertEquals(1.0, f.getY(0), 1e-10);
    }

    @Test
    @DisplayName("getxVal и getyVal возвращают только первые count элементов после удаления")
    void getxValGetyValAfterRemoval() {
        ArrayTabulatedFunction f = createTestFunction();
        f.remove(2); // удалили x=3.0, y=6.0 → теперь count=4
        f.remove(3); // удалили x=4.0, y=8.0 → теперь count=3

        double[] xCopy = f.getxVal();
        double[] yCopy = f.getyVal();

        assertEquals(3, xCopy.length);
        assertEquals(3, yCopy.length);

        assertArrayEquals(new double[]{1.0, 2.0, 4.0}, xCopy, 1e-10);
        assertArrayEquals(new double[]{1.0, 4.0, 8.0}, yCopy, 1e-10);
    }

    // === remove ===

    @Test
    void testRemoveMiddleElement() {
        ArrayTabulatedFunction f = createTestFunction();
        assertEquals(5, f.getCount());

        f.remove(2); // удаляем x=3.0, y=6.0

        assertEquals(4, f.getCount());

        assertEquals(1.0, f.getX(0));
        assertEquals(2.0, f.getX(1));
        assertEquals(4.0, f.getX(2));
        assertEquals(5.0, f.getX(3));

        assertEquals(1.0, f.getY(0));
        assertEquals(4.0, f.getY(1));
        assertEquals(8.0, f.getY(2));
        assertEquals(10.0, f.getY(3));

        assertEquals(6.0, f.interpolate(3.0, 1), 1e-10);
        assertEquals(6.0, f.apply(3.0), 1e-10);
    }

    @Test
    void testRemoveFirstElement() {
        ArrayTabulatedFunction f = createTestFunction();
        f.remove(0); // удаляем x=1.0, y=1.0

        assertEquals(4, f.getCount());

        assertEquals(2.0, f.getX(0));
        assertEquals(3.0, f.getX(1));
        assertEquals(4.0, f.getX(2));
        assertEquals(5.0, f.getX(3));

        assertEquals(4.0, f.getY(0));
        assertEquals(6.0, f.getY(1));
        assertEquals(8.0, f.getY(2));
        assertEquals(10.0, f.getY(3));

        assertEquals(3.0, f.extrapolateLeft(1.5), 1e-10);
        assertEquals(3.0, f.apply(1.5), 1e-10);
    }

    @Test
    void testRemoveLastElement() {
        ArrayTabulatedFunction f = createTestFunction();
        f.remove(4); // удаляем x=5.0, y=10.0

        assertEquals(4, f.getCount());

        assertEquals(1.0, f.getX(0));
        assertEquals(2.0, f.getX(1));
        assertEquals(3.0, f.getX(2));
        assertEquals(4.0, f.getX(3));

        assertEquals(1.0, f.getY(0));
        assertEquals(4.0, f.getY(1));
        assertEquals(6.0, f.getY(2));
        assertEquals(8.0, f.getY(3));

        assertEquals(12.0, f.extrapolateRight(6.0), 1e-10);
        assertEquals(12.0, f.apply(6.0), 1e-10);
    }

    @Test
    void testRemoveMultipleElements() {
        ArrayTabulatedFunction f = createTestFunction();

        f.remove(0); // удаляем (1,1)
        f.remove(1); // удаляем (3,6) — теперь на позиции 1 был (4,8)

        assertEquals(3, f.getCount());
        assertEquals(2.0, f.getX(0));
        assertEquals(4.0, f.getX(1));
        assertEquals(4.0, f.getY(0));
        assertEquals(8.0, f.getY(1));
        assertEquals(7.0, f.apply(3.5), 1e-10);
        assertEquals(0, f.floorIndexOfX(3.5));
        assertEquals(2.0, f.extrapolateLeft(1.0), 1e-10);
        assertEquals(12.0, f.extrapolateRight(6.0), 1e-10);
    }

    @Test
    void testRemoveNegativeIndex() {
        ArrayTabulatedFunction f = createTestFunction();
        assertThrows(IndexOutOfBoundsException.class, () -> f.remove(-1));
    }

    @Test
    void testRemoveIndexTooLarge() {
        ArrayTabulatedFunction f = createTestFunction();
        assertThrows(IndexOutOfBoundsException.class, () -> f.remove(5)); // max index = 4
    }

    @Test
    void testRemoveOnEmptyAfterAllRemoved() {
        ArrayTabulatedFunction f = createTestFunction();
        f.remove(0);
        f.remove(0);
        assertEquals(3, f.getCount());

        assertThrows(IndexOutOfBoundsException.class, () -> f.remove(0));
    }

    // === Тест на сохранение упорядоченности и корректность после удаления ===

    @Test
    void testFloorIndexOfXAfterRemoval() {
        ArrayTabulatedFunction f = createTestFunction();
        f.remove(2); // удалили x=3.0

        assertEquals(0, f.floorIndexOfX(1.0));
        assertEquals(0, f.floorIndexOfX(1.5));
        assertEquals(1, f.floorIndexOfX(2.0));
        assertEquals(1, f.floorIndexOfX(2.5));
        assertEquals(1, f.floorIndexOfX(3.0));
        assertEquals(1, f.floorIndexOfX(3.9));
        assertEquals(2, f.floorIndexOfX(4.0));
        assertEquals(2, f.floorIndexOfX(4.5));
        assertEquals(3, f.floorIndexOfX(5.0));
    }

    @Test
    void testApplyAfterRemoval() {
        ArrayTabulatedFunction f = createTestFunction();
        f.remove(1); // удаляем x=2.0, y=4.0 → теперь точки: [1, 3, 4, 5] с y=[1,6,8,10]

        assertEquals(3.5, f.apply(2.0), 1e-10); // (1+6)/2? Нет — интерполяция: 1 + (6-1)/(3-1)*(2-1) = 1 + 2.5 = 3.5
        assertEquals(7.0, f.apply(3.5), 1e-10); // между 3 и 4: (6+8)/2 = 7
        assertEquals(12.0, f.apply(6.0), 1e-10); // экстраполяция справа: 10 + 2*(6-5) = 12
    }

    // === Тест на indexOfX / indexOfY после удаления ===

    @Test
    void testIndexOfXAndYAfterRemoval() {
        ArrayTabulatedFunction f = createTestFunction();
        f.remove(2); // удалили x=3.0, y=6.0

        assertEquals(0, f.indexOfX(1.0));
        assertEquals(1, f.indexOfX(2.0));
        assertEquals(-1, f.indexOfX(3.0)); // удалено!
        assertEquals(2, f.indexOfX(4.0));
        assertEquals(3, f.indexOfX(5.0));

        assertEquals(0, f.indexOfY(1.0));
        assertEquals(1, f.indexOfY(4.0));
        assertEquals(-1, f.indexOfY(6.0)); // удалено!
        assertEquals(2, f.indexOfY(8.0));
        assertEquals(3, f.indexOfY(10.0));
    }

    // === ФИНАЛЬНЫЙ ТЕСТ: все методы должны бросать исключение при count == 0 ===


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
    void testArrayWithArrayAndThen(){
        //f(x) = x + 1
        double[] x1 = {0.0, 1.0, 2.0, 3.0, 4.0};
        double[] y1 = {1.0, 2.0, 3.0, 4.0, 5.0};
        ArrayTabulatedFunction f = new ArrayTabulatedFunction(x1, y1);

        //g(x) = 2x + 3
        double[] x2 = {0.0, 1.0, 2.0, 3.0, 4.0};
        double[] y2 = {3.0, 5.0, 7.0, 9.0, 11.0};
        ArrayTabulatedFunction g = new ArrayTabulatedFunction(x2, y2);

        MathFunction composition = f.andThen(g);
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
    void testArrayWithArrayExponentialWithLogarithmicComposition() {
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
    void testArrayWithArrayTrigonometricComposition() {
        //f(x) = sin(x)
        double[] x1 = {0.0, Math.PI/6, Math.PI/4, Math.PI/3, Math.PI/2};
        double[] y1 = {0.0, 0.5, 0.7071, 0.8660, 1.0};
        ArrayTabulatedFunction f = new ArrayTabulatedFunction(x1, y1);



        //g(x) = cos(x)
        double[] x2 = {0.0, Math.PI/6, Math.PI/4, Math.PI/3, Math.PI/2};
        double[] y2 = {1.0, 0.8660, 0.7071, 0.5, 0.0};
        ArrayTabulatedFunction g = new ArrayTabulatedFunction(x2, y2);

        //h(x) = f(g(x)) = sin(cos(x))
        CompositeFunction composition = new CompositeFunction(g, f);

        assertEquals(Math.sin(1.0), composition.apply(0.0), 0.1, "sin(cos(0)) = sin(1)");
        assertEquals(Math.sin(0.8660), composition.apply(Math.PI/6), 0.1, "sin(cos(π/6))");
        assertEquals(Math.sin(0.7071), composition.apply(Math.PI/4), 0.1, "sin(cos(π/4))");
        assertEquals(Math.sin(0.5), composition.apply(Math.PI/3), 0.1, "sin(cos(π/3))");
        assertEquals(Math.sin(0.0), composition.apply(Math.PI/2), 0.1, "sin(cos(π/2)) = sin(0)");
    }

    @Test
    void testArrayWithArrayCompositeWithExtrapolation() {
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

    void testArrayWithLinkedList(){
        //f(x) = x + 1
        double[] x1 = {0.0, 1.0, 2.0, 3.0, 4.0};
        double[] y1 = {1.0, 2.0, 3.0, 4.0, 5.0};
        ArrayTabulatedFunction f = new ArrayTabulatedFunction(x1, y1);

        //g(x) = 2x + 3
        double[] x2 = {0.0, 1.0, 2.0, 3.0, 4.0};
        double[] y2 = {3.0, 5.0, 7.0, 9.0, 11.0};
        LinkedListTabulatedFunction g = new LinkedListTabulatedFunction(x2, y2);

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
    void testArrayWithLinkedListExponentialWithLogarithmicComposition() {
        //f(x) = x + 1
        double[] x1 = {0.0, 0.5, 1.0, 1.5, 2.0};
        double[] y1 = {1.0, 1.5, 2.0, 2.5, 3.0};
        ArrayTabulatedFunction f = new ArrayTabulatedFunction(x1, y1);

        //g(x) = ln(x + 1)
        double[] x2 = {0.0, 1.0, 2.0, 3.0, 4.0};
        double[] y2 = {0.0, 0.6931, 1.0986, 1.3863, 1.6094};
        LinkedListTabulatedFunction g = new LinkedListTabulatedFunction(x2, y2);



        //h(x) = f(g(x)) = ln(x + 1) + 1
        CompositeFunction composition = new CompositeFunction(g, f);

        assertEquals(1.0, composition.apply(0.0), 0.1, "f(g(0)) = f(0) = 1");
        assertEquals(1.6931, composition.apply(1.0), 0.1, "f(g(1)) = f(ln2) ≈ 2");
        assertEquals(2.0986, composition.apply(2.0), 0.1, "f(g(2)) = f(ln3) ≈ 3");
        assertEquals(2.3863, composition.apply(3.0), 0.1, "f(g(3)) = f(ln4) ≈ 4");
        assertEquals(2.6094, composition.apply(4.0), 0.1, "f(g(4)) = f(ln5) ≈ 5");
    }

    @Test
    void testArrayWithLinkedListTrigonometricComposition() {
        //f(x) = sin(x)
        double[] x1 = {0.0, Math.PI/6, Math.PI/4, Math.PI/3, Math.PI/2};
        double[] y1 = {0.0, 0.5, 0.7071, 0.8660, 1.0};
        ArrayTabulatedFunction f = new ArrayTabulatedFunction(x1, y1);

        //g(x) = cos(x)
        double[] x2 = {0.0, Math.PI/6, Math.PI/4, Math.PI/3, Math.PI/2};
        double[] y2 = {1.0, 0.8660, 0.7071, 0.5, 0.0};
        LinkedListTabulatedFunction g = new LinkedListTabulatedFunction(x2, y2);

        //h(x) = f(g(x)) = sin(cos(x))
        CompositeFunction composition = new CompositeFunction(g, f);

        assertEquals(Math.sin(1.0), composition.apply(0.0), 0.1, "sin(cos(0)) = sin(1)");
        assertEquals(Math.sin(0.8660), composition.apply(Math.PI/6), 0.1, "sin(cos(π/6))");
        assertEquals(Math.sin(0.7071), composition.apply(Math.PI/4), 0.1, "sin(cos(π/4))");
        assertEquals(Math.sin(0.5), composition.apply(Math.PI/3), 0.1, "sin(cos(π/3))");
        assertEquals(Math.sin(0.0), composition.apply(Math.PI/2), 0.1, "sin(cos(π/2)) = sin(0)");
    }

    @Test
    void testArrayWithLinkedListCompositeWithExtrapolation() {
        //f(x) = √x
        double[] x1 = {0.0, 1.0, 2.0, 3.0, 4.0};
        double[] y1 = {0.0, 1.0, 1.4142, 1.7321, 2.0};
        ArrayTabulatedFunction f = new ArrayTabulatedFunction(x1, y1);

        //g(x) = x - 3
        double[] x2 = {0.0, 1.0, 2.0, 3.0, 4.0, 5.0, 6.0};
        double[] y2 = {-3.0, -2.0, -1.0, 0.0, 1.0, 2.0, 3.0};
        LinkedListTabulatedFunction g = new LinkedListTabulatedFunction(x2, y2);

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