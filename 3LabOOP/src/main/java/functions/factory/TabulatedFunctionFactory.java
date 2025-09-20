package functions.factory;

import functions.StrictTabulatedFunction;
import functions.TabulatedFunction;
import functions.UnmodifiableTabulatedFunction;

public interface TabulatedFunctionFactory {
    TabulatedFunction create(double[] xValues, double[] yValues);

    default TabulatedFunction createStrict(double[] xValues, double[] yValues){
        TabulatedFunction baseFunction = create(xValues, yValues);
        return new StrictTabulatedFunction(baseFunction);
    }

    default TabulatedFunction createStrictUnmodifiable(double[] xValues, double[] yValues){
        TabulatedFunction baseFunction = create(xValues, yValues);
        TabulatedFunction strictFunction = new StrictTabulatedFunction(baseFunction);
        return new UnmodifiableTabulatedFunction(strictFunction);
    }

}
