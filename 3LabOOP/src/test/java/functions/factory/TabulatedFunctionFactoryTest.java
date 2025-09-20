package functions.factory;

import functions.TabulatedFunction;
import functions.ArrayTabulatedFunction;
import functions.LinkedListTabulatedFunction;
import functions.StrictTabulatedFunction;

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


    @Test
    @DisplayName("ArrayTabulatedFunctionFactory.createStrict должен создавать StrictTabulatedFunction")
    void testArrayFactoryCreateStrict() {
        TabulatedFunctionFactory factory = new ArrayTabulatedFunctionFactory();
        double[] xValues = {1.0, 2.0, 3.0};
        double[] yValues = {10.0, 20.0, 30.0};

        TabulatedFunction function = factory.createStrict(xValues, yValues);

        assertNotNull(function);
        assertTrue(function instanceof StrictTabulatedFunction);
        assertEquals(3, function.getCount());
        assertEquals(1.0, function.getX(0), 0.0001);
        assertEquals(10.0, function.getY(0), 0.0001);
    }

    @Test
    @DisplayName("LinkedListTabulatedFunctionFactory.createStrict должен создавать StrictTabulatedFunction")
    void testLinkedListFactoryCreateStrict() {
        TabulatedFunctionFactory factory = new LinkedListTabulatedFunctionFactory();
        double[] xValues = {1.0, 2.0, 3.0};
        double[] yValues = {10.0, 20.0, 30.0};

        TabulatedFunction function = factory.createStrict(xValues, yValues);

        assertNotNull(function);
        assertTrue(function instanceof StrictTabulatedFunction);
        assertEquals(3, function.getCount());
        assertEquals(1.0, function.getX(0), 0.0001);
        assertEquals(10.0, function.getY(0), 0.0001);
    }

    @Test
    @DisplayName("createStrict должен запрещать интерполяцию для ArrayFactory")
    void testArrayFactoryCreateStrictNoInterpolation() {
        TabulatedFunctionFactory factory = new ArrayTabulatedFunctionFactory();
        double[] xValues = {1.0, 2.0, 3.0};
        double[] yValues = {10.0, 20.0, 30.0};

        TabulatedFunction function = factory.createStrict(xValues, yValues);

        assertEquals(10.0, function.apply(1.0), 0.0001);
        assertEquals(20.0, function.apply(2.0), 0.0001);

        assertThrows(UnsupportedOperationException.class, () -> function.apply(1.5));
        assertThrows(UnsupportedOperationException.class, () -> function.apply(0.5));
        assertThrows(UnsupportedOperationException.class, () -> function.apply(3.5));
    }

    @Test
    @DisplayName("createStrict должен запрещать интерполяцию для LinkedListFactory")
    void testLinkedListFactoryCreateStrictNoInterpolation() {
        TabulatedFunctionFactory factory = new LinkedListTabulatedFunctionFactory();
        double[] xValues = {1.0, 2.0, 3.0};
        double[] yValues = {10.0, 20.0, 30.0};

        TabulatedFunction function = factory.createStrict(xValues, yValues);

        assertEquals(10.0, function.apply(1.0), 0.0001);
        assertEquals(20.0, function.apply(2.0), 0.0001);

        assertThrows(UnsupportedOperationException.class, () -> function.apply(1.5));
        assertThrows(UnsupportedOperationException.class, () -> function.apply(0.5));
        assertThrows(UnsupportedOperationException.class, () -> function.apply(3.5));
    }

    @Test
    @DisplayName("createStrict должен сохранять независимость от исходных массивов")
    void testCreateStrictIndependenceFromArrays() {
        TabulatedFunctionFactory factory = new ArrayTabulatedFunctionFactory();
        double[] xValues = {1.0, 2.0, 3.0};
        double[] yValues = {10.0, 20.0, 30.0};

        TabulatedFunction function = factory.createStrict(xValues, yValues);

        xValues[0] = 100.0;
        yValues[0] = 200.0;

        assertEquals(1.0, function.getX(0), 0.0001);
        assertEquals(10.0, function.getY(0), 0.0001);
    }

    @Test
    @DisplayName("createStrict с большими массивами для ArrayFactory")
    void testArrayFactoryCreateStrictWithManyElements() {
        TabulatedFunctionFactory factory = new ArrayTabulatedFunctionFactory();
        double[] xValues = {1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0, 10.0};
        double[] yValues = {1.0, 4.0, 9.0, 16.0, 25.0, 36.0, 49.0, 64.0, 81.0, 100.0};

        TabulatedFunction function = factory.createStrict(xValues, yValues);

        assertNotNull(function);
        assertTrue(function instanceof StrictTabulatedFunction);
        assertEquals(10, function.getCount());
        assertEquals(10.0, function.getX(9), 0.0001);
        assertEquals(100.0, function.getY(9), 0.0001);
    }

    @Test
    @DisplayName("createStrict с большими массивами для LinkedListFactory")
    void testLinkedListFactoryCreateStrictWithManyElements() {
        TabulatedFunctionFactory factory = new LinkedListTabulatedFunctionFactory();
        double[] xValues = {1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0, 10.0};
        double[] yValues = {1.0, 4.0, 9.0, 16.0, 25.0, 36.0, 49.0, 64.0, 81.0, 100.0};

        TabulatedFunction function = factory.createStrict(xValues, yValues);

        assertNotNull(function);
        assertTrue(function instanceof StrictTabulatedFunction);
        assertEquals(10, function.getCount());
        assertEquals(10.0, function.getX(9), 0.0001);
        assertEquals(100.0, function.getY(9), 0.0001);
    }

    @Test
    @DisplayName("createStrict должен разрешать модификацию Y значений")
    void testCreateStrictAllowsSetY() {
        TabulatedFunctionFactory factory = new ArrayTabulatedFunctionFactory();
        double[] xValues = {1.0, 2.0, 3.0};
        double[] yValues = {10.0, 20.0, 30.0};

        TabulatedFunction function = factory.createStrict(xValues, yValues);

        function.setY(1, 25.0);
        assertEquals(25.0, function.getY(1), 0.0001);
        assertEquals(25.0, function.apply(2.0), 0.0001);
    }

    @Test
    @DisplayName("ArrayTabulatedFunctionFactory.createStrictUnmodifiable создает неизменяемую строгую функцию")
    void testArrayFactoryCreateStrictUnmodifiable() {
        double[] xValues = {1.0, 2.0, 3.0, 4.0};
        double[] yValues = {10.0, 20.0, 30.0, 40.0};

        TabulatedFunctionFactory factory = new ArrayTabulatedFunctionFactory();

        TabulatedFunction function = factory.createStrictUnmodifiable(xValues, yValues);

        assertNotNull(function);
        assertEquals(4, function.getCount());

        assertEquals(1.0, function.getX(0), 1e-10);
        assertEquals(10.0, function.getY(0), 1e-10);
        assertEquals(4.0, function.getX(3), 1e-10);
        assertEquals(40.0, function.getY(3), 1e-10);
    }

    @Test
    @DisplayName("LinkedListTabulatedFunctionFactory.createStrictUnmodifiable создает неизменяемую строгую функцию")
    void testLinkedListFactoryCreateStrictUnmodifiable() {
        double[] xValues = {1.0, 2.0, 3.0};
        double[] yValues = {10.0, 20.0, 30.0};

        TabulatedFunctionFactory factory = new LinkedListTabulatedFunctionFactory();

        TabulatedFunction function = factory.createStrictUnmodifiable(xValues, yValues);

        assertNotNull(function);
        assertEquals(3, function.getCount());

        assertEquals(1.0, function.getX(0), 1e-10);
        assertEquals(10.0, function.getY(0), 1e-10);
    }

    @Test
    @DisplayName("createStrictUnmodifiable должен запрещать интерполяцию")
    void testCreateStrictUnmodifiableNoInterpolation() {
        double[] xValues = {1.0, 2.0, 3.0};
        double[] yValues = {10.0, 20.0, 30.0};

        TabulatedFunctionFactory factory = new ArrayTabulatedFunctionFactory();
        TabulatedFunction function = factory.createStrictUnmodifiable(xValues, yValues);

        // Должен работать для существующих точек
        assertEquals(10.0, function.apply(1.0), 1e-10);
        assertEquals(20.0, function.apply(2.0), 1e-10);

        // Должен бросать исключение для несуществующих точек
        assertThrows(UnsupportedOperationException.class, () -> function.apply(1.5));
        assertThrows(UnsupportedOperationException.class, () -> function.apply(0.5));
        assertThrows(UnsupportedOperationException.class, () -> function.apply(4.5));
    }

    @Test
    @DisplayName("createStrictUnmodifiable должен запрещать модификацию Y значений")
    void testCreateStrictUnmodifiableNoModification() {
        double[] xValues = {1.0, 2.0, 3.0};
        double[] yValues = {10.0, 20.0, 30.0};

        TabulatedFunctionFactory factory = new ArrayTabulatedFunctionFactory();
        TabulatedFunction function = factory.createStrictUnmodifiable(xValues, yValues);

        // Попытка изменить значение должна бросать исключение
        assertThrows(UnsupportedOperationException.class, () -> {
            function.setY(1, 25.0);
        });

        // Проверяем, что значение не изменилось
        assertEquals(20.0, function.getY(1), 1e-10);
    }

    @Test
    @DisplayName("createStrictUnmodifiable должен быть полностью неизменяемым")
    void testCreateStrictUnmodifiableCompleteImmutability() {
        double[] xValues = {1.0, 2.0, 3.0};
        double[] yValues = {10.0, 20.0, 30.0};

        TabulatedFunctionFactory factory = new ArrayTabulatedFunctionFactory();
        TabulatedFunction function = factory.createStrictUnmodifiable(xValues, yValues);

        // Все операции модификации должны бросать исключение
        assertThrows(UnsupportedOperationException.class, () -> function.setY(0, 100.0));

        // Данные должны остаться неизменными
        assertEquals(10.0, function.getY(0), 1e-10);
        assertEquals(20.0, function.getY(1), 1e-10);
    }

    @Test
    @DisplayName("createStrictUnmodifiable должен сохранять независимость от исходных массивов")
    void testCreateStrictUnmodifiableIndependence() {
        TabulatedFunctionFactory factory = new ArrayTabulatedFunctionFactory();
        double[] xValues = {1.0, 2.0, 3.0};
        double[] yValues = {10.0, 20.0, 30.0};

        TabulatedFunction function = factory.createStrictUnmodifiable(xValues, yValues);

        // Изменяем исходные массивы
        xValues[0] = 100.0;
        yValues[0] = 200.0;

        // Функция должна остаться неизменной
        assertEquals(1.0, function.getX(0), 1e-10);
        assertEquals(10.0, function.getY(0), 1e-10);
    }

    @Test
    @DisplayName("createStrictUnmodifiable итератор должен быть неизменяемым")
    void testCreateStrictUnmodifiableIterator() {
        double[] xValues = {1.0, 2.0, 3.0};
        double[] yValues = {10.0, 20.0, 30.0};

        TabulatedFunctionFactory factory = new ArrayTabulatedFunctionFactory();
        TabulatedFunction function = factory.createStrictUnmodifiable(xValues, yValues);

        // Итератор должен работать для чтения
        int count = 0;
        for (var point : function) {
            assertNotNull(point);
            count++;
        }
        assertEquals(3, count);

        // Итератор должен запрещать remove
        var iterator = function.iterator();
        iterator.next(); // Переходим к первому элементу
        assertThrows(UnsupportedOperationException.class, () -> iterator.remove());
    }

    @Test
    @DisplayName("createStrictUnmodifiable должен обладать свойствами обеих оберток")
    void testCreateStrictUnmodifiableCombinedProperties() {
        double[] xValues = {1.0, 2.0, 3.0};
        double[] yValues = {10.0, 20.0, 30.0};

        TabulatedFunctionFactory factory = new ArrayTabulatedFunctionFactory();
        TabulatedFunction function = factory.createStrictUnmodifiable(xValues, yValues);

        // Свойство Strict: запрет интерполяции
        assertThrows(UnsupportedOperationException.class, () -> function.apply(1.5));

        // Свойство Unmodifiable: запрет модификации
        assertThrows(UnsupportedOperationException.class, () -> function.setY(0, 100.0));

        // Свойство обеих: чтение данных работает
        assertEquals(1.0, function.getX(0), 1e-10);
        assertEquals(10.0, function.getY(0), 1e-10);
        assertEquals(10.0, function.apply(1.0), 1e-10);
    }

    @Test
    @DisplayName("Сравнение create, createStrict и createStrictUnmodifiable")
    void testCompareAllCreateMethods() {
        double[] xValues = {1.0, 2.0, 3.0};
        double[] yValues = {10.0, 20.0, 30.0};

        TabulatedFunctionFactory factory = new ArrayTabulatedFunctionFactory();

        TabulatedFunction normal = factory.create(xValues, yValues);
        TabulatedFunction strict = factory.createStrict(xValues, yValues);
        TabulatedFunction strictUnmodifiable = factory.createStrictUnmodifiable(xValues, yValues);

        // Все должны иметь одинаковые данные
        for (int i = 0; i < xValues.length; i++) {
            assertEquals(normal.getX(i), strict.getX(i), 1e-10);
            assertEquals(normal.getX(i), strictUnmodifiable.getX(i), 1e-10);
            assertEquals(normal.getY(i), strict.getY(i), 1e-10);
            assertEquals(normal.getY(i), strictUnmodifiable.getY(i), 1e-10);
        }

        // Разное поведение при интерполяции
        assertEquals(15.0, normal.apply(1.5), 1e-10); // Интерполяция работает
        assertThrows(UnsupportedOperationException.class, () -> strict.apply(1.5)); // Только strict
        assertThrows(UnsupportedOperationException.class, () -> strictUnmodifiable.apply(1.5)); // Strict + Unmodifiable

        // Разное поведение при модификации
        normal.setY(0, 100.0); // Модификация работает
        strict.setY(0, 100.0); // Модификация работает (только strict)
        assertThrows(UnsupportedOperationException.class, () -> strictUnmodifiable.setY(0, 100.0)); // Запрещено
    }

}