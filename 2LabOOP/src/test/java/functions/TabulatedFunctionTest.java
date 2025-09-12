package functions;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Тестовый класс для интерфейса TabulatedFunction.
 * Проверяет контракт интерфейса без конкретной реализации.
 */
class TabulatedFunctionTest {

    // Вспомогательный класс для тестирования интерфейса
    private static class TestTabulatedFunction implements TabulatedFunction {
        private final double[] xValues;
        private final double[] yValues;

        public TestTabulatedFunction(double[] xValues, double[] yValues) {
            this.xValues = xValues.clone();
            this.yValues = yValues.clone();
        }

        @Override
        public int getCount() {
            return xValues.length;
        }

        @Override
        public double getX(int index) {
            return xValues[index];
        }

        @Override
        public double getY(int index) {
            return yValues[index];
        }

        @Override
        public void setY(int index, double value) {
            yValues[index] = value;
        }

        @Override
        public int indexOfX(double x) {
            for (int i = 0; i < xValues.length; i++) {
                if (Math.abs(xValues[i] - x) < 1e-10) {
                    return i;
                }
            }
            return -1;
        }

        @Override
        public int indexOfY(double y) {
            for (int i = 0; i < yValues.length; i++) {
                if (Math.abs(yValues[i] - y) < 1e-10) {
                    return i;
                }
            }
            return -1;
        }

        @Override
        public double leftBound() {
            return xValues[0];
        }

        @Override
        public double rightBound() {
            return xValues[xValues.length - 1];
        }

        @Override
        public double apply(double x) {
            // Простая реализация для тестирования
            int index = indexOfX(x);
            return index != -1 ? yValues[index] : 0;
        }
    }

    @Test
    void testGetCount() {
        double[] xValues = {1.0, 2.0, 3.0};
        double[] yValues = {10.0, 20.0, 30.0};
        TabulatedFunction function = new TestTabulatedFunction(xValues, yValues);

        assertEquals(3, function.getCount(), "Количество точек должно быть 3");

        double[] singlePointX = {5.0};
        double[] singlePointY = {50.0};
        TabulatedFunction singleFunction = new TestTabulatedFunction(singlePointX, singlePointY);
        assertEquals(1, singleFunction.getCount(), "Количество точек должно быть 1 для одного элемента");

        double[] emptyX = {};
        double[] emptyY = {};
        TabulatedFunction emptyFunction = new TestTabulatedFunction(emptyX, emptyY);
        assertEquals(0, emptyFunction.getCount(), "Количество точек должно быть 0 для пустого массива");
    }

    @Test
    void testGetXAndGetY() {
        double[] xValues = {1.0, 2.0, 3.0, 4.0};
        double[] yValues = {10.0, 20.0, 30.0, 40.0};
        TabulatedFunction function = new TestTabulatedFunction(xValues, yValues);

        assertEquals(1.0, function.getX(0), 0.0001, "x[0] должен быть 1.0");
        assertEquals(2.0, function.getX(1), 0.0001, "x[1] должен быть 2.0");
        assertEquals(3.0, function.getX(2), 0.0001, "x[2] должен быть 3.0");
        assertEquals(4.0, function.getX(3), 0.0001, "x[3] должен быть 4.0");

        assertEquals(10.0, function.getY(0), 0.0001, "y[0] должен быть 10.0");
        assertEquals(20.0, function.getY(1), 0.0001, "y[1] должен быть 20.0");
        assertEquals(30.0, function.getY(2), 0.0001, "y[2] должен быть 30.0");
        assertEquals(40.0, function.getY(3), 0.0001, "y[3] должен быть 40.0");
    }

    @Test
    void testSetY() {
        double[] xValues = {1.0, 2.0, 3.0};
        double[] yValues = {10.0, 20.0, 30.0};
        TabulatedFunction function = new TestTabulatedFunction(xValues, yValues);

        function.setY(0, 100.0);
        assertEquals(100.0, function.getY(0), 0.0001, "y[0] должен быть изменен на 100.0");

        function.setY(1, 200.0);
        assertEquals(200.0, function.getY(1), 0.0001, "y[1] должен быть изменен на 200.0");

        function.setY(2, 300.0);
        assertEquals(300.0, function.getY(2), 0.0001, "y[2] должен быть изменен на 300.0");

        // Проверка, что x значения не изменились
        assertEquals(1.0, function.getX(0), 0.0001, "x[0] должен остаться 1.0");
        assertEquals(2.0, function.getX(1), 0.0001, "x[1] должен остаться 2.0");
        assertEquals(3.0, function.getX(2), 0.0001, "x[2] должен остаться 3.0");
    }

