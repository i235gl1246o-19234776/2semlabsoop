package functions.factory;

import functions.TabulatedFunction;
import functions.ArrayTabulatedFunction;
import functions.LinkedListTabulatedFunction;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

class TabulatedFunctionFactoryTest {

    @Test
    @DisplayName("ArrayTabulatedFunctionFactory должен создавать объекты ArrayTabulatedFunction")
    void testArrayTabulatedFunctionFactory() {
        TabulatedFunctionFactory factory = new ArrayTabulatedFunctionFactory();
        double[] xValues = {1.0, 2.0, 3.0};
        double[] yValues = {10.0, 20.0, 30.0};

        TabulatedFunction function = factory.create(xValues, yValues);

        assertNotNull(function);
        assertTrue(function instanceof ArrayTabulatedFunction);
        assertEquals(3, function.getCount());
        assertEquals(1.0, function.getX(0), 0.0001);
        assertEquals(10.0, function.getY(0), 0.0001);
    }

    @Test
    @DisplayName("LinkedListTabulatedFunctionFactory должен создавать объекты LinkedListTabulatedFunction")
    void testLinkedListTabulatedFunctionFactory() {
        TabulatedFunctionFactory factory = new LinkedListTabulatedFunctionFactory();
        double[] xValues = {1.0, 2.0, 3.0};
        double[] yValues = {10.0, 20.0, 30.0};

        TabulatedFunction function = factory.create(xValues, yValues);

        assertNotNull(function);
        assertTrue(function instanceof LinkedListTabulatedFunction);
        assertEquals(3, function.getCount());
        assertEquals(1.0, function.getX(0), 0.0001);
        assertEquals(10.0, function.getY(0), 0.0001);
    }

    @Test
    @DisplayName("ArrayTabulatedFunctionFactory должен создавать функцию с большим количеством элементов")
    void testArrayFactoryWithManyElements() {
        TabulatedFunctionFactory factory = new ArrayTabulatedFunctionFactory();
        double[] xValues = {1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0, 10.0};
        double[] yValues = {1.0, 4.0, 9.0, 16.0, 25.0, 36.0, 49.0, 64.0, 81.0, 100.0};

        TabulatedFunction function = factory.create(xValues, yValues);

        assertNotNull(function);
        assertTrue(function instanceof ArrayTabulatedFunction);
        assertEquals(10, function.getCount());
        assertEquals(10.0, function.getX(9), 0.0001);
        assertEquals(100.0, function.getY(9), 0.0001);
    }

    @Test
    @DisplayName("LinkedListTabulatedFunctionFactory должен создавать функцию с большим количеством элементов")
    void testLinkedListFactoryWithManyElements() {
        TabulatedFunctionFactory factory = new LinkedListTabulatedFunctionFactory();
        double[] xValues = {1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0, 10.0};
        double[] yValues = {1.0, 4.0, 9.0, 16.0, 25.0, 36.0, 49.0, 64.0, 81.0, 100.0};

        TabulatedFunction function = factory.create(xValues, yValues);

        assertNotNull(function);
        assertTrue(function instanceof LinkedListTabulatedFunction);
        assertEquals(10, function.getCount());
        assertEquals(10.0, function.getX(9), 0.0001);
        assertEquals(100.0, function.getY(9), 0.0001);
    }

    @Test
    @DisplayName("Созданные функции должны быть независимыми от исходных массивов")
    void testFunctionIndependenceFromArrays() {
        TabulatedFunctionFactory factory = new ArrayTabulatedFunctionFactory();
        double[] xValues = {1.0, 2.0, 3.0};
        double[] yValues = {10.0, 20.0, 30.0};

        TabulatedFunction function = factory.create(xValues, yValues);

        xValues[0] = 100.0;
        yValues[0] = 200.0;

        assertEquals(1.0, function.getX(0), 0.0001);
        assertEquals(10.0, function.getY(0), 0.0001);
    }

}