package concurrent;

import functions.*;
import functions.factory.LinkedListTabulatedFunctionFactory;

public class ReadWriteTaskExecutor {

    public static void main(String[] args) {
        final Object lock = new Object();

        ConstantFunction constantFunction = new ConstantFunction(-1.0);

        TabulatedFunction tabulatedFunction = new LinkedListTabulatedFunction(constantFunction, 1, 1000, 10);
        System.out.printf("Табулированная функция: %s на интервале [%f, %f]%n",
                constantFunction, 1.0, 1000.0);
        System.out.println("WriteTask будет устанавливать все Y = 0.5");
        System.out.println("===========================");
        System.out.flush();

        ReadTask readTask = new ReadTask(tabulatedFunction, lock);
        WriteTask writeTask = new WriteTask(tabulatedFunction, 0.5, lock);

        Thread readThread = new Thread(readTask, "ReadThread");
        Thread writeThread = new Thread(writeTask, "WriteThread");

        writeThread.start();

        try{
            Thread.sleep(50);
        }catch (InterruptedException e){
            e.printStackTrace();
        }

        readThread.start();

        try{
            writeThread.join();
            readThread.join();

            System.out.println("\nПроверка финальных значений функции:");
            boolean allCorrect = true;

            for (int i = 0; i < tabulatedFunction.getCount(); i++){
                double y = tabulatedFunction.getY(i);
                if (Math.abs(y - 0.5) > 1e-6){
                    System.out.printf("Ошибка: для индекса %d значение y = %f", i, y);
                    allCorrect = false;
                }
            }
            if (allCorrect){
                System.out.println("Все значения корректны и равны 0.5");
            }
        }catch (InterruptedException e){
            e.printStackTrace();
        }

    }
}