    @Test
    void testIndexOfX() {
        double[] xValues = {1.0, 2.0, 3.0, 4.0, 5.0};
        double[] yValues = {10.0, 20.0, 30.0, 40.0, 50.0};
        TabulatedFunction function = new TestTabulatedFunction(xValues, yValues);

        assertEquals(0, function.indexOfX(1.0), "Индекс x=1.0 должен быть 0");
        assertEquals(1, function.indexOfX(2.0), "Индекс x=2.0 должен быть 1");
        assertEquals(2, function.indexOfX(3.0), "Индекс x=3.0 должен быть 2");
        assertEquals(3, function.indexOfX(4.0), "Индекс x=4.0 должен быть 3");
        assertEquals(4, function.indexOfX(5.0), "Индекс x=5.0 должен быть 4");

        assertEquals(-1, function.indexOfX(0.0), "Индекс x=0.0 должен быть -1 (не найден)");
        assertEquals(-1, function.indexOfX(6.0), "Индекс x=6.0 должен быть -1 (не найден)");
        assertEquals(-1, function.indexOfX(2.5), "Индекс x=2.5 должен быть -1 (не найден)");
    }

    @Test
    void testIndexOfY() {
        double[] xValues = {1.0, 2.0, 3.0, 4.0, 5.0};
        double[] yValues = {10.0, 20.0, 30.0, 20.0, 50.0}; // Дубликат 20.0
        TabulatedFunction function = new TestTabulatedFunction(xValues, yValues);

        assertEquals(0, function.indexOfY(10.0), "Индекс первого y=10.0 должен быть 0");
        assertEquals(1, function.indexOfY(20.0), "Индекс первого y=20.0 должен быть 1");
        assertEquals(2, function.indexOfY(30.0), "Индекс первого y=30.0 должен быть 2");
        assertEquals(4, function.indexOfY(50.0), "Индекс первого y=50.0 должен быть 4");

        assertEquals(-1, function.indexOfY(0.0), "Индекс y=0.0 должен быть -1 (не найден)");
        assertEquals(-1, function.indexOfY(100.0), "Индекс y=100.0 должен быть -1 (не найден)");
        assertEquals(-1, function.indexOfY(25.0), "Индекс y=25.0 должен быть -1 (не найден)");
    }

    @Test
    void testLeftBoundAndRightBound() {
        double[] xValues1 = {1.0, 2.0, 3.0, 4.0, 5.0};
        double[] yValues1 = {10.0, 20.0, 30.0, 40.0, 50.0};
        TabulatedFunction function1 = new TestTabulatedFunction(xValues1, yValues1);

        assertEquals(1.0, function1.leftBound(), 0.0001, "Левая граница должна быть 1.0");
        assertEquals(5.0, function1.rightBound(), 0.0001, "Правая граница должна быть 5.0");

        double[] xValues2 = {-5.0, -3.0, 0.0, 3.0, 5.0};
        double[] yValues2 = {1.0, 2.0, 3.0, 4.0, 5.0};
        TabulatedFunction function2 = new TestTabulatedFunction(xValues2, yValues2);

        assertEquals(-5.0, function2.leftBound(), 0.0001, "Левая граница должна быть -5.0");
        assertEquals(5.0, function2.rightBound(), 0.0001, "Правая граница должна быть 5.0");

        double[] xValues3 = {10.0};
        double[] yValues3 = {100.0};
        TabulatedFunction function3 = new TestTabulatedFunction(xValues3, yValues3);

        assertEquals(10.0, function3.leftBound(), 0.0001, "Левая граница должна быть 10.0 для одного элемента");
        assertEquals(10.0, function3.rightBound(), 0.0001, "Правая граница должна быть 10.0 для одного элемента");
    }

