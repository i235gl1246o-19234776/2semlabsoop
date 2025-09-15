package functions;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Тесты для RungeKuttaFunction")
class RungeKuttaFunctionTest {
    
    private final static double delta = 1e-6;

    @Test
    @DisplayName("Тест на проверку константы")
    void testConstantFunctionSolution() {
        //dy/dx = 0, y(0) = 5 -> y(x) = 5
        MathFunction zeroFunction = x -> 0;

        RungeKuttaFunction function = new RungeKuttaFunction(zeroFunction, 0, 5, 0.1);
        assertEquals(5.0, function.apply(1.0), delta, "Решение для dy/dx=0 должно быть константой, GOOD");
        assertEquals(5.0, function.apply(2.0), delta, "Решение для dy/dx=0 должно быть константой, GOOD");
        assertEquals(5.0, function.apply(-1.0), delta, "Решение для dy/dx=0 должно быть константой, GOOD");
        assertEquals(5.0, function.apply(10.0), delta, "Решение для dy/dx=0 должно быть константой, GOOD");
        assertEquals(5.0, function.apply(0.5), delta, "Решение для dy/dx=0 должно быть константой, GOOD");
        assertEquals(5.0, function.apply(100.0), delta, "Решение для dy/dx=0 должно быть константой, GOOD");
    }

    @Test
    @DisplayName("Тест на проверку линейной функции")
    void testLinearFunctionSolution() {
        //dy/dx = 2, y(0) = 1 -> y(x) = 2x + 1
        MathFunction constantTwo = x -> 2;

        RungeKuttaFunction function = new RungeKuttaFunction(constantTwo, 0, 1, 1e-6);
        assertEquals(3.0, function.apply(1.0), delta, "y(1) = 2*1 + 1 = 3, GOOD");
        assertEquals(5.0, function.apply(2.0), delta, "y(2) = 2*2 + 1 = 5, GOOD");
        assertEquals(7.0, function.apply(3.0), delta, "y(3) = 2*3 + 1 = 7, GOOD");
        assertEquals(1.0, function.apply(0.0), delta, "y(0) = 1, GOOD");
        assertEquals(21.0, function.apply(10.0), delta, "y(10) = 2*10 + 1, GOOD");
    }

    @Test
    @DisplayName("Тест на проверку квадратичной функции")
    void testQuadraticFunctionSolution() {
        //dy/dx = x, y(0) = 0 -> y(x) = x²/2
        MathFunction identity = x -> x;

        RungeKuttaFunction function = new RungeKuttaFunction(identity, 0, 0, 0.001);
        assertEquals(0.0, function.apply(0.0), delta, "Начальное условие должно выполняться, GOOD");
        assertEquals(0.5, function.apply(1.0), delta, "y(1) = 1²/2 = 0.5, GOOD");
        assertEquals(2.0, function.apply(2.0), delta, "y(2) = 2²/2 = 2.0, GOOD");
        assertEquals(4.5, function.apply(3.0), delta, "y(3) = 3²/2 = 4.5, GOOD");
        assertEquals(0.125, function.apply(0.5), delta, "y(0.5) = 0.5²/2 = 0.125, GOOD");
        assertEquals(8.0, function.apply(4.0), delta, "y(4) = 4²/2 = 8.0, GOOD");
    }

    @Test
    @DisplayName("Тест на проверку различных начальных условий")
    void testDifferentInitialConditions() {
        MathFunction func = x -> 3;

        RungeKuttaFunction function1 = new RungeKuttaFunction(func, 0, 10, 0.1);
        RungeKuttaFunction function2 = new RungeKuttaFunction(func, 0, 20, 0.1);
        RungeKuttaFunction function3 = new RungeKuttaFunction(func, 1, 5, 0.1);

        assertEquals(13.0, function1.apply(1.0), 0.1, "y(1) = 13, GOOD");
        assertEquals(23.0, function2.apply(1.0), 0.1, "y(1) = 23, GOOD");
        assertEquals(8.0, function3.apply(2.0), 0.1, "y(2) = 8");
        assertEquals(10.0, function1.apply(0.0), delta, "y(0) = 10, GOOD");
        assertEquals(20.0, function2.apply(0.0), delta, "y(0) = 20, GOOD");
        assertEquals(5.0, function3.apply(1.0), delta, "y(1) = 5, GOOD");
    }

    @Test
    @DisplayName("Тест на проверку различных шагов")
    void testDifferentStepSizes() {
        MathFunction func = x -> 1;

        RungeKuttaFunction function1 = new RungeKuttaFunction(func, 0, 0, 0.1);
        RungeKuttaFunction function2 = new RungeKuttaFunction(func, 0, 0, 1e-8);
        RungeKuttaFunction function3 = new RungeKuttaFunction(func, 0, 0, 0.001);

        double result1 = function1.apply(1.0);
        double result2 = function2.apply(1.0);
        double result3 = function3.apply(1.0);

        assertEquals(1.0, result1, 0.1, "Решение с шагом 0.1, GOOD");
        assertEquals(1.0, result2, 1e-8, "Решение с шагом 1e-8 должно быть точнее, GOOD");
        assertEquals(1.0, result3, 0.001, "Решение с шагом 0.001 должно быть еще точнее, GOOD");
        assertTrue(java.lang.Math.abs(result2 - 1.0) > java.lang.Math.abs(result1 - 1.0), "Меньший шаг должен давать лучшую точность, GOOD");
        assertTrue(java.lang.Math.abs(result3 - 1.0) < java.lang.Math.abs(result2 - 1.0), "Меньший шаг должен давать лучшую точность, GOOD");
        assertEquals(0.0, function1.apply(0.0), delta, "Начальное условие должно выполняться, GOOD");
        assertEquals(0.0, function2.apply(0.0), delta, "Начальное условие должно выполняться, GOOD");
        assertEquals(0.0, function3.apply(0.0), delta, "Начальное условие должно выполняться, GOOD");
    }

    @Test
    @DisplayName("Тест на проверку негативного шага и обратного интегрирования")
    void testNegativeStepAndBackwardIntegration() {
        //dy/dx = 2x, y(1) = 1 -> y(x) = x²
        MathFunction func = x -> 2*x;

        RungeKuttaFunction function = new RungeKuttaFunction(func, 1, 1, -0.1);

        assertEquals(1.0, function.apply(1.0), delta, "Начальное условие должно выполняться, GOOD");
        assertEquals(0.81, function.apply(0.9), delta, "y(0.9) = 0.9² = 0.81, GOOD");
        assertEquals(0.64, function.apply(0.8), delta, "y(0.8) = 0.8² = 0.64, GOOD");
        assertEquals(0.49, function.apply(0.7), delta, "y(0.7) = 0.7² = 0.49, GOOD");
        assertEquals(0.36, function.apply(0.6), delta, "y(0.6) = 0.6² = 0.36, GOOD");
        assertEquals(0.25, function.apply(0.5), delta, "y(0.5) = 0.5² = 0.25, GOOD");
    }
}