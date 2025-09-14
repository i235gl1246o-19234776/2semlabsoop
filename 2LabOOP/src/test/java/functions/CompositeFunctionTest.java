package functions;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Тестовый класс для CompositeFunction.
 * Проверяет корректность работы композитных функций.
 */
class CompositeFunctionTest {

    @Test
    void testIdentityComposition() {
        MathFunction identity = new IdentityFunction();
        CompositeFunction composite = new CompositeFunction(identity, identity);

        assertEquals(5.0, composite.apply(5.0), 0.0001, "identity(identity(5)) = 5");
        assertEquals(-3.0, composite.apply(-3.0), 0.0001, "identity(identity(-3)) = -3");
        assertEquals(0.0, composite.apply(0.0), 0.0001, "identity(identity(0)) = 0");
        assertEquals(7.5, composite.apply(7.5), 0.0001, "identity(identity(7.5)) = 7.5");
        assertEquals(100.0, composite.apply(100.0), 0.0001, "identity(identity(100)) = 100");
        assertEquals(-2.2, composite.apply(-2.2), 0.0001, "identity(identity(-2.2)) = -2.2");
    }

    @Test
    void testSqrAndIdentityComposition() {
        MathFunction sqr = new SqrFunction();
        MathFunction identity = new IdentityFunction();
        CompositeFunction sqrThenIdentity = new CompositeFunction(sqr, identity);
        CompositeFunction identityThenSqr = new CompositeFunction(identity, sqr);

        assertEquals(25.0, sqrThenIdentity.apply(5.0), 0.0001, "identity(sqr(5)) = 25");
        assertEquals(25.0, identityThenSqr.apply(5.0), 0.0001, "sqr(identity(5)) = 25");
        assertEquals(4.0, sqrThenIdentity.apply(-2.0), 0.0001, "identity(sqr(-2)) = 4");
        assertEquals(4.0, identityThenSqr.apply(-2.0), 0.0001, "sqr(identity(-2)) = 4");
        assertEquals(0.0, sqrThenIdentity.apply(0.0), 0.0001, "identity(sqr(0)) = 0");
        assertEquals(0.0, identityThenSqr.apply(0.0), 0.0001, "sqr(identity(0)) = 0");
        assertEquals(6.25, sqrThenIdentity.apply(2.5), 0.0001, "identity(sqr(2.5)) = 6.25");
        assertEquals(6.25, identityThenSqr.apply(2.5), 0.0001, "sqr(identity(2.5)) = 6.25");
        assertEquals(6.25, sqrThenIdentity.apply(-2.5), 0.0001, "identity(sqr(-2.5)) = 6.25");
        assertEquals(6.25, identityThenSqr.apply(-2.5), 0.0001, "sqr(identity(-2.5)) = 6.25");
    }

    @Test
    void testDoubleSqrComposition() {
        MathFunction sqr = new SqrFunction();
        CompositeFunction sqrSqr = new CompositeFunction(sqr, sqr);

        assertEquals(625.0, sqrSqr.apply(5.0), 0.0001, "sqr(sqr(5)) = 625");
        assertEquals(16.0, sqrSqr.apply(2.0), 0.0001, "sqr(sqr(2)) = 16");
        assertEquals(16.0, sqrSqr.apply(-2.0), 0.0001, "sqr(sqr(-2)) = 16");
        assertEquals(0.0, sqrSqr.apply(0.0), 0.0001, "sqr(sqr(0)) = 0");
        assertEquals(390625.0, sqrSqr.apply(25.0), 0.0001, "sqr(sqr(25)) = 390625");
        assertEquals(1.0, sqrSqr.apply(1.0), 0.0001, "sqr(sqr(1)) = 1");
    }

