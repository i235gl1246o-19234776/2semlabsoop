package functions;

import java.util.Arrays;

public class ArrayTabulatedFunction extends AbstractTabulatedFunction implements Removable {
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
        //Дискретизация
        double step = (xTo - xFrom)/(count - 1);
        for(int i=0; i<count; i++){
            double x = xFrom + i*step;
            xVal[i] = x;
            yVal[i] = s.apply(x);
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
            throw new IllegalArgumentException("Index: " + index + ", Size: " + count);
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
            throw new IllegalArgumentException("Index: " + index + ", Size: " + count);
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
        if (count == 0) {
            throw new IndexOutOfBoundsException("Нет точек в функции: невозможно определить левую границу");
        }
        return xVal[0];
    }

    @Override
    public double rightBound() {
        if (count == 0) {
            throw new IndexOutOfBoundsException("Нет точек в функции: невозможно определить правую границу");
        }
        return xVal[count-1];
    }

    @Override
    public CompositeFunction andThen(MathFunction afterFunction) {
        return super.andThen(afterFunction);
    }

    //должен возвращать индекс наибольшего элемента массива, который меньше или равен заданному x
    @Override
    public int floorIndexOfX(double x) {
        if (x < leftBound()) {
            throw new IllegalArgumentException("Значение x = " + x + " меньше левой границы таблицы ");
        }
        if (x >  rightBound()) {
            throw new IllegalArgumentException("Значение x = " + x + " больше правой границы таблицы ");
        }

        for (int i = 0; i < count - 1; i++) {
            if (xVal[i] <= x && x < xVal[i + 1]) {
                return i;
            }
        }
        return count - 1;
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

    @Override
    public void remove(int index) {
        if (index < 0 || index >= count) {
            throw new IndexOutOfBoundsException("Индекс: " + index + ", размер: " + count);
        }

        for (int i = index; i < count - 1; i++) {
            xVal[i] = xVal[i + 1];
            yVal[i] = yVal[i + 1];
        }

        //Уменьшаем количество точек
        count--;
    }
    public double[] getxVal() {
        return Arrays.copyOf(xVal, count);
    }
    public double[] getyVal() {
        return Arrays.copyOf(yVal, count);
    }

}