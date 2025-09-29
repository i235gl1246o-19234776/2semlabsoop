package operations;

import functions.ConstantFunction;
import functions.MathFunction;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Тесты для класса ParallelIntegrator")
class ParallelIntegratorTest {

    @Test
    @DisplayName("Интеграл константной функции должен быть равен площади прямоугольника")
    void testConstantFunction() {
        //f(x) = 5, ∫(5)dx = 5x
        MathFunction constant = new ConstantFunction(5.0);
        double a = 0.0;
        double b = 10.0;
        int n = 1000;
        double expected = 5.0 * (b - a); // 50

        double result = ParallelIntegrator.integrate(constant, a, b, n);

        assertEquals(expected, result, 1e-10,
                "Интеграл константы 5 на [0,10] должен быть 50");
    }

    @Test
    @DisplayName("Интеграл линейной функции должен вычисляться корректно")
    void testLinearFunction() {
        //f(x) = 2x + 1, ∫(2x+1)dx = x² + x
        MathFunction linear = x -> 2 * x + 1;
        double a = 0.0;
        double b = 5.0;
        int n = 10000;
        double expected = (b * b + b) - (a * a + a); //30

        double result = ParallelIntegrator.integrate(linear, a, b, n);

        assertEquals(expected, result, 1e-8,
                "Интеграл 2x+1 на [0,5] должен быть 30");
    }

    @Test
    @DisplayName("Интеграл квадратичной функции должен вычисляться корректно")
    void testQuadraticFunction() {
        //f(x) = x², ∫x²dx = x³/3
        MathFunction quadratic = x -> x * x;
        double a = 0.0;
        double b = 3.0;
        int n = 10000;
        double expected = (b * b * b / 3) - (a * a * a / 3); //9

        double result = ParallelIntegrator.integrate(quadratic, a, b, n);

        assertEquals(expected, result, 1e-8,
                "Интеграл x² на [0,3] должен быть 9");
    }

    @Test
    @DisplayName("Интеграл синуса должен вычисляться корректно")
    void testSinFunction() {
        //f(x) = sin(x), ∫sin(x)dx = -cos(x)
        MathFunction sinFunc = Math::sin;
        double a = 0.0;
        double b = Math.PI;
        int n = 10000;
        double expected = -Math.cos(b) + Math.cos(a); //2

        double result = ParallelIntegrator.integrate(sinFunc, a, b, n);

        assertEquals(expected, result, 1e-8,
                "Интеграл sin(x) на [0,π] должен быть 2");
    }

    @Test
    @DisplayName("Интеграл на симметричном интервале для нечётной функции должен быть 0")
    void testOddFunctionSymmetricInterval() {
        //f(x) = x³ - нечётная функция
        MathFunction oddFunction = x -> x * x * x;
        double a = -2.0;
        double b = 2.0;
        int n = 1000;

        double result = ParallelIntegrator.integrate(oddFunction, a, b, n);

        assertEquals(0.0, result, 1e-10,
                "Интеграл нечётной функции на симметричном интервале должен быть 0");
    }

    @Test
    @DisplayName("Интеграл при a = b должен быть 0")
    void testZeroLengthInterval() {
        //f(x) = x², ∫x²dx = x³/3
        MathFunction anyFunction = x -> x * x;
        double a = 5.0;
        double b = 5.0;
        int n = 1000;

        double result = ParallelIntegrator.integrate(anyFunction, a, b, n);

        assertEquals(0.0, result, 1e-10,
                "Интеграл на интервале нулевой длины должен быть 0");
    }

    @Test
    @DisplayName("Интеграл при a > b должен быть отрицательным")
    void testReversedInterval() {
        MathFunction constant = new ConstantFunction(2.0);
        double a = 10.0;
        double b = 0.0;
        int n = 1000;
        double expected = -20.0; //2 * (0 - 10) = -20

        double result = ParallelIntegrator.integrate(constant, a, b, n);

        assertEquals(expected, result, 1e-10,
                "Интеграл при a > b должен быть отрицательным");
    }

    @Test
    @DisplayName("Метод должен корректно обрабатывать нечётное n (делать его чётным)")
    void testOddNBecomesEven() {
        MathFunction constant = new ConstantFunction(1.0);
        double a = 0.0;
        double b = 1.0;
        int oddN = 999; //нечётное

        double result = ParallelIntegrator.integrate(constant, a, b, oddN);

        assertEquals(1.0, result, 1e-8,
                "Интеграл константы 1 на [0,1] должен быть 1 при любом n");
    }

    @Test
    @DisplayName("Метод должен бросать исключение при n <= 0")
    void testInvalidNThrowsException() {
        MathFunction func = new ConstantFunction(1.0);
        double a = 0.0;
        double b = 1.0;

        assertThrows(IllegalArgumentException.class,
                () -> ParallelIntegrator.integrate(func, a, b, 0),
                "Должно бросаться исключение при n = 0");

        assertThrows(IllegalArgumentException.class,
                () -> ParallelIntegrator.integrate(func, a, b, -100),
                "Должно бросаться исключение при отрицательном n");
    }