    @Test
    void testTripleComposition() {
        MathFunction sqr = new SqrFunction();
        MathFunction identity = new IdentityFunction();

        CompositeFunction firstLevel = new CompositeFunction(sqr, identity);
        CompositeFunction secondLevel = new CompositeFunction(firstLevel, sqr);
        CompositeFunction thirdLevel = new CompositeFunction(secondLevel, identity);

        assertEquals(625.0, secondLevel.apply(5.0), 0.0001, "sqr(identity(sqr(5))) = 625");
        assertEquals(625.0, thirdLevel.apply(5.0), 0.0001, "identity(sqr(identity(sqr(5)))) = 625");
        assertEquals(256.0, secondLevel.apply(4.0), 0.0001, "sqr(identity(sqr(4))) = 256");
        assertEquals(256.0, thirdLevel.apply(4.0), 0.0001, "identity(sqr(identity(sqr(4)))) = 256");
        assertEquals(16.0, secondLevel.apply(2.0), 0.0001, "sqr(identity(sqr(2))) = 16");
        assertEquals(16.0, thirdLevel.apply(2.0), 0.0001, "identity(sqr(identity(sqr(2)))) = 16");
        assertEquals(0.0, secondLevel.apply(0.0), 0.0001, "sqr(identity(sqr(0))) = 0");
        assertEquals(0.0, thirdLevel.apply(0.0), 0.0001, "identity(sqr(identity(sqr(0)))) = 0");
        assertEquals(1.0, secondLevel.apply(1.0), 0.0001, "sqr(identity(sqr(1))) = 1");
        assertEquals(1.0, thirdLevel.apply(1.0), 0.0001, "identity(sqr(identity(sqr(1)))) = 1");
        assertEquals(1.0, secondLevel.apply(-1.0), 0.0001, "sqr(identity(sqr(-1))) = 1");
        assertEquals(1.0, thirdLevel.apply(-1.0), 0.0001, "identity(sqr(identity(sqr(-1)))) = 1");
    }

    @Test
    void testComplexNestedComposition() {
        MathFunction sqr = new SqrFunction();
        MathFunction identity = new IdentityFunction();

        CompositeFunction level1 = new CompositeFunction(sqr, sqr);
        CompositeFunction level2 = new CompositeFunction(level1, identity);
        CompositeFunction level3 = new CompositeFunction(identity, level2);
        CompositeFunction level4 = new CompositeFunction(level3, sqr);

        assertEquals(625.0, level1.apply(5.0), 0.0001, "sqr(sqr(5)) = 390625");
        assertEquals(625.0, level2.apply(5.0), 0.0001, "identity(sqr(sqr(5))) = 390625");
        assertEquals(625.0, level3.apply(5.0), 0.0001, "identity(sqr(sqr(5))) через level3 = 390625");
        assertEquals(390625.0, level4.apply(5.0), 1.0, "sqr(identity(sqr(sqr(5)))) = 152587890625");

        assertEquals(16.0, level1.apply(2.0), 0.0001, "sqr(sqr(2)) = 256");
        assertEquals(16.0, level2.apply(2.0), 0.0001, "identity(sqr(sqr(2))) = 256");
        assertEquals(16.0, level3.apply(2.0), 0.0001, "identity(sqr(sqr(2))) через level3 = 256");
        assertEquals(256.0, level4.apply(2.0), 0.0001, "sqr(identity(sqr(sqr(2)))) = 65536");

        assertEquals(0.0, level1.apply(0.0), 0.0001, "sqr(sqr(0)) = 0");
        assertEquals(0.0, level2.apply(0.0), 0.0001, "identity(sqr(sqr(0))) = 0");
        assertEquals(0.0, level3.apply(0.0), 0.0001, "identity(sqr(sqr(0))) через level3 = 0");
        assertEquals(0.0, level4.apply(0.0), 0.0001, "sqr(identity(sqr(sqr(0)))) = 0");
    }

