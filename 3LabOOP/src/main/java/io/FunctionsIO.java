package io;

import functions.TabulatedFunction;
import functions.Point;
import functions.factory.TabulatedFunctionFactory;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

import java.io.*;

public final class FunctionsIO {
    private FunctionsIO() {
        throw new UnsupportedOperationException("Не удается создать экземпляр служебного класса");
    }

    public static void writeTabulatedFunction(BufferedWriter writer, TabulatedFunction function) throws IOException {
        PrintWriter printWriter = new PrintWriter(writer);

        printWriter.println(function.getCount());

        for (Point point : function) {
            printWriter.printf("%f %f\n", point.x, point.y);
        }

        printWriter.flush();
    }

    public static void writeTabulatedFunction(BufferedOutputStream outputStream, TabulatedFunction function) throws IOException {
        DataOutputStream dataOutputStream = new DataOutputStream(outputStream);

        dataOutputStream.writeInt(function.getCount());

        for (Point point : function) {
            dataOutputStream.writeDouble(point.x);
            dataOutputStream.writeDouble(point.y);
        }

        dataOutputStream.flush();
    }


    public static TabulatedFunction readTabulatedFunction(BufferedReader reader, TabulatedFunctionFactory factory)
            throws IOException {

        try {
            String countLine = reader.readLine();
            if (countLine == null) {
                throw new IOException("Файл пустой");
            }

            int count = Integer.parseInt(countLine.trim());

            double[] xValues = new double[count];
            double[] yValues = new double[count];

            NumberFormat formatter = NumberFormat.getInstance(Locale.forLanguageTag("ru"));

            for (int i = 0; i < count; i++) {
                String line = reader.readLine();
                if (line == null) {
                    throw new IOException("Неожиданный конец файла");
                }

                String[] parts = line.split(" ");
                if (parts.length != 2) {
                    throw new IOException("Некорректный формат данных в строке: " + line);
                }

                // Парсим числа
                try {
                    xValues[i] = formatter.parse(parts[0].trim()).doubleValue();
                    yValues[i] = formatter.parse(parts[1].trim()).doubleValue();
                } catch (ParseException e) {
                    throw new IOException("Ошибка парсинга чисел в строке: " + line, e);
                }
            }

            return factory.create(xValues, yValues);

        } catch (NumberFormatException e) {
            throw new IOException("Некорректный формат числа", e);
        }
    }

    public static TabulatedFunction readTabulatedFunction(BufferedInputStream inputStream, TabulatedFunctionFactory factory) throws IOException {
        DataInputStream dataInputStream = new DataInputStream(inputStream);

        int count = dataInputStream.readInt();

        double[] xValues = new double[count];
        double[] yValues = new double[count];

        for (int i = 0; i < count; i++) {
            xValues[i] = dataInputStream.readDouble();
            yValues[i] = dataInputStream.readDouble();
        }

        return factory.create(xValues, yValues);
    }

    public static void serialize(BufferedOutputStream stream, TabulatedFunction function)
            throws IOException {

        // Создаем ObjectOutputStream для сериализации объекта
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(stream);

        // Сериализуем функцию
        objectOutputStream.writeObject(function);

        // Пробрасываем все данные из буфера в основной поток
        // Не закрываем поток, так как мы его не создавали
        objectOutputStream.flush();
    }

}
