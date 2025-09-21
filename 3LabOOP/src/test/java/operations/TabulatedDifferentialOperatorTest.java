package operations;

import functions.factory.*;
import functions.Point;
import functions.TabulatedFunction;
import operations.TabulatedDifferentialOperator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class TabulatedDifferentialOperatorTest {

    private static final double DELTA = 1e-10;

    // ========== Фабрики для параметризованных тестов ==========
    private static Stream<TabulatedFunctionFactory> provideFactories() {
        return Stream.of(
                new ArrayTabulatedFunctionFactory(),
                new LinkedListTabulatedFunctionFactory()
        );
    }

    // ========== Тестовые данные ==========
    private static final double[] X_LINEAR = {0.0, 1.0, 2.0, 3.0, 4.0};
    private static final double[] Y_LINEAR = {1.0, 3.0, 5.0, 7.0, 9.0}; // f(x) = 2x + 1

    private static final double[] X_QUADRATIC = {0.0, 1.0, 2.0, 3.0, 4.0};
    private static final double[] Y_QUADRATIC = {0.0, 1.0, 4.0, 9.0, 16.0}; // f(x) = x^2

    // ========== Тест: линейная функция ==========
    @ParameterizedTest
    @MethodSource("provideFactories")
    @DisplayName("Производная линейной функции f(x) = 2x + 1 должна быть константой 2.0")
    void testLinearFunctionDerivative(TabulatedFunctionFactory factory) {
        TabulatedDifferentialOperator operator = new TabulatedDifferentialOperator(factory);
        TabulatedFunction linearFunction = factory.create(X_LINEAR, Y_LINEAR);
        TabulatedFunction derivative = operator.derive(linearFunction);

        assertEquals(X_LINEAR.length, derivative.getCount(), "Количество точек должно сохраняться");

        // Проверяем внутренние точки (центральная разность)
        for (int i = 1; i < derivative.getCount() - 1; i++) {
            assertEquals(2.0, derivative.getY(i), DELTA,
                    "Производная в точке x=" + X_LINEAR[i] + " должна быть 2.0");
        }

        // Проверяем граничные точки (левая/правая разность)
        if (derivative.getCount() > 1) {
            assertEquals(2.0, derivative.getY(0), DELTA, "Производная в первой точке должна быть 2.0");
            assertEquals(2.0, derivative.getY(derivative.getCount() - 1), DELTA,
                    "Производная в последней точке должна быть 2.0");
        }
    }

    // ========== Тест: квадратичная функция ==========
        @ParameterizedTest
        @MethodSource("provideFactories")
        @DisplayName("Производная квадратичной функции f(x) = x² должна быть f'(x) = 2x")
        void testQuadraticFunctionDerivative(TabulatedFunctionFactory factory) {
            TabulatedDifferentialOperator operator = new TabulatedDifferentialOperator(factory);
            TabulatedFunction quadraticFunction = factory.create(X_QUADRATIC, Y_QUADRATIC);
            TabulatedFunction derivative = operator.derive(quadraticFunction);

            assertEquals(X_QUADRATIC.length, derivative.getCount(), "Количество точек должно сохраняться");

        // Проверяем несколько точек, используя корректные индексы
        for (int i = 0; i < derivative.getCount(); i++) {
            double x = derivative.getX(i);
            double expected = 2 * x;
            assertEquals(expected, derivative.getY(i), DELTA,
                    "Производная в точке x=" + x + " должна быть " + expected);
        }
            double x = derivative.getX(0);
            double expected = 2 * x;
            assertEquals(expected, derivative.getY(0), DELTA, "Производная в точке x=" + x + " должна быть " + expected);

        }
/*        Для 1 точки нужно реализовать .remove() для TabulatedGunction
    // ========== Тест: одна точка ==========
    @ParameterizedTest
    @MethodSource("provideFactories")
    @DisplayName("Производная функции из одной точки должна быть 0")
    void testSinglePointFunction(TabulatedFunctionFactory factory) {
        TabulatedDifferentialOperator operator = new TabulatedDifferentialOperator(factory);
        TabulatedFunction singlePoint = factory.create(new double[]{5.0}, new double[]{10.0});
        TabulatedFunction derivative = operator.derive(singlePoint);

        assertEquals(1, derivative.getCount(), "Должна остаться одна точка");
        assertEquals(0.0, derivative.getY(0), DELTA, "Производная в единственной точке должна быть 0");
    }*/

    // ========== Тест: две точки ==========
    @ParameterizedTest
    @MethodSource("provideFactories")
    @DisplayName("Производная функции из двух точек должна быть постоянной (разностное приближение)")
    void testTwoPointFunction(TabulatedFunctionFactory factory) {
        double[] x = {1.0, 3.0};
        double[] y = {2.0, 6.0}; // f(x) = 2x → производная = 2

        TabulatedDifferentialOperator operator = new TabulatedDifferentialOperator(factory);
        TabulatedFunction function = factory.create(x, y);
        TabulatedFunction derivative = operator.derive(function);

        assertEquals(2, derivative.getCount(), "Должно быть две точки");

        double expectedDerivative = (y[1] - y[0]) / (x[1] - x[0]); // = 2.0

        assertEquals(expectedDerivative, derivative.getY(0), DELTA, "Производная в первой точке");
        assertEquals(expectedDerivative, derivative.getY(1), DELTA, "Производная во второй точке");
    }

    // ========== Тест: работа с фабриками ==========
    @Test
    @DisplayName("Проверка работы с разными фабриками")
    void testDifferentFactories() {
        double[] x = {0.0, 1.0, 2.0};
        double[] y = {0.0, 1.0, 4.0};

        // Array фабрика
        TabulatedFunctionFactory arrayFactory = new ArrayTabulatedFunctionFactory();
        TabulatedDifferentialOperator arrayOperator = new TabulatedDifferentialOperator(arrayFactory);
        TabulatedFunction derivative1 = arrayOperator.derive(arrayFactory.create(x, y));
        assertTrue(derivative1 instanceof functions.ArrayTabulatedFunction,
                "Должна создаваться ArrayTabulatedFunction");

        // LinkedList фабрика
        TabulatedFunctionFactory linkedListFactory = new LinkedListTabulatedFunctionFactory();
        TabulatedDifferentialOperator linkedListOperator = new TabulatedDifferentialOperator(linkedListFactory);
        TabulatedFunction derivative2 = linkedListOperator.derive(linkedListFactory.create(x, y));
        assertTrue(derivative2 instanceof functions.LinkedListTabulatedFunction,
                "Должна создаваться LinkedListTabulatedFunction");
    }

    // ========== Тест: конструктор по умолчанию ==========
    @Test
    @DisplayName("Конструктор по умолчанию должен использовать ArrayTabulatedFunctionFactory")
    void testDefaultConstructor() {
        TabulatedDifferentialOperator operator = new TabulatedDifferentialOperator();
        assertNotNull(operator.getFactory(), "Фабрика не должна быть null");
        assertTrue(operator.getFactory() instanceof ArrayTabulatedFunctionFactory,
                "По умолчанию должна использоваться ArrayTabulatedFunctionFactory");
    }

    // ========== Тест: сеттер и геттер фабрики ==========
    @Test
    @DisplayName("Проверка сеттера и геттера фабрики")
    void testFactorySetterGetter() {
        TabulatedDifferentialOperator operator = new TabulatedDifferentialOperator();
        TabulatedFunctionFactory newFactory = new LinkedListTabulatedFunctionFactory();

        operator.setFactory(newFactory);
        assertSame(newFactory, operator.getFactory(), "Геттер должен возвращать установленную фабрику");
    }

    // ========== Тест: исключение при null фабрике ==========
    @Test
    @DisplayName("Установка null фабрики должна вызывать IllegalArgumentException")
    void testSetNullFactory() {
        TabulatedDifferentialOperator operator = new TabulatedDifferentialOperator();

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> operator.setFactory(null),
                "Должно бросаться исключение при установке null фабрики"
        );

        assertNotNull(exception.getMessage());
        assertTrue(exception.getMessage().toLowerCase().contains("null"),
                "Сообщение исключения должно содержать упоминание null");
    }

    // ========== Вспомогательный метод для отладки (если нужно) ==========
    private void printFunction(TabulatedFunction function) {
        System.out.print("Точки: ");
        for (int i = 0; i < function.getCount(); i++) {
            System.out.printf("(%.2f, %.4f) ", function.getX(i), function.getY(i));
        }
        System.out.println();
    }
}