    @Test
    void testWithCustomFunctions() {
        MathFunction doubleFunction = new MathFunction() {
            public double apply(double x) {
                return 2 * x;
            }
        };

        MathFunction tripleFunction = new MathFunction() {
            public double apply(double x) {
                return 3 * x;
            }
        };

        CompositeFunction doubleThenTriple = new CompositeFunction(doubleFunction, tripleFunction);
        CompositeFunction tripleThenDouble = new CompositeFunction(tripleFunction, doubleFunction);

        assertEquals(30.0, doubleThenTriple.apply(5.0), 0.0001, "triple(double(5)) = 30");
        assertEquals(30.0, tripleThenDouble.apply(5.0), 0.0001, "double(triple(5)) = 30");
        assertEquals(0.0, doubleThenTriple.apply(0.0), 0.0001, "triple(double(0)) = 0");
        assertEquals(0.0, tripleThenDouble.apply(0.0), 0.0001, "double(triple(0)) = 0");
        assertEquals(-12.0, doubleThenTriple.apply(-2.0), 0.0001, "triple(double(-2)) = -12");
        assertEquals(-12.0, tripleThenDouble.apply(-2.0), 0.0001, "double(triple(-2)) = -12");
        assertEquals(7.2, doubleThenTriple.apply(1.2), 0.0001, "triple(double(1.2)) = 7.2");
        assertEquals(7.2, tripleThenDouble.apply(1.2), 0.0001, "double(triple(1.2)) = 7.2");
        assertEquals(60.0, doubleThenTriple.apply(10.0), 0.0001, "triple(double(10)) = 60");
        assertEquals(60.0, tripleThenDouble.apply(10.0), 0.0001, "double(triple(10)) = 60");
        assertEquals(-60.0, doubleThenTriple.apply(-10.0), 0.0001, "triple(double(-10)) = -60");
        assertEquals(-60.0, tripleThenDouble.apply(-10.0), 0.0001, "double(triple(-10)) = -60");
    }

    @Test
    void testArrayWithArrayAndThen(){
        //f(x) = 4x - 5
        double[] x1 = {0.0, 1.0, 2.0, 3.0, 4.0};
        double[] y1 = {-5.0, -1.0, 3.0, 7.0, 11.0};
        ArrayTabulatedFunction f = new ArrayTabulatedFunction(x1, y1);

        //g(x) = x + 10
        double[] x2 = {0.0, 1.0, 2.0, 3.0, 4.0};
        double[] y2 = {10.0, 11.0, 12.0, 13.0, 14.0};
        ArrayTabulatedFunction g = new ArrayTabulatedFunction(x2, y2);

        MathFunction composition = f.andThen(g);
        // f(g(x)) = 4(x + 10) - 5 = 4x + 35
        assertEquals(35.0, composition.apply(0.0), 1e-8, "f(g(0)) = f(10) = 35");
        assertEquals(39.0, composition.apply(1.0), 1e-8, "f(g(1)) = f(11) = 39");
        assertEquals(43.0, composition.apply(2.0), 1e-8, "f(g(2)) = f(12) = 43");
        assertEquals(47.0, composition.apply(3.0), 1e-8, "f(g(3)) = f(13) = 47");
        assertEquals(51.0, composition.apply(4.0), 1e-8, "f(g(3)) = f(14) = 51");

        assertEquals(36.0, composition.apply(0.25), 1e-8, "f(g(0.25)) интерполяция");
        assertEquals(37.0, composition.apply(0.5), 1e-8, "f(g(0.5)) интерполяций");
        assertEquals(38.0, composition.apply(0.75), 1e-8, "f(g(0.75)) интерполяций");
        assertEquals(40.0, composition.apply(1.25), 1e-8, "f(g(1.25)) интерполяций");
        assertEquals(41.0, composition.apply(1.5), 1e-8, "f(g(1.5)) интерполяций");
    }

    void testArrayWithLinkedListAndThen(){
        //f(x) = 4x - 5
        double[] x1 = {0.0, 1.0, 2.0, 3.0, 4.0};
        double[] y1 = {-5.0, -1.0, 3.0, 7.0, 11.0};
        ArrayTabulatedFunction f = new ArrayTabulatedFunction(x1, y1);

        //g(x) = x + 10
        double[] x2 = {0.0, 1.0, 2.0, 3.0, 4.0};
        double[] y2 = {10.0, 11.0, 12.0, 13.0, 14.0};
        LinkedListTabulatedFunction g = new LinkedListTabulatedFunction(x2, y2);

        MathFunction composition = f.andThen(g);
        // f(g(x)) = 4(x + 10) - 5 = 4x + 35
        assertEquals(35.0, composition.apply(0.0), 1e-8, "f(g(0)) = f(10) = 35");
        assertEquals(39.0, composition.apply(1.0), 1e-8, "f(g(1)) = f(11) = 39");
        assertEquals(43.0, composition.apply(2.0), 1e-8, "f(g(2)) = f(12) = 43");
        assertEquals(47.0, composition.apply(3.0), 1e-8, "f(g(3)) = f(13) = 47");
        assertEquals(51.0, composition.apply(4.0), 1e-8, "f(g(3)) = f(14) = 51");

        assertEquals(36.0, composition.apply(0.25), 1e-8, "f(g(0.25)) интерполяция");
        assertEquals(37.0, composition.apply(0.5), 1e-8, "f(g(0.5)) интерполяций");
        assertEquals(38.0, composition.apply(0.75), 1e-8, "f(g(0.75)) интерполяций");
        assertEquals(40.0, composition.apply(1.25), 1e-8, "f(g(1.25)) интерполяций");
        assertEquals(41.0, composition.apply(1.5), 1e-8, "f(g(1.5)) интерполяций");
    }

