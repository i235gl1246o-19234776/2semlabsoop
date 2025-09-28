package concurrent;

import functions.*;
import operations.TabulatedFunctionOperationService;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class SynchronizedTabulatedFunction implements TabulatedFunction{
    private final TabulatedFunction function;
    private final Lock lock;

    public SynchronizedTabulatedFunction(TabulatedFunction function){
        this.function = function;
        this.lock = new ReentrantLock();
    }

    @Override
    public int getCount(){
        lock.lock();
        try{
            return function.getCount();
        } finally {
            lock.unlock();
        }
    }

    @Override
    public double getX(int index){
        lock.lock();
        try{
            return function.getX(index);
        }finally {
            lock.unlock();
        }
    }

    @Override
    public double getY(int index){
        lock.lock();
        try{
            return function.getY(index);
        }finally {
            lock.unlock();
        }
    }

    @Override
    public void setY(int index, double value) {
        lock.lock();
        try {
            function.setY(index, value);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public double leftBound() {
        lock.lock();
        try {
            return function.leftBound();
        } finally {
            lock.unlock();
        }
    }

    @Override
    public double rightBound() {
        lock.lock();
        try {
            return function.rightBound();
        } finally {
            lock.unlock();
        }
    }

    @Override
    public int indexOfX(double x) {
        lock.lock();
        try {
            return function.indexOfX(x);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public int indexOfY(double y) {
        lock.lock();
        try {
            return function.indexOfY(y);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public double apply(double x) {
        lock.lock();
        try {
            return function.apply(x);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public Iterator<Point> iterator(){
        lock.lock();
        try{
            Point[] pointsCopy = TabulatedFunctionOperationService.asPoints(function);

            return new Iterator<Point>() {

                private int currIndex = 0;
                private final Point[] points = pointsCopy;

                @Override
                public boolean hasNext() {
                    return currIndex < points.length;
                }

                @Override
                public Point next() {
                    if(!hasNext()){
                        throw new NoSuchElementException("Элементов больше нет, ы");
                    }
                    return points[currIndex++];
                }
            };
        }finally {
            lock.unlock();
        }
    }

    public <T> T doSynchronously(Operation<T> operation) {
        lock.lock();
        try {
            return operation.apply(this);
        } finally {
            lock.unlock();
        }
    }

    @FunctionalInterface
    public interface Operation<T> {
        T apply(SynchronizedTabulatedFunction function);
    }
}
