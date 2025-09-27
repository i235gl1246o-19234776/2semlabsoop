package concurrent;

import functions.*;

public class ReadWriteTaskExecutor {

    public static void main(String[] args) {
        ConstantFunction constantFunction = new ConstantFunction(-1.0);

        double xFrom = 1.0;
        double xTo = 1000.0;
        int pointCount = 1000;

        TabulatedFunction tabulatedFunction = new LinkedListTabulatedFunction(constantFunction, xFrom, xTo, pointCount);

        ReadTask readTask = new ReadTask(tabulatedFunction);
        WriteTask writeTask = new WriteTask(tabulatedFunction, 0.5);

        Thread readThread = new Thread(readTask);
        Thread writeThread = new Thread(writeTask);

        System.out.println("Запуск потоков чтения и записи пип пип пип");
        System.out.println("Исходная функция: ConstantFunction(-1.0) на интервале [1.0, 1000.0]");

        System.out.println("WriteTask будет устанавливать все Y в 0.5");
        System.out.println("ВИУ ВИУ ВИУ ====================================== ВИУ ВИУ ВИУ");

        try{
            readThread.join();
            writeThread.join();
        } catch(InterruptedException e){
            e.printStackTrace();
        }

        System.out.println("Оба потока завершены");

        // Проверяем финальное состояние функции
        System.out.println("Проверка финальных значений функции:");
        for (int i = 0; i < Math.min(10, tabulatedFunction.getCount()); i++) {
            System.out.printf("Index %d: x = %f, y = %f%n",
                    i, tabulatedFunction.getX(i), tabulatedFunction.getY(i));
        }
        if (tabulatedFunction.getCount() > 10) {
            System.out.println("... и еще " + (tabulatedFunction.getCount() - 10) + " точек");
        }

    }
}
