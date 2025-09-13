package functions;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Тестовый класс для LinkedListTabulatedFunction.
 */
class LinkedListTabulatedFunctionTest {

    private static final double DELTA = 0.0001;

    @Test
    void testConstructorWithArrays() {
        double[] xValues = {1.0, 2.0, 3.0, 4.0};
        double[] yValues = {10.0, 20.0, 30.0, 40.0};

        LinkedListTabulatedFunction function = new LinkedListTabulatedFunction(xValues, yValues);

        assertEquals(4, function.getCount(), "Количество точек должно быть 4");
        assertEquals(1.0, function.leftBound(), DELTA, "Левая граница");
        assertEquals(4.0, function.rightBound(), DELTA, "Правая граница");

        // Проверка значений
        assertEquals(1.0, function.getX(0), DELTA, "x[0]");
        assertEquals(2.0, function.getX(1), DELTA, "x[1]");
        assertEquals(3.0, function.getX(2), DELTA, "x[2]");
        assertEquals(4.0, function.getX(3), DELTA, "x[3]");

        assertEquals(10.0, function.getY(0), DELTA, "y[0]");
        assertEquals(20.0, function.getY(1), DELTA, "y[1]");
        assertEquals(30.0, function.getY(2), DELTA, "y[2]");
        assertEquals(40.0, function.getY(3), DELTA, "y[3]");
    }

    @Test
    void testConstructorWithFunction() {
        MathFunction sqr = new SqrFunction();
        LinkedListTabulatedFunction function = new LinkedListTabulatedFunction(sqr, 0.0, 4.0, 5);

        assertEquals(5, function.getCount(), "Количество точек должно быть 5");
        assertEquals(0.0, function.leftBound(), DELTA, "Левая граница");
        assertEquals(4.0, function.rightBound(), DELTA, "Правая граница");

        // Проверка значений
        assertEquals(0.0, function.getX(0), DELTA, "x[0]");
        assertEquals(1.0, function.getX(1), DELTA, "x[1]");
        assertEquals(2.0, function.getX(2), DELTA, "x[2]");
        assertEquals(3.0, function.getX(3), DELTA, "x[3]");
        assertEquals(4.0, function.getX(4), DELTA, "x[4]");

        assertEquals(0.0, function.getY(0), DELTA, "y[0]");
        assertEquals(1.0, function.getY(1), DELTA, "y[1]");
        assertEquals(4.0, function.getY(2), DELTA, "y[2]");
        assertEquals(9.0, function.getY(3), DELTA, "y[3]");
        assertEquals(16.0, function.getY(4), DELTA, "y[4]");
    }

    @Test
    void testGetNode() {
        double[] xValues = {1.0, 2.0, 3.0};
        double[] yValues = {10.0, 20.0, 30.0};
        LinkedListTabulatedFunction function = new LinkedListTabulatedFunction(xValues, yValues);

        // Проверка getNode через публичные методы
        assertEquals(1.0, function.getX(0), DELTA, "getNode(0).x");
        assertEquals(10.0, function.getY(0), DELTA, "getNode(0).y");

        assertEquals(2.0, function.getX(1), DELTA, "getNode(1).x");
        assertEquals(20.0, function.getY(1), DELTA, "getNode(1).y");

        assertEquals(3.0, function.getX(2), DELTA, "getNode(2).x");
        assertEquals(30.0, function.getY(2), DELTA, "getNode(2).y");

        assertThrows(IndexOutOfBoundsException.class, () -> function.getX(-1), "Отрицательный индекс");
        assertThrows(IndexOutOfBoundsException.class, () -> function.getX(3), "Индекс за границами");
    }

    @Test
    void testSetY() {
        double[] xValues = {1.0, 2.0, 3.0};
        double[] yValues = {10.0, 20.0, 30.0};
        LinkedListTabulatedFunction function = new LinkedListTabulatedFunction(xValues, yValues);

        function.setY(1, 25.0);
        assertEquals(25.0, function.getY(1), DELTA, "setY должен изменить значение");

        function.setY(2, 35.0);
        assertEquals(35.0, function.getY(2), DELTA, "setY должен изменить значение");
    }

