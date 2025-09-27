package operations;

import functions.MathFunction;

import java.util.concurrent.RecursiveTask;

public class SimpsonIntegral extends RecursiveTask<Double> {
    private final MathFunction func;
    private final double a, b;
    private final int n; // должно быть чётным!
    private static final int THRESHOLD = 1000; // порог для переключения на последовательное вычисление

    public SimpsonIntegral(MathFunction func, double a, double b, int n) {
        this.func = func;
        this.a = a;
        this.b = b;
        this.n = (n % 2 == 0) ? n : n + 1;
    }

    @Override
    protected Double compute() {
        if (n <= THRESHOLD) {
            return computeSimpsonSequential(a, b, n);
        } else {
            // Делим отрезок и число разбиений пополам
            double mid = a + (b - a) / 2.0;
            int halfN = n / 2;

            SimpsonIntegral left = new SimpsonIntegral(func, a, mid, halfN);
            SimpsonIntegral right = new SimpsonIntegral(func, mid, b, halfN);

            left.fork();
            double rightResult = right.compute();
            double leftResult = left.join();

            return leftResult + rightResult;
        }
    }

    private double computeSimpsonSequential(double a, double b, int n) {
        double h = (b - a) / n;
        double sum = func.apply(a) + func.apply(b);

        for (int i = 1; i < n; i++) {
            double x = a + i * h;
            sum += (i % 2 == 0) ? 2.0 * func.apply(x) : 4.0 * func.apply(x);
        }

        return sum * h / 3.0;
    }
}