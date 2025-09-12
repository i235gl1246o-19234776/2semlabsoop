package functions;

public interface MathFunction {

    double apply(double x);



    default MathFunction andThen(MathFunction afterFunction) {
        return new CompositeFunction(this, afterFunction);
    }

}
