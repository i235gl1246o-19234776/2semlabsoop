package functions;

import java.util.Arrays;

public class ArrayTabulatedFunction extends AbstractTabulatedFunction implements Insertable{
    private double[] xVal;
    private double[] yVal;
    private int count;

    public ArrayTabulatedFunction(double[] xVal, double[] yVal){
        if (xVal.length != yVal.length){
            throw new IllegalArgumentException("Массивы разной длины");
        }
        for (int i = 1; i < xVal.length; i++){
            if (xVal[i] <= xVal[i-1]){
                throw new IllegalArgumentException("xVal не упорядочены");
            }
        }

        this.count = xVal.length;
        this.xVal = Arrays.copyOf(xVal, count);
        this.yVal = Arrays.copyOf(yVal, count);
    }

    public ArrayTabulatedFunction(MathFunction s, double xFrom, double xTo, int count){
        if(count < 2){
            throw new IllegalArgumentException("Меньше 2х элементов");
        }
        this.count = count;
        this.xVal = new double[count];
        this.yVal = new double[count];

        if (xFrom == xTo){
            Arrays.fill(xVal, xFrom);
            Arrays.fill(yVal, s.apply(xFrom));
        }
        if (xFrom > xTo){
            double t = xFrom;
            xFrom = xTo;
            xTo = t;
        }
        // Дискретизация
        double step = (xTo - xFrom)/(count - 1);
        for(int i=0; i<count; i++){
            double x = xFrom + i*step;
            xVal[i] = x;
            yVal[i] = s.apply(x);
        }

    }

    @Override
    public void insert(double x, double y){
        int index = findInsertionIndex(x);

        if(index < count && java.lang.Math.abs(xVal[index] - x) < 1e-10){
            yVal[index] = y;
            return;
        }else{
            ensureCapacity();

            if(index < count){
                System.arraycopy(xVal, index, xVal, index + 1, count - index);
                System.arraycopy(yVal, index, yVal, index + 1, count - index);
            }

            xVal[index] = x;
            yVal[index] = y;
            count++;
        }
    }

    private int findInsertionIndex(double x){
        for (int i = 0; i < count; i++){
            if(java.lang.Math.abs(xVal[i] - x) < 1e-10){
                return i;
            }
            if(xVal[i] > x){
                return i;
            }
        }
        return count;
    }

    private void ensureCapacity() {
        if (count == xVal.length) {
            // Увеличиваем массивы в 2 раза
            int nwCapacity = xVal.length * 2;
            double[] nwXVal = new double[nwCapacity];
            double[] nwYVal = new double[nwCapacity];

            System.arraycopy(xVal, 0, nwXVal, 0, count);
            System.arraycopy(yVal, 0, nwYVal, 0, count);

            xVal = nwXVal;
            yVal = nwYVal;
        }
    }

    @Override
    public double extrapolateLeft(double x) {
        if (count == 1) {
            return yVal[0];
        }
        return interpolate(x, 0);
    }

    @Override
    public double extrapolateRight(double x) {
        if (count == 1) {
            return yVal[0];
        }
        return interpolate(x, count - 2);
    }

    @Override
    public double interpolate(double x, int floorIndex) {
        if (count == 1) {
            return yVal[0];
        }

        if (floorIndex < 0 || floorIndex >= count - 1) {
            throw new IndexOutOfBoundsException("Аут оф индекс: " + floorIndex);
        }

        double x1 = xVal[floorIndex];
        double x2 = xVal[floorIndex + 1];
        double y1 = yVal[floorIndex];
        double y2 = yVal[floorIndex + 1];

        return interpolate(x, x1, x2, y1, y2);
    }

    @Override
    public int getCount(){
        return count;
    }
    @Override
    public double getX(int index){
        if (index < 0 || index >= count){
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + count);
        }
        return xVal[index];
    }

    @Override
    public double getY(int index) {
        if (index < 0 || index >= count) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + count);
        }
        return yVal[index];
    }

    @Override
    public void setY(int index, double value) {
        if (index < 0 || index >= count) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + count);
        }
        yVal[index] = value;
    }

    @Override
    public int indexOfX(double x) {
        for (int i = 0; i < count; i++) {
            if (Math.abs(xVal[i] - x) < 1e-10) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public int indexOfY(double y) {
        for (int i = 0; i < count; i++) {
            if (Math.abs(yVal[i] - y) < 1e-10) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public double leftBound() {
        return xVal[0];
    }

    @Override
    public double rightBound() {
        return xVal[count-1];
    }

    @Override
    public MathFunction andThen(MathFunction afterFunction) {
        return super.andThen(afterFunction);
    }
    //должен возвращать индекс наибольшего элемента массива, который меньше или равен заданному x
    @Override

    public int floorIndexOfX(double x) {
        if (x < xVal[0]) {
            return 0;
        }
        if (x >= xVal[count - 1]) {
            return count - 1;
        }

        for (int i = 0; i < count - 1; i++) {
            if (xVal[i] <= x && x < xVal[i + 1]) {
                return i;
            }
        }
        return -42;
    }
    @Override
    public double apply(double x) {
        if (x < leftBound()) {
            return extrapolateLeft(x);
        } else if (x > rightBound()) {
            return extrapolateRight(x);
        } else {
            int index = indexOfX(x);
            if (index != -1) {
                return getY(index);
            } else {
                return interpolate(x, floorIndexOfX(x));
            }
        }
    }
}