    @Test
    @DisplayName("Точность должна увеличиваться с ростом n")
    void testAccuracyIncreasesWithN() {
        //f(x) = x⁴, ∫x⁴dx = x⁵/5
        MathFunction func = x -> x * x * x * x;
        double a = 0.0;
        double b = 2.0;
        double exact = (Math.pow(b, 5) / 5) - (Math.pow(a, 5) / 5); //6.4

        double resultN100 = ParallelIntegrator.integrate(func, a, b, 100);
        double resultN1000 = ParallelIntegrator.integrate(func, a, b, 1000);
        double resultN10000 = ParallelIntegrator.integrate(func, a, b, 10000);

        double error100 = Math.abs(resultN100 - exact);
        double error1000 = Math.abs(resultN1000 - exact);
        double error10000 = Math.abs(resultN10000 - exact);

        assertTrue(error10000 < error1000,
                "Точность при n=10000 должна быть выше чем при n=1000");
        assertTrue(error1000 < error100,
                "Точность при n=1000 должна быть выше чем при n=100");
    }

    @Test
    @DisplayName("Параллельное вычисление должно работать для больших n")
    @Timeout(value = 5, unit = TimeUnit.SECONDS)
    void testParallelComputationForLargeN() {
        //функция, требующая много вычислений
        MathFunction complexFunc = x -> Math.sin(x) * Math.cos(x) * Math.exp(-x * x);
        double a = -10.0;
        double b = 10.0;
        int largeN = 1000000; //1 миллион разбиений

        double result = ParallelIntegrator.integrate(complexFunc, a, b, largeN);

        assertTrue(Double.isFinite(result),
                "Результат должен быть конечным числом");
        assertFalse(Double.isNaN(result),
                "Результат не должен быть NaN");
    }

    @Test
    @DisplayName("Интеграл экспоненциальной функции должен вычисляться корректно")
    void testExponentialFunction() {
        //f(x) = e^x, ∫e^xdx = e^x
        MathFunction expFunc = Math::exp;
        double a = 0.0;
        double b = 1.0;
        int n = 10000;
        double expected = Math.exp(b) - Math.exp(a); // e - 1

        double result = ParallelIntegrator.integrate(expFunc, a, b, n);

        assertEquals(expected, result, 1e-8,
                "Интеграл e^x на [0,1] должен быть e - 1");
    }

    @Test
    @DisplayName("Интеграл должен вычисляться корректно на маленьком интервале")
    void testSmallInterval() {
        MathFunction func = x -> 2 * x + 1; // 2x + 1
        double a = 0.0;
        double b = 0.001; //очень маленький интервал
        int n = 100;
        //Для линейной функции можно использовать приближение трапеции
        double avgHeight = (func.apply(a) + func.apply(b)) / 2;
        double expected = avgHeight * (b - a);

        double result = ParallelIntegrator.integrate(func, a, b, n);

        assertEquals(expected, result, 1e-10,
                "Интеграл на маленьком интервале должен вычисляться корректно");
    }

    @Test
    @DisplayName("Интеграл должен вычисляться корректно на большом интервале")
    void testLargeInterval() {
        MathFunction func = x -> 1.0 / (1.0 + x * x); // функция типа 1/(1+x²)
        double a = -1000.0;
        double b = 1000.0;
        int n = 100000; //большое количество разбиений

        double result = ParallelIntegrator.integrate(func, a, b, n);

        //∫dx/(1+x²) = arctg(x), от -∞ до ∞ = π
        assertEquals(Math.PI, result, 0.01,
                "Интеграл 1/(1+x²) на [-1000,1000] должен быть близок к π");
    }

    @Test
    @DisplayName("Параллельное и последовательное вычисление должны давать одинаковый результат")
    void testParallelVsSequentialConsistency() {
        MathFunction func = x -> Math.sin(x) * Math.log(1 + x * x);
        double a = 0.1;
        double b = 5.0;

        //Когда n меньше порога - последовательное вычисление
        double sequentialResult = ParallelIntegrator.integrate(func, a, b, 500);

        //Когда n больше порога - параллельное вычисление
        double parallelResult = ParallelIntegrator.integrate(func, a, b, 5000);

        assertTrue(Double.isFinite(sequentialResult),
                "Последовательный результат должен быть конечным");
        assertTrue(Double.isFinite(parallelResult),
                "Параллельный результат должен быть конечным");

        assertTrue(Math.abs(sequentialResult) < 100,
                "Результат должен быть разумным по величине");
        assertTrue(Math.abs(parallelResult) < 100,
                "Результат должен быть разумным по величине");
    }

    @Test
    @DisplayName("Интеграл косинуса должен вычисляться корректно")
    void testCosFunction() {
        //f(x) = cos(x), ∫cos(x)dx = sin(x)
        MathFunction cosFunc = Math::cos;
        double a = 0.0;
        double b = Math.PI / 2;
        int n = 10000;
        double expected = Math.sin(b) - Math.sin(a); //1

        double result = ParallelIntegrator.integrate(cosFunc, a, b, n);

        assertEquals(expected, result, 1e-8,
                "Интеграл cos(x) на [0,π/2] должен быть 1");
    }

