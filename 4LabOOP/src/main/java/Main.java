import functions.MathFunction;
import io.*;
import operations.ParallelIntegrator;

import java.util.concurrent.ForkJoinPool;

public class Main {

    public static void main(String[] args) {

        System.out.println("ОНО ЗАПУСТИЛОСЬ НАКОНЕЦ-ТО УРА f");
        ArrayTabulatedFunctionSerialization.main(args);
        ArrayTabulatedFunctionXmlSerialization.main(args);
        LinkedListTabulatedFunctionSerialization.main(args);
        //TabulatedFunctionFileInputStream.main(args);
        //TabulatedFunctionFileOutputStream.main(args);
        //TabulatedFunctionFileReader.main(args);
        //TabulatedFunctionFileWriter.main(args);
        //TabulatedFunctionJsonIO.main(args);
        //ForkJoinPool
        System.out.println("===================================");
        // Пример 1: f(x) = x^2, ∫₀¹ x² dx = 1/3 ≈ 0.333...
        MathFunction f1 = x -> x * x;
        double result1 = ParallelIntegrator.integrate(f1, 0.0, 1.0, 10_000);
        System.out.printf("∫₀¹ x² dx ≈ %.10f (точно: 0.3333333333)%n", result1);

        // Пример 2: f(x) = sin(x), ∫₀^π sin(x) dx = 2
        MathFunction f2 = Math::sin;
        double result2 = ParallelIntegrator.integrate(f2, 0.0, Math.PI, 20_000);
        System.out.printf("∫₀^π sin(x) dx ≈ %.10f (точно: 2.0)%n", result2);

        MathFunction f3 = x -> x * x * x * x * x;
        double res = ParallelIntegrator.integrate(f3, 0.0, Math.PI, 20_000);
        System.out.println("f3 = "+ res);

    }

}
