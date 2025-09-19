package operations;

import functions.TabulatedFunction;
import functions.Point;
import functions.ArrayTabulatedFunction;
import functions.LinkedListTabulatedFunction;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
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
        double[] xValues = {1.0, 2.0};
        double[] yValues = {10.0, 20.0};
        TabulatedFunction function = new ArrayTabulatedFunction(xValues, yValues);

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
}