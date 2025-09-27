package concurrent;

import functions.TabulatedFunction;

public class ReadTask implements Runnable{

    private final TabulatedFunction function;
    private final Object lock;

    public ReadTask(TabulatedFunction function, Object lock){
        this.function = function;
        this.lock = lock;

    }

    @Override
    public void run(){
        try {
            for (int i = 0; i < function.getCount(); i++){
                synchronized (lock){
                    double x = function.getX(i);
                    double y = function.getY(i);
                    System.out.println("After read: i = " + i + ", x = " + String.format("%.6f", x) + ", y = " + String.format("%.6f", y));
                    System.out.flush();

                    lock.notify();

                    if (i < function.getCount() - 1){
                        lock.wait();
                    }
                }
            }
        }catch (InterruptedException e){
            Thread.currentThread().interrupt();
        }

    }
}