    void testLinkedListyWithLinkedListAndThen(){
        //f(x) = 4x - 5
        double[] x1 = {0.0, 1.0, 2.0, 3.0, 4.0};
        double[] y1 = {-5.0, -1.0, 3.0, 7.0, 11.0};
        LinkedListTabulatedFunction f = new LinkedListTabulatedFunction(x1, y1);

        //g(x) = x + 10
        double[] x2 = {0.0, 1.0, 2.0, 3.0, 4.0};
        double[] y2 = {10.0, 11.0, 12.0, 13.0, 14.0};
        LinkedListTabulatedFunction g = new LinkedListTabulatedFunction(x2, y2);

        MathFunction composition = f.andThen(g);
        // f(g(x)) = 4(x + 10) - 5 = 4x + 35
        assertEquals(35.0, composition.apply(0.0), 1e-8, "f(g(0)) = f(10) = 35");
        assertEquals(39.0, composition.apply(1.0), 1e-8, "f(g(1)) = f(11) = 39");
        assertEquals(43.0, composition.apply(2.0), 1e-8, "f(g(2)) = f(12) = 43");
        assertEquals(47.0, composition.apply(3.0), 1e-8, "f(g(3)) = f(13) = 47");
        assertEquals(51.0, composition.apply(4.0), 1e-8, "f(g(3)) = f(14) = 51");

        assertEquals(36.0, composition.apply(0.25), 1e-8, "f(g(0.25)) интерполяция");
        assertEquals(37.0, composition.apply(0.5), 1e-8, "f(g(0.5)) интерполяций");
        assertEquals(38.0, composition.apply(0.75), 1e-8, "f(g(0.75)) интерполяций");
        assertEquals(40.0, composition.apply(1.25), 1e-8, "f(g(1.25)) интерполяций");
        assertEquals(41.0, composition.apply(1.5), 1e-8, "f(g(1.5)) интерполяций");
    }

    void testLinkedListyWithArrayAndThen(){
        //f(x) = 4x - 5
        double[] x1 = {0.0, 1.0, 2.0, 3.0, 4.0};
        double[] y1 = {-5.0, -1.0, 3.0, 7.0, 11.0};
        LinkedListTabulatedFunction f = new LinkedListTabulatedFunction(x1, y1);

        //g(x) = x + 10
        double[] x2 = {0.0, 1.0, 2.0, 3.0, 4.0};
        double[] y2 = {10.0, 11.0, 12.0, 13.0, 14.0};
        ArrayTabulatedFunction g = new ArrayTabulatedFunction(x2, y2);

        MathFunction composition = f.andThen(g);
        // f(g(x)) = 4(x + 10) - 5 = 4x + 35
        assertEquals(35.0, composition.apply(0.0), 1e-8, "f(g(0)) = f(10) = 35");
        assertEquals(39.0, composition.apply(1.0), 1e-8, "f(g(1)) = f(11) = 39");
        assertEquals(43.0, composition.apply(2.0), 1e-8, "f(g(2)) = f(12) = 43");
        assertEquals(47.0, composition.apply(3.0), 1e-8, "f(g(3)) = f(13) = 47");
        assertEquals(51.0, composition.apply(4.0), 1e-8, "f(g(3)) = f(14) = 51");

        assertEquals(36.0, composition.apply(0.25), 1e-8, "f(g(0.25)) интерполяция");
        assertEquals(37.0, composition.apply(0.5), 1e-8, "f(g(0.5)) интерполяций");
        assertEquals(38.0, composition.apply(0.75), 1e-8, "f(g(0.75)) интерполяций");
        assertEquals(40.0, composition.apply(1.25), 1e-8, "f(g(1.25)) интерполяций");
        assertEquals(41.0, composition.apply(1.5), 1e-8, "f(g(1.5)) интерполяций");
    }
}