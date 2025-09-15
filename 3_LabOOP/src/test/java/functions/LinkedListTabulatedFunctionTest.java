package functions;

import exception.InterpolationException;
import exception.DifferentLengthOfArraysException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

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
    void testFloorNodeOfX() {
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
    void testOptimizedApply() {
        double[] xValues = {1.0, 2.0, 3.0, 4.0};
        double[] yValues = {10.0, 20.0, 30.0, 40.0};
        LinkedListTabulatedFunction function = new LinkedListTabulatedFunction(xValues, yValues);

        // Точные значения
        assertEquals(10.0, function.apply(1.0), DELTA, "apply(1.0)");
        assertEquals(20.0, function.apply(2.0), DELTA, "apply(2.0)");
        assertEquals(30.0, function.apply(3.0), DELTA, "apply(3.0)");
        assertEquals(40.0, function.apply(4.0), DELTA, "apply(4.0)");

        // Интерполяция
        assertEquals(15.0, function.apply(1.5), DELTA, "apply(1.5) интерполяция");
        assertEquals(25.0, function.apply(2.5), DELTA, "apply(2.5) интерполяция");
        assertEquals(35.0, function.apply(3.5), DELTA, "apply(3.5) интерполяция");

        // Экстраполяция
        assertEquals(0.0, function.apply(0.0), DELTA, "apply(0.0) экстраполяция");
        assertEquals(45.0, function.apply(4.5), DELTA, "apply(4.5) экстраполяция");
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



    //==============


    @Test
    public void testInsertEmptyList() {
        LinkedListTabulatedFunction list = new LinkedListTabulatedFunction(new double[]{}, new double[]{});
        assertEquals(0, list.getCount());

        list.insert(1.0, 2.0);
        assertEquals(1, list.getCount());
        assertEquals(1.0, list.getX(0));
        assertEquals(2.0, list.getY(0));
    }

    @Test
    public void testInsertAtBeginning() {
        LinkedListTabulatedFunction list = new LinkedListTabulatedFunction(new double[]{2.0, 4.0, 6.0}, new double[]{1.0, 2.0, 3.0});
        assertEquals(3, list.getCount());

        list.insert(1.0, 10.0);
        assertEquals(4, list.getCount());
        assertEquals(1.0, list.getX(0));
        assertEquals(10.0, list.getY(0));
        assertEquals(2.0, list.getX(1)); // остались в порядке
    }

    @Test
    public void testInsertInMiddle() {
        LinkedListTabulatedFunction list = new LinkedListTabulatedFunction(new double[]{1.0, 3.0, 5.0}, new double[]{1.0, 3.0, 5.0});
        list.insert(2.0, 2.0);
        assertEquals(4, list.getCount());
        assertEquals(1.0, list.getX(0));
        assertEquals(2.0, list.getX(1));
        assertEquals(3.0, list.getX(2));
        assertEquals(2.0, list.getY(1));
    }

    @Test
    public void testInsertAtEnd() {
        LinkedListTabulatedFunction list = new LinkedListTabulatedFunction(new double[]{1.0, 2.0, 3.0}, new double[]{1.0, 2.0, 3.0});
        list.insert(4.0, 4.0);
        assertEquals(4, list.getCount());
        assertEquals(4.0, list.getX(3));
        assertEquals(4.0, list.getY(3));
    }

    @Test
    public void testInsertDuplicateX() {
        LinkedListTabulatedFunction list = new LinkedListTabulatedFunction(new double[]{1.0, 2.0, 3.0}, new double[]{1.0, 2.0, 3.0});
        list.insert(2.0, 99.0); // замена
        assertEquals(3, list.getCount());
        assertEquals(99.0, list.getY(1)); // y обновился
    }
     @Test
     public void testInsertMultipleTimes() {
        LinkedListTabulatedFunction list = new LinkedListTabulatedFunction(new double[]{}, new double[]{});
        list.insert(5.0, 5.0);
        list.insert(3.0, 3.0);
        list.insert(7.0, 7.0);
        list.insert(4.0, 4.0);

        assertEquals(4, list.getCount());
        assertEquals(3.0, list.getX(0));
        assertEquals(4.0, list.getX(1));
        assertEquals(5.0, list.getX(2));
        assertEquals(7.0, list.getX(3));
    }

    @Test
    public void testInsertWithSmallEpsilon() {
        LinkedListTabulatedFunction list = new LinkedListTabulatedFunction(new double[]{1.0, 3.0}, new double[]{1.0, 3.0});
        list.insert(1.0 + 1e-11, 99.0); // почти совпадает — должно заменить
        assertEquals(2, list.getCount());
        assertEquals(99.0, list.getY(0)); // заменило первый элемент
    }

    @Test
    public void testRemoveSingleElement() {
        LinkedListTabulatedFunction list = new LinkedListTabulatedFunction(new double[]{5.0}, new double[]{10.0});
        assertEquals(1, list.getCount());
        assertEquals(5.0, list.getX(0));
        assertEquals(10.0, list.getY(0));

        list.remove(0);
        assertEquals(0, list.getCount());
        assertNull(list.getHead());
    }

    @Test
    public void testRemoveFirstElement() {
        LinkedListTabulatedFunction list = new LinkedListTabulatedFunction(
                new double[]{1.0, 2.0, 3.0},
                new double[]{10.0, 20.0, 30.0}
        );

        assertEquals(3, list.getCount());
        assertEquals(1.0, list.getX(0));
        assertEquals(2.0, list.getX(1));
        assertEquals(3.0, list.getX(2));

        list.remove(0);

        assertEquals(2, list.getCount());
        assertEquals(2.0, list.getX(0)); // теперь второй стал первым
        assertEquals(3.0, list.getX(1));
        assertEquals(20.0, list.getY(0));
        assertEquals(30.0, list.getY(1));

        assertEquals(2.0, list.leftBound()); // правильные границы
        assertEquals(3.0, list.rightBound());
    }

    @Test
    public void testRemoveMiddleElement() {
        LinkedListTabulatedFunction list = new LinkedListTabulatedFunction(
                new double[]{1.0, 2.0, 3.0, 4.0},
                new double[]{1.0, 2.0, 3.0, 4.0}
        );

        list.remove(2); // удаляем x=3.0

        assertEquals(3, list.getCount());
        assertEquals(1.0, list.getX(0));
        assertEquals(2.0, list.getX(1));
        assertEquals(4.0, list.getX(2));
        assertEquals(1.0, list.getY(0));
        assertEquals(2.0, list.getY(1));
        assertEquals(4.0, list.getY(2));

        assertEquals(1.0, list.leftBound());
        assertEquals(4.0, list.rightBound());
    }

    @Test
    public void testRemoveLastElement() {
        LinkedListTabulatedFunction list = new LinkedListTabulatedFunction(
                new double[]{1.0, 2.0, 3.0},
                new double[]{1.0, 2.0, 3.0}
        );

        list.remove(2); // удаляем последний

        assertEquals(2, list.getCount());
        assertEquals(1.0, list.getX(0));
        assertEquals(2.0, list.getX(1));
        assertEquals(1.0, list.leftBound());
        assertEquals(2.0, list.rightBound());

            // Проверим, что apply() продолжает работать
        assertEquals(1.5, list.apply(1.5), 1e-10); // интерполяция между 1.0 и 2.0
    }

    @Test
    public void testRemoveAllElementsOneByOne() {
        LinkedListTabulatedFunction list = new LinkedListTabulatedFunction(
                new double[]{1.0, 2.0, 3.0, 4.0},
                new double[]{1.0, 2.0, 3.0, 4.0}
        );

        for (int i = 0; i < 4; i++) {
            list.remove(0); // всегда удаляем первый
            assertEquals(3 - i, list.getCount());
        }

        assertNull(list.getHead());
        assertEquals(0, list.getCount());
    }

    @Test
    public void testRemoveAfterInsert() {
        LinkedListTabulatedFunction list = new LinkedListTabulatedFunction(new double[]{}, new double[]{});

        list.insert(1.0, 10.0);
        list.insert(3.0, 30.0);
        list.insert(2.0, 20.0); // вставится между 1.0 и 3.0 → [1.0, 2.0, 3.0]

        list.remove(1); // удаляем 2.0

        assertEquals(2, list.getCount());
        assertEquals(1.0, list.getX(0));
        assertEquals(3.0, list.getX(1));
        assertEquals(10.0, list.getY(0));
        assertEquals(30.0, list.getY(1));
    }

    @Test
    public void testRemoveOutOfBounds() {
        LinkedListTabulatedFunction list = new LinkedListTabulatedFunction(new double[]{1.0}, new double[]{1.0});

        assertThrows(IndexOutOfBoundsException.class, () -> list.remove(-1));
        assertThrows(IndexOutOfBoundsException.class, () -> list.remove(1));
        assertThrows(IndexOutOfBoundsException.class, () -> list.remove(2));

        list.remove(0); // удалили единственный элемент
        assertThrows(IndexOutOfBoundsException.class, () -> list.remove(0)); // пустой список
    }

    @Test
    public void testRemoveAndCheckApply() {
        LinkedListTabulatedFunction list = new LinkedListTabulatedFunction(
                new double[]{0.0, 1.0, 2.0, 3.0},
                new double[]{0.0, 1.0, 4.0, 9.0} // x^2
        );

        list.remove(1); // удаляем x=1.0 → остаются [0.0, 2.0, 3.0]

        assertEquals(3.0, list.apply(1.5), 1e-10);

        assertEquals(1.0, list.apply(0.5), 1e-10);

        assertEquals(6.5, list.apply(2.5), 1e-10);
    }
    @Test
    public void testFloorIndexOfX_EmptyList() {
        LinkedListTabulatedFunction list = new LinkedListTabulatedFunction(new double[]{}, new double[]{});
        assertThrows(IllegalArgumentException.class, () -> list.floorIndexOfX(1.0), "Пустой список должен бросать исключение");
    }

    @Test
    public void testExtrapolateLeft_CountOne() {
        LinkedListTabulatedFunction list = new LinkedListTabulatedFunction(new double[]{5.0}, new double[]{10.0});
        assertEquals(10.0, list.extrapolateLeft(3.0), DELTA, "Экстраполяция слева для одной точки");
        assertEquals(10.0, list.extrapolateRight(7.0), DELTA, "Экстраполяция справа для одной точки");
    }


    @Test
    public void testApply_ExactMatchOnHead() {
        double[] xValues = {1.0, 3.0, 5.0};
        double[] yValues = {10.0, 30.0, 50.0};
        LinkedListTabulatedFunction list = new LinkedListTabulatedFunction(xValues, yValues);

        assertEquals(10.0, list.apply(1.0), DELTA, "apply(1.0) — точное совпадение с head");
    }

    @Test
    public void testApply_ExactMatchOnLast() {
        double[] xValues = {1.0, 3.0, 5.0};
        double[] yValues = {10.0, 30.0, 50.0};
        LinkedListTabulatedFunction list = new LinkedListTabulatedFunction(xValues, yValues);

        assertEquals(50.0, list.apply(5.0), DELTA, "apply(5.0) — точное совпадение с последним");
    }

    @Test
    public void testFloorNodeOfX_ExactMatchOnHead() {
        double[] xValues = {1.0, 3.0, 5.0};
        double[] yValues = {10.0, 30.0, 50.0};
        LinkedListTabulatedFunction list = new LinkedListTabulatedFunction(xValues, yValues);

        LinkedListTabulatedFunction.Node node = list.floorNodeOfX(1.0);
        assertEquals(1.0, node.x, DELTA, "floorNodeOfX(1.0) должен вернуть head");
    }

    @Test
    public void testFloorNodeOfX_ExactMatchOnLast() {
        double[] xValues = {1.0, 3.0, 5.0};
        double[] yValues = {10.0, 30.0, 50.0};
        LinkedListTabulatedFunction list = new LinkedListTabulatedFunction(xValues, yValues);

        LinkedListTabulatedFunction.Node node = list.floorNodeOfX(5.0);
        assertEquals(5.0, node.x, DELTA, "floorNodeOfX(5.0) должен вернуть последний узел");
    }

    @Test
    public void testFloorNodeOfX_XGreaterThanOrEqualToLast() {
        double[] xValues = {1.0, 3.0, 5.0};
        double[] yValues = {10.0, 30.0, 50.0};
        LinkedListTabulatedFunction list = new LinkedListTabulatedFunction(xValues, yValues);

        LinkedListTabulatedFunction.Node node = list.floorNodeOfX(6.0);
        assertEquals(5.0, node.x, DELTA, "floorNodeOfX(6.0) должен вернуть последний узел");
    }

    @Test
    public void testFloorNodeOfX_XLessThanFirst() {
        double[] xValues = {1.0, 3.0, 5.0};
        double[] yValues = {10.0, 30.0, 50.0};
        LinkedListTabulatedFunction list = new LinkedListTabulatedFunction(xValues, yValues);

        LinkedListTabulatedFunction.Node node = list.floorNodeOfX(0.5);
        assertEquals(1.0, node.x, DELTA, "floorNodeOfX(0.5) должен вернуть head");
    }

    @Test
    public void testInsert_InsertAtEnd_CircularUpdate() {
        LinkedListTabulatedFunction list = new LinkedListTabulatedFunction(new double[]{1.0, 2.0}, new double[]{10.0, 20.0});
        list.insert(3.0, 30.0);

        // Проверим, что голова не сдвинулась, а хвост правильно связан
        assertEquals(3.0, list.getX(2));
        assertEquals(1.0, list.getX(0)); // head не изменился
        assertEquals(2.0, list.getX(1));
        assertEquals(3.0, list.rightBound());
    }

    @Test
    public void testInsert_InsertDuplicateWithEpsilon() {
        LinkedListTabulatedFunction list = new LinkedListTabulatedFunction(new double[]{1.0}, new double[]{10.0});
        list.insert(1.0 + 1e-9, 99.0); // должно заменить, т.к. |1.0 - 1.000000001| < 1e-10?

        // ВАЖНО: в коде используется 1e-10, а здесь 1e-9 — это больше!
        // Значит, НЕ должно заменить! Создаст новую точку → станет 2 точки!

        assertEquals(2, list.getCount(), "1e-9 > 1e-10 — должно быть добавлено как новая точка");
        assertEquals(1.0, list.getX(0));
        assertEquals(10.0, list.getY(0));
        assertEquals(1.000000001, list.getX(1), DELTA);
        assertEquals(99.0, list.getY(1));
    }

    @Test
    public void testInsert_InsertDuplicateWithinEpsilon() {
        LinkedListTabulatedFunction list = new LinkedListTabulatedFunction(new double[]{1.0}, new double[]{10.0});
        list.insert(1.0 + 1e-11, 99.0); // |1.0 - 1.00000000001| < 1e-10 → true

        assertEquals(1, list.getCount(), "Должно заменить существующий элемент");
        assertEquals(99.0, list.getY(0), DELTA);
    }
/*
    @Test
    public void testGetNode_LeftHalf() {
        double[] xValues = {1.0, 2.0, 3.0, 4.0, 5.0}; // count=5, index=2 (middle) → left half
        double[] yValues = {10.0, 20.0, 30.0, 40.0, 50.0};
        LinkedListTabulatedFunction list = new LinkedListTabulatedFunction(xValues, yValues);

        Node node = list.getNode(1); // индекс 1 < 5/2=2.5 → левая половина
        assertEquals(2.0, node.x, DELTA);
    }

    @Test
    public void testGetNode_RightHalf() {
        double[] xValues = {1.0, 2.0, 3.0, 4.0, 5.0}; // count=5, index=3 > 2.5 → правая половина
        double[] yValues = {10.0, 20.0, 30.0, 40.0, 50.0};
        LinkedListTabulatedFunction list = new LinkedListTabulatedFunction(xValues, yValues);

        Node node = list.getNode(3); // индекс 3 >= 2.5 → правая половина
        assertEquals(4.0, node.x, DELTA);
    }
    getNode() - приватный, поэтому не тестируем его, не ломаем инкапсуляцию
*/
    @Test
    public void testIndexOfY_Found() {
        double[] xValues = {1.0, 2.0, 3.0};
        double[] yValues = {10.0, 20.0, 30.0};
        LinkedListTabulatedFunction list = new LinkedListTabulatedFunction(xValues, yValues);

        assertEquals(1, list.indexOfY(20.0), "indexOfY(20.0) должен найти индекс 1");
    }

    @Test
    public void testIndexOfY_NotFound() {
        double[] xValues = {1.0, 2.0, 3.0};
        double[] yValues = {10.0, 20.0, 30.0};
        LinkedListTabulatedFunction list = new LinkedListTabulatedFunction(xValues, yValues);

        assertEquals(-1, list.indexOfY(25.0), "indexOfY(25.0) не найден");
    }

    @Test
    public void testIndexOfY_EpsilonMatch() {
        double[] xValues = {1.0, 2.0, 3.0};
        double[] yValues = {10.0, 20.0, 30.0};
        LinkedListTabulatedFunction list = new LinkedListTabulatedFunction(xValues, yValues);

        assertEquals(1, list.indexOfY(20.0 + 1e-11), "indexOfY с погрешностью 1e-11 должен найти");
        assertEquals(-1, list.indexOfY(20.0 + 1e-9), "indexOfY с погрешностью 1e-9 не должен найти (больше 1e-10)");
    }

    @Test
    public void testLeftBound_EmptyList() {
        LinkedListTabulatedFunction list = new LinkedListTabulatedFunction(new double[]{}, new double[]{});
        assertThrows(IllegalArgumentException.class, () -> list.leftBound(), "Пустой список при leftBound()");
    }

    @Test
    public void testRightBound_EmptyList() {
        LinkedListTabulatedFunction list = new LinkedListTabulatedFunction(new double[]{}, new double[]{});
        assertThrows(IllegalArgumentException.class, () -> list.rightBound(), "Пустой список при rightBound()");
    }

    @Test
    public void testConstructor_ArraysDifferentLength() {
        assertThrows(DifferentLengthOfArraysException.class, () ->
                new LinkedListTabulatedFunction(new double[]{1.0, 2.0, 3.0}, new double[]{10.0, 9.0}), "Разные длины массивов");
    }


    @Test
    public void testApply_ExtrapolateLeft_CountOne() {
        LinkedListTabulatedFunction list = new LinkedListTabulatedFunction(new double[]{5.0}, new double[]{10.0});
        assertEquals(10.0, list.apply(4.0), DELTA, "apply(4.0) при одной точке — экстраполяция слева");
    }

    @Test
    public void testApply_ExtrapolateRight_CountOne() {
        LinkedListTabulatedFunction list = new LinkedListTabulatedFunction(new double[]{5.0}, new double[]{10.0});
        assertEquals(10.0, list.apply(6.0), DELTA, "apply(6.0) при одной точке — экстраполяция справа");
    }

    @Test
    public void testApply_Interpolate_BoundaryCase() {
        double[] xValues = {1.0, 2.0};
        double[] yValues = {10.0, 20.0};
        LinkedListTabulatedFunction list = new LinkedListTabulatedFunction(xValues, yValues);

        // Проверим границу: x = 1.0 (head) и x = 2.0 (last)
        assertEquals(10.0, list.apply(1.0), DELTA);
        assertEquals(20.0, list.apply(2.0), DELTA);

        // Проверим интерполяцию на границе между ними
        assertEquals(15.0, list.apply(1.5), DELTA);
    }

    @Test
    public void testRemove_HeadIsUpdatedAfterRemoval() {
        LinkedListTabulatedFunction list = new LinkedListTabulatedFunction(
                new double[]{1.0, 2.0, 3.0},
                new double[]{10.0, 20.0, 30.0}
        );

        list.remove(0); // удаляем head

        assertNotNull(list.getHead());
        assertEquals(2.0, list.getHead().x, DELTA, "После удаления head, новый head должен быть 2.0");
    }

    @Test
    public void testInsert_InsertAfterHeadInCircular() {
        LinkedListTabulatedFunction list = new LinkedListTabulatedFunction(new double[]{1.0}, new double[]{10.0});
        list.insert(2.0, 20.0);
        list.insert(0.5, 5.0); // вставить перед head

        assertEquals(3, list.getCount());
        assertEquals(0.5, list.getX(0));
        assertEquals(1.0, list.getX(1));
        assertEquals(2.0, list.getX(2));

        assertEquals(5.0, list.getY(0));
        assertEquals(10.0, list.getY(1));
        assertEquals(20.0, list.getY(2));
    }

    @Test
    public void testApply_WithSingleNodeAndEpsilon() {
        LinkedListTabulatedFunction list = new LinkedListTabulatedFunction(new double[]{1.0}, new double[]{10.0});
        assertEquals(10.0, list.apply(1.0 + 1e-11), DELTA, "apply(1.0 + 1e-11) — должно вернуть 10.0 (точное совпадение по эпсилон)");
    }

    @Test
    void testLinkedListWithLinkedList(){
        //f(x) = x + 1
        double[] x1 = {0.0, 1.0, 2.0, 3.0, 4.0};
        double[] y1 = {1.0, 2.0, 3.0, 4.0, 5.0};
        LinkedListTabulatedFunction f = new LinkedListTabulatedFunction(x1, y1);

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
    void testLinkedListWithLinkedListAndThen(){
        //f(x) = x + 1
        double[] x1 = {0.0, 1.0, 2.0, 3.0, 4.0};
        double[] y1 = {1.0, 2.0, 3.0, 4.0, 5.0};
        LinkedListTabulatedFunction f = new LinkedListTabulatedFunction(x1, y1);

        //g(x) = 2x + 3
        double[] x2 = {0.0, 1.0, 2.0, 3.0, 4.0};
        double[] y2 = {3.0, 5.0, 7.0, 9.0, 11.0};
        LinkedListTabulatedFunction g = new LinkedListTabulatedFunction(x2, y2);

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
    void testLinkedListWithLinkedListExponentialWithLogarithmicComposition() {
        //f(x) = x + 1
        double[] x1 = {0.0, 0.5, 1.0, 1.5, 2.0};
        double[] y1 = {1.0, 1.5, 2.0, 2.5, 3.0};
        LinkedListTabulatedFunction f = new LinkedListTabulatedFunction(x1, y1);

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
    void testLinkedListWithLinkedListTrigonometricComposition() {
        //f(x) = sin(x)
        double[] x1 = {0.0, Math.PI/6, Math.PI/4, Math.PI/3, Math.PI/2};
        double[] y1 = {0.0, 0.5, 0.7071, 0.8660, 1.0};
        LinkedListTabulatedFunction f = new LinkedListTabulatedFunction(x1, y1);

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
    void testLinkedListWithLinkedListCompositeWithExtrapolation() {
        //f(x) = √x
        double[] x1 = {0.0, 1.0, 2.0, 3.0, 4.0};
        double[] y1 = {0.0, 1.0, 1.4142, 1.7321, 2.0};
        LinkedListTabulatedFunction f = new LinkedListTabulatedFunction(x1, y1);

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

    void testLinkedListWithArray(){
        //f(x) = x + 1
        double[] x1 = {0.0, 1.0, 2.0, 3.0, 4.0};
        double[] y1 = {1.0, 2.0, 3.0, 4.0, 5.0};
        LinkedListTabulatedFunction f = new LinkedListTabulatedFunction(x1, y1);

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
    void testLinkedListWithArrayExponentialWithLogarithmicComposition() {
        //f(x) = x + 1
        double[] x1 = {0.0, 0.5, 1.0, 1.5, 2.0};
        double[] y1 = {1.0, 1.5, 2.0, 2.5, 3.0};
        LinkedListTabulatedFunction f = new LinkedListTabulatedFunction(x1, y1);

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
    void testLinkedListWithArrayTrigonometricComposition() {
        //f(x) = sin(x)
        double[] x1 = {0.0, Math.PI/6, Math.PI/4, Math.PI/3, Math.PI/2};
        double[] y1 = {0.0, 0.5, 0.7071, 0.8660, 1.0};
        LinkedListTabulatedFunction f = new LinkedListTabulatedFunction(x1, y1);

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
    void testLinkedListWithArrayCompositeWithExtrapolation() {
        //f(x) = √x
        double[] x1 = {0.0, 1.0, 2.0, 3.0, 4.0};
        double[] y1 = {0.0, 1.0, 1.4142, 1.7321, 2.0};
        LinkedListTabulatedFunction f = new LinkedListTabulatedFunction(x1, y1);

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


    private LinkedListTabulatedFunction createList(double[] x, double[] y) {
        return new LinkedListTabulatedFunction(x, y);
    }

        // ========================
        // Тесты: интерполяция внутри интервала (должна работать)
        // ========================

    @Test
    void testInterpolate_SimpleLinearCase() {
            // Точки: (1, 1), (3, 5) → линейная функция: y = 2x - 1
        double[] x = {1.0, 3.0};
        double[] y = {1.0, 5.0};
        LinkedListTabulatedFunction list = createList(x, y);

            // Интерполяция при x=2.0 → должно быть (1+5)/2 = 3.0
        double result = list.interpolate(2.0, 0); // floorIndex=0, т.к. 1.0 <= 2.0 < 3.0
        assertEquals(3.0, result, 1e-10, "Интерполяция между (1,1) и (3,5) при x=2.0 должна дать 3.0");
    }

    @Test
    void testInterpolate_QuadraticBehavior() {
            // Точки: (0,0), (2,4), (4,16) — как y=x²
        double[] x = {0.0, 2.0, 4.0};
        double[] y = {0.0, 4.0, 16.0};
        LinkedListTabulatedFunction list = createList(x, y);

            // Интерполируем между (0,0) и (2,4): x=1.0 → ожидаем 2.0
        double res1 = list.interpolate(1.0, 0);
        assertEquals(2.0, res1, 1e-10, "x=1.0 между [0,2]: (0→4) → линейно: 2.0");

            // Между (2,4) и (4,16): x=3.0 → ожидаем 10.0
        double res2 = list.interpolate(3.0, 1);
        assertEquals(10.0, res2, 1e-10, "x=3.0 между [2,4]: (4→16) → линейно: 10.0");
    }

    @Test
    void testInterpolate_AtExactPoint() {
            // Если x совпадает с узлом — должен вернуть y этого узла
        double[] x = {1.0, 2.0, 3.0};
        double[] y = {10.0, 20.0, 30.0};
        LinkedListTabulatedFunction list = createList(x, y);

            // x=2.0 — это точный узел, но мы вызываем interpolate с floorIndex=1
            // Это НЕ ожидается в нормальном use-case, но метод должен корректно обработать
        double result = list.interpolate(2.0, 1); // x == x[1]
        assertEquals(20.0, result, 1e-10, "Если x == x[i], интерполяция должна вернуть y[i]");
    }

        // ========================
        // Тесты: InterpolationException — x вне интервала
        // ========================


    @Test
    void testInterpolate_XAboveRightBound_ThrowsException() {
        double[] x = {1.0, 3.0, 5.0};
        double[] y = {1.0, 9.0, 25.0};
        LinkedListTabulatedFunction list = createList(x, y);

            // Пытаемся интерполировать между 3.0 и 5.0 (floorIndex=1), но x=6.0 — выше правого
        assertThrows(InterpolationException.class,
                () -> list.interpolate(6.0, 1),
                "Должен бросить InterpolationException, если x > x[floorIndex + 1]");
        }
}
