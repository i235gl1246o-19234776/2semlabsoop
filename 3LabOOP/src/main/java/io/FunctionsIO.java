package io;

import functions.ArrayTabulatedFunction;
import functions.LinkedListTabulatedFunction;
import functions.TabulatedFunction;
import functions.Point;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

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

}