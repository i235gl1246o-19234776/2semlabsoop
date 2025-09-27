package concurrent;

import functions.TabulatedFunction;

public class MultiplyingTask implements Runnable{

    private final TabulatedFunction function;

    public MultiplyingTask(TabulatedFunction function) {
        this.function = function;
    }

    @Override
    public void run() {
        for (int i = 0; i < function.getCount(); i++) {
            double x = function.getX(i);
            double y = function.getY(i);
            function.setY(i, y * 2);
        }

        System.out.println(Thread.currentThread().getName() + " закончил выполнение задачи.");
    }
}
