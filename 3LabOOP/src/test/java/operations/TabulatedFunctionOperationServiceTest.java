package operations;

import exception.InconsistentFunctionsException;
import functions.TabulatedFunction;
import functions.Point;
import functions.ArrayTabulatedFunction;
import functions.LinkedListTabulatedFunction;
import functions.factory.*;
import functions.factory.TabulatedFunctionFactory;
import org.junit.jupiter.api.*;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class TabulatedFunctionOperationServiceTest {

    @Test
    @DisplayName("asPoints должен корректно преобразовывать ArrayTabulatedFunction")
    void testAsPointsWithArrayTabulatedFunction() {
        double[] xValues = {1.0, 2.0, 3.0, 4.0};
        double[] yValues = {10.0, 20.0, 30.0, 40.0};
        TabulatedFunction function = new ArrayTabulatedFunction(xValues, yValues);

        Point[] points = TabulatedFunctionOperationService.asPoints(function);

        assertNotNull(points);
        assertEquals(4, points.length);

        assertEquals(1.0, points[0].x, 0.0001);
        assertEquals(10.0, points[0].y, 0.0001);
        assertEquals(2.0, points[1].x, 0.0001);
        assertEquals(20.0, points[1].y, 0.0001);
        assertEquals(3.0, points[2].x, 0.0001);
        assertEquals(30.0, points[2].y, 0.0001);
        assertEquals(4.0, points[3].x, 0.0001);
        assertEquals(40.0, points[3].y, 0.0001);
    }

    @Test
    @DisplayName("asPoints должен корректно преобразовывать LinkedListTabulatedFunction")
    void testAsPointsWithLinkedListTabulatedFunction() {
        double[] xValues = {1.0, 2.0, 3.0};
        double[] yValues = {10.0, 20.0, 30.0};
        TabulatedFunction function = new LinkedListTabulatedFunction(xValues, yValues);

        Point[] points = TabulatedFunctionOperationService.asPoints(function);

        assertNotNull(points);
        assertEquals(3, points.length);

        assertEquals(1.0, points[0].x, 0.0001);
        assertEquals(10.0, points[0].y, 0.0001);
        assertEquals(2.0, points[1].x, 0.0001);
        assertEquals(20.0, points[1].y, 0.0001);
        assertEquals(3.0, points[2].x, 0.0001);
        assertEquals(30.0, points[2].y, 0.0001);
    }

    @Test
    @DisplayName("asPoints должен бросать исключение для null функции")
    void testAsPointsWithNullFunction() {
        assertThrows(IllegalArgumentException.class, () -> {
            TabulatedFunctionOperationService.asPoints(null);
        });
    }

    @Test
    @DisplayName("asPoints должен корректно работать с минимальным количеством точек (2 точки)")
    void testAsPointsWithMinimumPoints() {
        double[] xValues = {1.0, 2.0};
        double[] yValues = {10.0, 20.0};

        TabulatedFunction arrayFunction = new ArrayTabulatedFunction(xValues, yValues);
        TabulatedFunction linkedListFunction = new LinkedListTabulatedFunction(xValues, yValues);

        Point[] arrayPoints = TabulatedFunctionOperationService.asPoints(arrayFunction);
        Point[] linkedListPoints = TabulatedFunctionOperationService.asPoints(linkedListFunction);

        assertEquals(2, arrayPoints.length);
        assertEquals(2, linkedListPoints.length);

        assertEquals(1.0, arrayPoints[0].x, 0.0001);
        assertEquals(10.0, arrayPoints[0].y, 0.0001);
        assertEquals(2.0, arrayPoints[1].x, 0.0001);
        assertEquals(20.0, arrayPoints[1].y, 0.0001);

        assertEquals(1.0, linkedListPoints[0].x, 0.0001);
        assertEquals(10.0, linkedListPoints[0].y, 0.0001);
        assertEquals(2.0, linkedListPoints[1].x, 0.0001);
        assertEquals(20.0, linkedListPoints[1].y, 0.0001);
    }

    @Test
    @DisplayName("asPoints должен создавать новые независимые объекты Point")
    void testAsPointsCreatesNewObjects() {
        double[] xVal = {1.0, 2.0};
        double[] yVal = {10.0, 20.0};
        TabulatedFunction function = new ArrayTabulatedFunction(xVal, yVal);

        Point[] points = TabulatedFunctionOperationService.asPoints(function);

        // Проверяем, что это новые объекты (не те же ссылки)
        for (int i = 0; i < points.length; i++) {
            assertNotSame(function.iterator().next(), points[i]);
        }

        // Проверяем, что значения корректны
        assertEquals(1.0, points[0].x, 0.0001);
        assertEquals(10.0, points[0].y, 0.0001);
        assertEquals(2.0, points[1].x, 0.0001);
        assertEquals(20.0, points[1].y, 0.0001);
    }

    @Test
    @DisplayName("asPoints должен корректно работать с отрицательными значениями")
    void testAsPointsWithNegativeValues() {
        double[] xValues = {-2.0, -1.0, 0.0, 1.0, 2.0};
        double[] yValues = {4.0, 1.0, 0.0, 1.0, 4.0};

        TabulatedFunction arrayFunction = new ArrayTabulatedFunction(xValues, yValues);
        Point[] points = TabulatedFunctionOperationService.asPoints(arrayFunction);

        assertEquals(5, points.length);
        assertEquals(-2.0, points[0].x, 0.0001);
        assertEquals(4.0, points[0].y, 0.0001);
        assertEquals(0.0, points[2].x, 0.0001);
        assertEquals(0.0, points[2].y, 0.0001);
        assertEquals(2.0, points[4].x, 0.0001);
        assertEquals(4.0, points[4].y, 0.0001);
    }

    @Test
    @DisplayName("asPoints должен корректно работать с десятичными значениями")
    void testAsPointsWithDecimalValues() {
        double[] xValues = {0.1, 0.2, 0.3, 0.4, 0.5};
        double[] yValues = {0.01, 0.04, 0.09, 0.16, 0.25};

        TabulatedFunction function = new LinkedListTabulatedFunction(xValues, yValues);
        Point[] points = TabulatedFunctionOperationService.asPoints(function);

        assertEquals(5, points.length);
        assertEquals(0.1, points[0].x, 0.0000001);
        assertEquals(0.01, points[0].y, 0.0000001);
        assertEquals(0.3, points[2].x, 0.0000001);
        assertEquals(0.09, points[2].y, 0.0000001);
        assertEquals(0.5, points[4].x, 0.0000001);
        assertEquals(0.25, points[4].y, 0.0000001);
    }

    @Test
    @DisplayName("asPoints должен корректно работать с большими массивами")
    void testAsPointsWithLargeArrays() {
        int size = 100;
        double[] xValues = new double[size];
        double[] yValues = new double[size];

        for (int i = 0; i < size; i++) {
            xValues[i] = i;
            yValues[i] = i * 10;
        }

        TabulatedFunction arrayFunction = new ArrayTabulatedFunction(xValues, yValues);
        Point[] points = TabulatedFunctionOperationService.asPoints(arrayFunction);

        assertEquals(size, points.length);
        assertEquals(0.0, points[0].x, 0.0001);
        assertEquals(0.0, points[0].y, 0.0001);
        assertEquals(99.0, points[99].x, 0.0001);
        assertEquals(990.0, points[99].y, 0.0001);
    }

    @Test
    @DisplayName("asPoints должен согласовываться с итератором функции")
    void testAsPointsConsistentWithIterator() {
        double[] xValues = {1.0, 2.0, 3.0, 4.0};
        double[] yValues = {10.0, 20.0, 30.0, 40.0};

        TabulatedFunction arrayFunction = new ArrayTabulatedFunction(xValues, yValues);
        TabulatedFunction linkedListFunction = new LinkedListTabulatedFunction(xValues, yValues);

        Point[] arrayPoints = TabulatedFunctionOperationService.asPoints(arrayFunction);
        Point[] linkedListPoints = TabulatedFunctionOperationService.asPoints(linkedListFunction);

        int i = 0;
        for (Point point : arrayFunction) {
            assertEquals(point.x, arrayPoints[i].x, 0.0001);
            assertEquals(point.y, arrayPoints[i].y, 0.0001);
            i++;
        }

        i = 0;
        for (Point point : linkedListFunction) {
            assertEquals(point.x, linkedListPoints[i].x, 0.0001);
            assertEquals(point.y, linkedListPoints[i].y, 0.0001);
            i++;
        }
    }
    // package test;


    private static final double DELTA = 1e-10;

        // ========== Тестовые данные ==========
        private static final double[] X1 = {0.0, 1.0, 2.0, 3.0};
        private static final double[] Y1 = {1.0, 3.0, 5.0, 7.0}; // f(x) = 2x + 1
        private static final double[] Y2 = {0.0, 1.0, 4.0, 9.0}; // f(x) = x^2

        // ========== Фабрики ==========
        private static Stream<TabulatedFunctionFactory> provideFactories() {
            return Stream.of(
                    new ArrayTabulatedFunctionFactory(),
                    new LinkedListTabulatedFunctionFactory()
            );
        }

        // ========== Тест: сложение одинаковых функций ==========
        @Test
        @DisplayName("Сложение двух одинаковых функций")
        void testAddSameTypeFunctions(TabulatedFunctionFactory factory) {
            TabulatedFunctionOperationService service = new TabulatedFunctionOperationService(factory);

            TabulatedFunction f1 = factory.create(X1, Y1);
            TabulatedFunction f2 = factory.create(X1, Y2);

            TabulatedFunction result = service.add(f1, f2);

            assertEquals(X1.length, result.getCount());
            for (int i = 0; i < X1.length; i++) {
                double expected = Y1[i] + Y2[i];
                assertEquals(expected, result.getY(i), DELTA,
                        "Неверный результат сложения в точке " + i);
            }

            // Проверка типа результата
            if (factory instanceof ArrayTabulatedFunctionFactory) {
                assertTrue(result instanceof ArrayTabulatedFunction);
            } else if (factory instanceof LinkedListTabulatedFunctionFactory) {
                assertTrue(result instanceof LinkedListTabulatedFunction);
            }
        }

        // ========== Тест: вычитание разных типов функций ==========
        @Test
        @DisplayName("Вычитание функций разных типов (Array - LinkedList)")
        void testSubtractDifferentTypes() {
            TabulatedFunctionOperationService service = new TabulatedFunctionOperationService();

            TabulatedFunction f1 = new ArrayTabulatedFunction(X1, Y1);
            TabulatedFunction f2 = new LinkedListTabulatedFunction(X1, Y2);

            TabulatedFunction result = service.subtract(f1, f2);

            assertEquals(X1.length, result.getCount());
            for (int i = 0; i < X1.length; i++) {
                double expected = Y1[i] - Y2[i];
                assertEquals(expected, result.getY(i), DELTA,
                        "Неверный результат вычитания в точке " + i);
            }

            // По умолчанию используется Array фабрика
            assertTrue(result instanceof ArrayTabulatedFunction);
        }

        // ========== Тест: сложение разных типов функций ==========
        @Test
        @DisplayName("Сложение функций разных типов (LinkedList + Array)")
        void testAddDifferentTypes() {
            TabulatedFunctionFactory factory = new LinkedListTabulatedFunctionFactory();
            TabulatedFunctionOperationService service = new TabulatedFunctionOperationService(factory);

            TabulatedFunction f1 = new LinkedListTabulatedFunction(X1, Y1);
            TabulatedFunction f2 = new ArrayTabulatedFunction(X1, Y2);

            TabulatedFunction result = service.add(f1, f2);

            assertEquals(X1.length, result.getCount());
            for (int i = 0; i < X1.length; i++) {
                double expected = Y1[i] + Y2[i];
                assertEquals(expected, result.getY(i), DELTA);
            }

            assertTrue(result instanceof LinkedListTabulatedFunction);
        }

        // ========== Тест: разное количество точек ==========
        @Test
        @DisplayName("Исключение при разном количестве точек")
        void testInconsistentCountThrowsException() {
            TabulatedFunctionOperationService service = new TabulatedFunctionOperationService();

            TabulatedFunction f1 = new ArrayTabulatedFunction(X1, Y1);
            TabulatedFunction f2 = new ArrayTabulatedFunction(
                    new double[]{0.0, 1.0}, // только 2 точки
                    new double[]{1.0, 2.0}
            );

            InconsistentFunctionsException exception = assertThrows(
                    InconsistentFunctionsException.class,
                    () -> service.add(f1, f2),
                    "Должно бросаться исключение при разном количестве точек"
            );

            assertTrue(exception.getMessage().contains("differ"),
                    "Сообщение должно содержать информацию о несовпадении количества");
        }

        // ========== Тест: разные значения X ==========
        @Test
        @DisplayName("Исключение при несовпадении X-значений")
        void testInconsistentXValuesThrowsException() {
            TabulatedFunctionOperationService service = new TabulatedFunctionOperationService();

            TabulatedFunction f1 = new ArrayTabulatedFunction(X1, Y1);
            double[] x2 = {0.0, 1.0, 2.0, 4.0}; // Последнее значение отличается!
            TabulatedFunction f2 = new ArrayTabulatedFunction(x2, Y2);

            InconsistentFunctionsException exception = assertThrows(
                    InconsistentFunctionsException.class,
                    () -> service.add(f1, f2),
                    "Должно бросаться исключение при несовпадении X-значений"
            );

            assertTrue(exception.getMessage().contains("X values differ at index 3"),
                    "Сообщение должно указывать на индекс и значения");
        }

        // ========== Тест: null функции ==========
        @Test
        @DisplayName("Исключение при передаче null")
        void testNullFunctionThrowsException() {
            TabulatedFunctionOperationService service = new TabulatedFunctionOperationService();

            TabulatedFunction f = new ArrayTabulatedFunction(X1, Y1);

            assertThrows(IllegalArgumentException.class,
                    () -> service.add(null, f),
                    "Должно бросаться исключение при null первой функции");

            assertThrows(IllegalArgumentException.class,
                    () -> service.add(f, null),
                    "Должно бросаться исключение при null второй функции");
        }

        // ========== Тест: сеттер и геттер фабрики ==========
        @Test
        @DisplayName("Проверка сеттера и геттера фабрики")
        void testFactorySetterGetter() {
            TabulatedFunctionOperationService service = new TabulatedFunctionOperationService();

            TabulatedFunctionFactory newFactory = new LinkedListTabulatedFunctionFactory();
            service.setFactory(newFactory);

            assertSame(newFactory, service.getFactory(),
                    "Геттер должен возвращать установленную фабрику");
        }

        // ========== Тест: конструктор по умолчанию ==========
        @Test
        @DisplayName("Конструктор по умолчанию использует ArrayTabulatedFunctionFactory")
        void testDefaultConstructorUsesArrayFactory() {
            TabulatedFunctionOperationService service = new TabulatedFunctionOperationService();
            assertTrue(service.getFactory() instanceof ArrayTabulatedFunctionFactory,
                    "По умолчанию должна использоваться Array фабрика");
        }

}