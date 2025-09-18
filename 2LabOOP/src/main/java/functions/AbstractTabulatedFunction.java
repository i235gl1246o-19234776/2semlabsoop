package functions;

public abstract class AbstractTabulatedFunction implements TabulatedFunction {

    protected int count;

    protected abstract int floorIndexOfX(double x);
    protected abstract double extrapolateLeft(double x);
    protected abstract double extrapolateRight(double x);
    protected abstract double interpolate(double x, int floorIndex);

    protected double interpolate(double x, double leftX, double rightX,
                                 double leftY, double rightY) {
        return leftY + (rightY - leftY) * (x - leftX) / (rightX - leftX);
    }

    @Override
    public double apply(double x) {
        if (x < getX(0)) {
            return extrapolateLeft(x);
        }

        if (x > getX(count - 1)) {
            return extrapolateRight(x);
        }

        int exactIndex = indexOfX(x);
        if (exactIndex != -1) {
            return getY(exactIndex);
        }

        int floorIndex = floorIndexOfX(x);
        return interpolate(x, floorIndex);
    }

    //Абстрактные методы из интерфейса TabulatedFunction
    public abstract int getCount();
    public abstract double getX(int index);
    public abstract double getY(int index);
    public abstract void setY(int index, double value);
    public abstract int indexOfX(double x);
    public abstract int indexOfY(double y);
    public abstract double leftBound();
    public abstract double rightBound();
}