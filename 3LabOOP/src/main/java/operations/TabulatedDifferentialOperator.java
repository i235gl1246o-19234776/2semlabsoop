package operations;

import functions.TabulatedFunction;
import functions.Point;
import functions.factory.TabulatedFunctionFactory;
import functions.factory.ArrayTabulatedFunctionFactory;


public class TabulatedDifferentialOperator implements DifferentialOperator<TabulatedFunction> {

    private TabulatedFunctionFactory factory;

    // Конструктор с фабрикой
    public TabulatedDifferentialOperator(TabulatedFunctionFactory factory) {
        this.factory = factory;
    }

    // Конструктор по умолчанию
    public TabulatedDifferentialOperator() {
        this.factory = new ArrayTabulatedFunctionFactory();
    }

    // Геттер для фабрики
    public TabulatedFunctionFactory getFactory() {
        return factory;
    }

    // Сеттер для фабрики
    public void setFactory(TabulatedFunctionFactory factory) {
        this.factory = factory;
    }

    @Override
    public TabulatedFunction derive(TabulatedFunction function) {
        // Получаем точки функции
        Point[] points = TabulatedFunctionOperationService.asPoints(function);
        int count = points.length;

        // Создаем массивы для x и y значений
        double[] xValues = new double[count];
        double[] yValues = new double[count];

        // Заполняем xValues (они остаются теми же)
        for (int i = 0; i < count; i++) {
            xValues[i] = points[i].x;
        }

        // Вычисляем производные (численное дифференцирование)
        // Для первой точки используем правостороннюю разность
        if (count > 1) {
            yValues[0] = (points[1].y - points[0].y) / (points[1].x - points[0].x);
        } else {
            yValues[0] = 0; // Если только одна точка, производная не определена
        }

        // Для внутренних точек используем центральную разность
        for (int i = 1; i < count - 1; i++) {
            yValues[i] = (points[i + 1].y - points[i - 1].y) / (points[i + 1].x - points[i - 1].x);
        }

        // Для последней точки используем левостороннюю разность
        if (count > 1) {
            yValues[count - 1] = (points[count - 1].y - points[count - 2].y) /
                    (points[count - 1].x - points[count - 2].x);
        }

        // Создаем новую табулированную функцию через фабрику
        return factory.create(xValues, yValues);
    }
}