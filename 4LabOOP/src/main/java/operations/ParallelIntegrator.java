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
        if (a >= b) {
            throw new IllegalArgumentException("a must be less than b");
        }
        if (n <= 0) {
            throw new IllegalArgumentException("n must be positive");
        }

        // Создаём задачу и выполняем в общем пуле
        SimpsonIntegral task = new SimpsonIntegral(func, a, b, n);
        return ForkJoinPool.commonPool().invoke(task);
    }
}
