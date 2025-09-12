package functions;

@FunctionalInterface
public interface MathFunction {

    double apply(double x);

    /**
     * Возвращает композицию текущей функции с afterFunction: this(afterFunction(x))
     * То есть: результат = this.apply(afterFunction.apply(x))
     *
     * @param afterFunction функция, которая будет применена второй
     * @return новая функция, представляющая композицию this ∘ afterFunction
     */
    default MathFunction andThen(MathFunction afterFunction) {
        return new MathFunction() {
            @Override
            public double apply(double x) {
                return MathFunction.this.apply(afterFunction.apply(x));
            }
        };
    }
}