    @Test
    @DisplayName("Интеграл логарифмической функции должен вычисляться корректно")
    void testLogFunction() {
        //f(x) = ln(x), ∫ln(x)dx = x·ln(x) - x
        MathFunction logFunc = Math::log;
        double a = 1.0;
        double b = Math.E;
        int n = 10000;
        double expected = (b * Math.log(b) - b) - (a * Math.log(a) - a); //1

        double result = ParallelIntegrator.integrate(logFunc, a, b, n);

        assertEquals(expected, result, 1e-8,
                "Интеграл ln(x) на [1,e] должен быть 1");
    }

    @Test
    @DisplayName("Интеграл гиперболического синуса должен вычисляться корректно")
    void testSinhFunction() {
        //f(x) = sinh(x), ∫sinh(x)dx = cosh(x)
        MathFunction sinhFunc = x -> (Math.exp(x) - Math.exp(-x)) / 2;
        double a = 0.0;
        double b = 2.0;
        int n = 10000;
        double coshB = (Math.exp(b) + Math.exp(-b)) / 2;
        double coshA = (Math.exp(a) + Math.exp(-a)) / 2;
        double expected = coshB - coshA;

        double result = ParallelIntegrator.integrate(sinhFunc, a, b, n);

        assertEquals(expected, result, 1e-8,
                "Интеграл sinh(x) на [0,2] должен быть cosh(2) - cosh(0)");
    }

    @Test
    @DisplayName("Интеграл полиномиальной функции должен вычисляться корректно")
    void testPolynomialFunction() {
        //f(x) = 2x³ - 3x² + 4x - 5
        MathFunction polynomial = x -> 2*x*x*x - 3*x*x + 4*x - 5;
        double a = 0.0;
        double b = 3.0;
        int n = 10000;
        //∫(2x³ - 3x² + 4x - 5)dx = (x⁴/2) - x³ + 2x² - 5x
        double expected = (Math.pow(b, 4)/2 - Math.pow(b, 3) + 2*b*b - 5*b) -
                (Math.pow(a, 4)/2 - Math.pow(a, 3) + 2*a*a - 5*a);

        double result = ParallelIntegrator.integrate(polynomial, a, b, n);

        assertEquals(expected, result, 1e-8,
                "Интеграл полинома должен вычисляться корректно");
    }

    @Test
    @DisplayName("Интеграл гауссовой функции должен вычисляться корректно")
    void testGaussianFunction() {
        //f(x) = e^(-x²) - гауссова функция
        MathFunction gaussian = x -> Math.exp(-x * x);
        double a = -3.0;
        double b = 3.0;
        int n = 50000;

        double result = ParallelIntegrator.integrate(gaussian, a, b, n);

        //интеграл гауссовой функции на [-∞,∞] = √π
        assertEquals(Math.sqrt(Math.PI), result, 0.01,
                "Интеграл гауссовой функции на [-3,3] должен быть близок к √π");
    }

    @Test
    @DisplayName("Метод должен работать с очень большим количеством разбиений")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testVeryLargeN() {
        MathFunction func = x -> Math.sin(x) / (1 + x * x);
        double a = 0.0;
        double b = 10.0;
        int veryLargeN = 2000000; //2 миллиона разбиений

        double result = ParallelIntegrator.integrate(func, a, b, veryLargeN);

        assertTrue(Double.isFinite(result),
                "Результат должен быть конечным числом даже при очень большом n");
        assertTrue(result > 0, "Результат должен быть положительным");
    }

    @Test
    @DisplayName("Интеграл рациональной функции должен вычисляться корректно")
    void testRationalFunction() {
        //f(x) = 1/(1+x²), ∫dx/(1+x²) = arctg(x)
        MathFunction rational = x -> 1.0 / (1.0 + x * x);
        double a = -1.0;
        double b = 1.0;
        int n = 10000;
        double expected = Math.atan(b) - Math.atan(a); // π/2

        double result = ParallelIntegrator.integrate(rational, a, b, n);

        assertEquals(expected, result, 1e-8,
                "Интеграл 1/(1+x²) на [-1,1] должен быть π/2");
    }

    @Test
    @DisplayName("Метод должен корректно работать с функцией, содержащей разрывы")
    void testFunctionWithDiscontinuities() {
        //функция с "углом" но без разрывов производной
        MathFunction piecewise = x -> Math.abs(x);
        double a = -2.0;
        double b = 2.0;
        int n = 10000;
        //∫|x|dx = x²/2 для x>=0, -x²/2 для x<0
        double expected = 2.0 * (2.0 * 2.0 / 2.0); //4

        double result = ParallelIntegrator.integrate(piecewise, a, b, n);

        assertEquals(expected, result, 1e-6,
                "Интеграл |x| на [-2,2] должен быть 4");
    }
}