    @Test
    void testApplyInheritance() {
        double[] xValues = {1.0, 2.0, 3.0};
        double[] yValues = {10.0, 20.0, 30.0};
        TabulatedFunction function = new TestTabulatedFunction(xValues, yValues);

        // Проверка, что TabulatedFunction является MathFunction
        assertTrue(function instanceof MathFunction, "TabulatedFunction должен наследоваться от MathFunction");

        // Проверка работы apply для существующих x
        assertEquals(10.0, function.apply(1.0), 0.0001, "apply(1.0) должен возвращать 10.0");
        assertEquals(20.0, function.apply(2.0), 0.0001, "apply(2.0) должен возвращать 20.0");
        assertEquals(30.0, function.apply(3.0), 0.0001, "apply(3.0) должен возвращать 30.0");
    }

    @Test
    void testEdgeCases() {
        // Тестирование граничных случаев
        double[] singleX = {5.0};
        double[] singleY = {50.0};
        TabulatedFunction singleFunction = new TestTabulatedFunction(singleX, singleY);

        assertEquals(5.0, singleFunction.getX(0), 0.0001, "Единственный x должен быть 5.0");
        assertEquals(50.0, singleFunction.getY(0), 0.0001, "Единственный y должен быть 50.0");
        assertEquals(5.0, singleFunction.leftBound(), 0.0001, "Левая граница должна быть 5.0");
        assertEquals(5.0, singleFunction.rightBound(), 0.0001, "Правая граница должна быть 5.0");
        assertEquals(0, singleFunction.indexOfX(5.0), "Индекс x=5.0 должен быть 0");
        assertEquals(-1, singleFunction.indexOfX(4.0), "Индекс x=4.0 должен быть -1");
        assertEquals(0, singleFunction.indexOfY(50.0), "Индекс y=50.0 должен быть 0");
        assertEquals(-1, singleFunction.indexOfY(40.0), "Индекс y=40.0 должен быть -1");
    }

    @Test
    void testNegativeValues() {
        double[] xValues = {-3.0, -2.0, -1.0, 0.0, 1.0, 2.0, 3.0};
        double[] yValues = {-30.0, -20.0, -10.0, 0.0, 10.0, 20.0, 30.0};
        TabulatedFunction function = new TestTabulatedFunction(xValues, yValues);

        assertEquals(-3.0, function.leftBound(), 0.0001, "Левая граница должна быть -3.0");
        assertEquals(3.0, function.rightBound(), 0.0001, "Правая граница должна быть 3.0");

        assertEquals(0, function.indexOfX(-3.0), "Индекс x=-3.0 должен быть 0");
        assertEquals(3, function.indexOfX(0.0), "Индекс x=0.0 должен быть 3");
        assertEquals(6, function.indexOfX(3.0), "Индекс x=3.0 должен быть 6");

        assertEquals(0, function.indexOfY(-30.0), "Индекс y=-30.0 должен быть 0");
        assertEquals(3, function.indexOfY(0.0), "Индекс y=0.0 должен быть 3");
        assertEquals(6, function.indexOfY(30.0), "Индекс y=30.0 должен быть 6");
    }

    @Test
    void testPrecisionInIndexOf() {
        double[] xValues = {1.0000000001, 2.0000000002, 3.0000000003};
        double[] yValues = {10.0000000001, 20.0000000002, 30.0000000003};
        TabulatedFunction function = new TestTabulatedFunction(xValues, yValues);

        // Проверка с учетом погрешности вычислений
        assertEquals(0, function.indexOfX(1.0000000001), "Должен найти x с учетом погрешности");
        assertEquals(1, function.indexOfX(2.0000000002), "Должен найти x с учетом погрешности");
        assertEquals(2, function.indexOfX(3.0000000003), "Должен найти x с учетом погрешности");

        assertEquals(0, function.indexOfY(10.0000000001), "Должен найти y с учетом погрешности");
        assertEquals(1, function.indexOfY(20.0000000002), "Должен найти y с учетом погрешности");
        assertEquals(2, function.indexOfY(30.0000000003), "Должен найти y с учетом погрешности");

        assertEquals(-1, function.indexOfX(1.0), "Не должен найти приблизительное значение x");
        assertEquals(-1, function.indexOfY(10.0), "Не должен найти приблизительное значение y");
    }
}