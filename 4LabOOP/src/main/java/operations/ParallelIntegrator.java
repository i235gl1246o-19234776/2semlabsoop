package operations;
import functions.MathFunction;

import java.util.concurrent.ForkJoinPool;

public class ParallelIntegrator {

    /**
     * Вычисляет определённый интеграл ∫ₐᵇ f(x) dx методом Симпсона с параллелизацией.
     *
     * @param func подынтегральная функция
     * @param a нижний предел интегрирования
     * @param b верхний предел интегрирования
     * @param n желаемое число разбиений (будет приведено к чётному)
     * @return приближённое значение интеграла
     */
    public static double integrate(MathFunction func, double a, double b, int n) {
        if (a == b) {
            return 0;
        }
        if (n <= 0) {
            throw new IllegalArgumentException("n должно быть положительным");
        }

        SimpsonIntegral task;
        if (a > b) {
            task = new SimpsonIntegral(func, b, a, n);
            return -ForkJoinPool.commonPool().invoke(task);
        } else {
            task = new SimpsonIntegral(func, a, b, n);
            return ForkJoinPool.commonPool().invoke(task);
        }
    }
}