    @Test
    void testIndexOfX() {
        double[] xValues = {1.0, 2.0, 3.0, 4.0};
        double[] yValues = {10.0, 20.0, 30.0, 40.0};
        LinkedListTabulatedFunction function = new LinkedListTabulatedFunction(xValues, yValues);

        assertEquals(0, function.indexOfX(1.0), "indexOfX(1.0)");
        assertEquals(1, function.indexOfX(2.0), "indexOfX(2.0)");
        assertEquals(2, function.indexOfX(3.0), "indexOfX(3.0)");
        assertEquals(3, function.indexOfX(4.0), "indexOfX(4.0)");

        assertEquals(-1, function.indexOfX(0.0), "indexOfX(0.0) не найден");
        assertEquals(-1, function.indexOfX(2.5), "indexOfX(2.5) не найден");
    }

    @Test
    void testFloorIndexOfX() {
        double[] xValues = {1.0, 3.0, 5.0, 7.0};
        double[] yValues = {10.0, 30.0, 50.0, 70.0};
        LinkedListTabulatedFunction function = new LinkedListTabulatedFunction(xValues, yValues);

        assertEquals(0, function.floorIndexOfX(0.5), "floorIndexOfX(0.5)");
        assertEquals(0, function.floorIndexOfX(2.0), "floorIndexOfX(2.0)");
        assertEquals(1, function.floorIndexOfX(4.0), "floorIndexOfX(4.0)");
        assertEquals(2, function.floorIndexOfX(6.0), "floorIndexOfX(6.0)");
        assertEquals(3, function.floorIndexOfX(7.0), "floorIndexOfX(7.0)");
        assertEquals(4, function.floorIndexOfX(8.0), "floorIndexOfX(8.0)");
    }

    @Test
    void testApply() {
        double[] xValues = {1.0, 2.0, 3.0};
        double[] yValues = {10.0, 20.0, 30.0};
        LinkedListTabulatedFunction function = new LinkedListTabulatedFunction(xValues, yValues);

        // Точные значения
        assertEquals(10.0, function.apply(1.0), DELTA, "apply(1.0)");
        assertEquals(20.0, function.apply(2.0), DELTA, "apply(2.0)");
        assertEquals(30.0, function.apply(3.0), DELTA, "apply(3.0)");

        // Интерполяция
        assertEquals(15.0, function.apply(1.5), DELTA, "apply(1.5) интерполяция");
        assertEquals(25.0, function.apply(2.5), DELTA, "apply(2.5) интерполяция");

        // Экстраполяция
        assertEquals(0.0, function.apply(0.0), DELTA, "apply(0.0) экстраполяция");
        assertEquals(35.0, function.apply(3.5), DELTA, "apply(3.5) экстраполяция");
    }

    @Test
    void testCircularListStructure() {
        double[] xValues = {1.0, 2.0, 3.0};
        double[] yValues = {10.0, 20.0, 30.0};
        LinkedListTabulatedFunction function = new LinkedListTabulatedFunction(xValues, yValues);

        assertEquals(1.0, function.getX(0), DELTA, "Первый элемент");
        assertEquals(3.0, function.getX(2), DELTA, "Последний элемент");


        assertEquals(0, function.indexOfX(1.0), "indexOfX для первого элемента");
        assertEquals(2, function.indexOfX(3.0), "indexOfX для последнего элемента");

        assertEquals(1.0, function.getX(0), DELTA, "Индекс 0");
        assertEquals(2.0, function.getX(1), DELTA, "Индекс 1");
        assertEquals(3.0, function.getX(2), DELTA, "Индекс 2");

        assertThrows(IndexOutOfBoundsException.class, () -> function.getX(3),
                "Индекс 3 должен вызывать исключение");
        assertThrows(IndexOutOfBoundsException.class, () -> function.getX(6),
                "Индекс 6 должен вызывать исключение");
    }

    @Test
    void testSinglePointFunction() {
        MathFunction constant = new ConstantFunction(5.0);
        LinkedListTabulatedFunction function = new LinkedListTabulatedFunction(constant, 2.0, 2.0, 3);

        assertEquals(3, function.getCount(), "Количество точек должно быть 3");
        assertEquals(2.0, function.leftBound(), DELTA, "Левая граница");
        assertEquals(2.0, function.rightBound(), DELTA, "Правая граница");

        assertEquals(2.0, function.getX(0), DELTA, "x[0] для одной точки");
        assertEquals(2.0, function.getX(1), DELTA, "x[1] для одной точки");
        assertEquals(2.0, function.getX(2), DELTA, "x[2] для одной точки");

        assertEquals(5.0, function.getY(0), DELTA, "y[0] для одной точки");
    }
}