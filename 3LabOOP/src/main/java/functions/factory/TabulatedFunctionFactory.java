package functions.factory;

import functions.StrictTabulatedFunction;
import functions.TabulatedFunction;

public interface TabulatedFunctionFactory {
    TabulatedFunction create(double[] xValues, double[] yValues);

    default TabulatedFunction createStrict(double[] xValues, double[] yValues){
        TabulatedFunction baseFunction = create(xValues, yValues);
        return new StrictTabulatedFunction(baseFunction);
    }

}
