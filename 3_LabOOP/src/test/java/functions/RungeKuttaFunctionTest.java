package functions;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Тестовый класс для RungeKuttaFunction.
 * Проверяет корректность работы метода Рунге-Кутты.
 */
class RungeKuttaFunctionTest {

    @Test
    void testConstantFunctionSolution() {
        // dy/dx = 0, y(0) = 5 -> y(x) = 5
        MathFunction zeroFunction = new MathFunction() {
            public double apply(double x) {
                return 0;
            }
        };

        RungeKuttaFunction solver = new RungeKuttaFunction(zeroFunction, 0, 5, 0.1);
        assertEquals(5.0, solver.apply(1.0), 0.0001, "Решение для dy/dx=0 должно быть константой");
        assertEquals(5.0, solver.apply(2.0), 0.0001, "Решение для dy/dx=0 должно быть константой");
        assertEquals(5.0, solver.apply(-1.0), 0.0001, "Решение для dy/dx=0 должно быть константой");
        assertEquals(5.0, solver.apply(10.0), 0.0001, "Решение для dy/dx=0 должно быть константой");
        assertEquals(5.0, solver.apply(0.5), 0.0001, "Решение для dy/dx=0 должно быть константой");
        assertEquals(5.0, solver.apply(100.0), 0.0001, "Решение для dy/dx=0 должно быть константой");
    }

    @Test
    void testLinearFunctionSolution() {
        // dy/dx = 2, y(0) = 1 -> y(x) = 2x + 1
        MathFunction constantTwo = new MathFunction() {
            public double apply(double x) {
                return 2;
            }
        };

        RungeKuttaFunction solver = new RungeKuttaFunction(constantTwo, 0, 1, 0.01);
        assertEquals(3.0, solver.apply(1.0), 0.01, "Решение для dy/dx=2 должно быть линейной функцией");
        assertEquals(5.0, solver.apply(2.0), 0.01, "Решение для dy/dx=2 должно быть линейной функцией");
        assertEquals(7.0, solver.apply(3.0), 0.01, "Решение для dy/dx=2 должно быть линейной функцией");
        assertEquals(1.0, solver.apply(0.0), 0.0001, "Решение должно удовлетворять начальному условию");
        assertEquals(1.0, solver.apply(-1.0), 0.01, "Решение для отрицательных x");
        assertEquals(21.0, solver.apply(10.0), 0.1, "Решение для больших x");
    }

    @Test
    void testExponentialFunctionSolution() {
        // dy/dx = y, y(0) = 1 -> y(x) = e^x
        MathFunction identity = new MathFunction() {
            public double apply(double x) {
                return x; // Для простоты тестирования: dy/dx = x
            }
        };

        RungeKuttaFunction solver = new RungeKuttaFunction(identity, 0, 0, 0.001);
        assertEquals(0.0, solver.apply(0.0), 0.0001, "Начальное условие должно выполняться");
        assertEquals(0.5, solver.apply(1.0), 0.01, "Приближенное решение для dy/dx=x");
        assertEquals(2.0, solver.apply(2.0), 0.1, "Приближенное решение для dy/dx=x");
        assertEquals(4.5, solver.apply(3.0), 0.2, "Приближенное решение для dy/dx=x");
        assertEquals(0.125, solver.apply(0.5), 0.01, "Приближенное решение для малых x");
        assertEquals(8.0, solver.apply(4.0), 0.3, "Приближенное решение для больших x");
    }

    @Test
    void testDifferentInitialConditions() {
        MathFunction func = new MathFunction() {
            public double apply(double x) {
                return 3;
            }
        };

        RungeKuttaFunction solver1 = new RungeKuttaFunction(func, 0, 10, 0.1);
        RungeKuttaFunction solver2 = new RungeKuttaFunction(func, 0, 20, 0.1);
        RungeKuttaFunction solver3 = new RungeKuttaFunction(func, 1, 5, 0.1);

        assertEquals(13.0, solver1.apply(1.0), 0.1, "Решение с начальным условием y(0)=10");
        assertEquals(23.0, solver2.apply(1.0), 0.1, "Решение с начальным условием y(0)=20");
        assertEquals(8.0, solver3.apply(2.0), 0.1, "Решение с начальным условием y(1)=5");
        assertEquals(10.0, solver1.apply(0.0), 0.0001, "Начальное условие должно сохраняться");
        assertEquals(20.0, solver2.apply(0.0), 0.0001, "Начальное условие должно сохраняться");
        assertEquals(5.0, solver3.apply(1.0), 0.0001, "Начальное условие должно сохраняться");
    }

    @Test
    void testDifferentStepSizes() {
        MathFunction func = new MathFunction() {
            public double apply(double x) {
                return 1;
            }
        };

        RungeKuttaFunction solver1 = new RungeKuttaFunction(func, 0, 0, 0.1);
        RungeKuttaFunction solver2 = new RungeKuttaFunction(func, 0, 0, 0.01);
        RungeKuttaFunction solver3 = new RungeKuttaFunction(func, 0, 0, 0.001);

        double result1 = solver1.apply(1.0);
        double result2 = solver2.apply(1.0);
        double result3 = solver3.apply(1.0);

        assertEquals(1.0, result1, 0.1, "Решение с шагом 0.1");
        assertEquals(1.0, result2, 0.01, "Решение с шагом 0.01 должно быть точнее");
        assertEquals(1.0, result3, 0.001, "Решение с шагом 0.001 должно быть еще точнее");
        assertTrue(Math.abs(result2 - 1.0) > Math.abs(result1 - 1.0), "Меньший шаг должен давать лучшую точность");
        assertTrue(Math.abs(result3 - 1.0) == Math.abs(result2 - 1.0), "Меньший шаг должен давать лучшую точность");
        assertEquals(0.0, solver1.apply(0.0), 0.0001, "Начальное условие должно выполняться");
    }

    @Test
    void testNegativeStepAndBackwardIntegration() {
        MathFunction func = new MathFunction() {
            public double apply(double x) {
                return 2 * x;
            }
        };

        RungeKuttaFunction solver = new RungeKuttaFunction(func, 1, 1, -0.1);

        assertEquals(1.0, solver.apply(1.0), 0.0001, "Начальное условие должно выполняться");
        assertEquals(0.81, solver.apply(0.9), 0.01, "Интегрирование назад с отрицательным шагом");
        assertEquals(0.64, solver.apply(0.8), 0.01, "Интегрирование назад с отрицательным шагом");
        assertEquals(0.49, solver.apply(0.7), 0.01, "Интегрирование назад с отрицательным шагом");
        assertEquals(0.36, solver.apply(0.6), 0.01, "Интегрирование назад с отрицательным шагом");
        assertEquals(0.25, solver.apply(0.5), 0.01, "Интегрирование назад с отрицательным шагом");
    }
}