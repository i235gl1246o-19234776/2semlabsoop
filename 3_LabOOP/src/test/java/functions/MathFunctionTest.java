package functions;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MathFunctionTest {

    @Test
    public void testAndThenSingle() {
        // f(x) = x + 1
        MathFunction f = new MathFunction() {
            @Override
            public double apply(double x) {
                return x + 1;
            }
        };

        // g(x) = x²
        MathFunction g = new MathFunction() {
            @Override
            public double apply(double x) {
                return x * x;
            }
        };

        MathFunction composite = g.andThen(f); // f(g(x)) = (x+1)^2
        assertEquals(4.0, composite.apply(1.0)); // (1+1)^2 = 4
        assertEquals(9.0, composite.apply(2.0)); // (2+1)^2 = 9
    }

    @Test
    public void testAndThenChainThree() {
        // f(x) = x + 1
        MathFunction f = new MathFunction() {
            @Override
            public double apply(double x) {
                return x + 1;
            }
        };

        // g(x) = 2x
        MathFunction g = new MathFunction() {
            @Override
            public double apply(double x) {
                return 2 * x;
            }
        };

        // h(x) = x²
        MathFunction h = new MathFunction() {
            @Override
            public double apply(double x) {
                return x * x;
            }
        };

        // Цепочка: f.andThen(g).andThen(h) = h(g(f(x))) = (2*(x+1))^2
        MathFunction composite = h.andThen(g).andThen(f);

        assertEquals(16.0, composite.apply(1.0)); // f(1)=2 → g(2)=4 → h(4)=16
        assertEquals(4.0, composite.apply(0.0));  // f(0)=1 → g(1)=2 → h(2)=4
    }

    @Test
    public void testAndThenWithExpAndLog() {
        // exp(x) = e^x
        MathFunction exp = new MathFunction() {
            @Override
            public double apply(double x) {
                return Math.exp(x);
            }
        };

        // log(x) = ln(x)
        MathFunction log = new MathFunction() {
            @Override
            public double apply(double x) {
                return Math.log(x);
            }
        };

        // Композиция: log(exp(x)) = x
        MathFunction composed = exp.andThen(log); // log(exp(x))

        assertEquals(0.0, composed.apply(0.0), 1e-10);   // log(exp(0)) = log(1) = 0
        assertEquals(1.0, composed.apply(1.0), 1e-10);   // log(exp(1)) ≈ 1
        assertEquals(2.0, composed.apply(2.0), 1e-10);   // log(exp(2)) ≈ 2
    }

    @Test
    public void testAndThenOrderMatters() {
        // f(x) = x + 1
        MathFunction f = new MathFunction() {
            @Override
            public double apply(double x) {
                return x + 1;
            }
        };

        // g(x) = x²
        MathFunction g = new MathFunction() {
            @Override
            public double apply(double x) {
                return x * x;
            }
        };

        // f.andThen(g) = g(f(x)) = (x+1)^2
        MathFunction fg = g.andThen(f);
        assertEquals(4.0, fg.apply(1.0)); // (1+1)^2 = 4

        // g.andThen(f) = f(g(x)) = x^2 + 1
        MathFunction gf = f.andThen(g);
        assertEquals(2.0, gf.apply(1.0)); // 1^2 + 1 = 2

        assertNotEquals(fg.apply(1.0), gf.apply(1.0)); // порядок важен!
    }

    @Test
    public void testEmptyChain() {
        MathFunction identity = new MathFunction() {
            @Override
            public double apply(double x) {
                return x;
            }
        };

        // f(x) = x + 1
        MathFunction f = new MathFunction() {
            @Override
            public double apply(double x) {
                return x + 1;
            }
        };

        MathFunction result = identity.andThen(f); // f(identity(x)) = f(x)
        assertEquals(3.0, result.apply(2.0)); // 2 + 1 = 3

        MathFunction result2 = f.andThen(identity); // identity(f(x)) = f(x)
        assertEquals(3.0, result2.apply(2.0)); // тоже 3
    }

    @Test
    public void testDirectComposition() {
        // Создаем сложную функцию f(g(h(x))) напрямую
        MathFunction h = new MathFunction() {
            @Override
            public double apply(double x) {
                return x * 2; // h(x) = 2x
            }
        };

        MathFunction g = new MathFunction() {
            @Override
            public double apply(double x) {
                return x + 3; // g(x) = x + 3
            }
        };

        MathFunction f = new MathFunction() {
            @Override
            public double apply(double x) {
                return x * x; // f(x) = x²
            }
        };

        // f(g(h(x))) = ((2x) + 3)²
        MathFunction composite = f.andThen(g).andThen(h);
        assertEquals(25.0, composite.apply(1.0)); // ((2*1)+3)² = 25
        assertEquals(49.0, composite.apply(2.0)); // ((2*2)+3)² = 49
    }
}