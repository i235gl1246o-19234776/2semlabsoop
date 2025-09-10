package functions;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class IdentityFunctionTest {

    @Test
    void testApply() {

        MathFunction func = new IdentityFunction();

        double expected1 = 42.0;
        double actual1 = func.apply(42.0);
        assertEquals(expected1, actual1);

        double expected2 = -5.2;
        double actual2 = func.apply(-5.2);
        assertEquals(expected2, actual2);

    }

}