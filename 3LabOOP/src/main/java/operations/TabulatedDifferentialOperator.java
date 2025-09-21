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
        if (factory == null) {
            throw new IllegalArgumentException("TabulatedFunctionFactory не может быть равна null");
        }
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

        // Обработка разных случаев по количеству точек
        if (count == 1) {
            // Одна точка — производная не определена, возвращаем 0
            yValues[0] = 0;
        } else if (count == 2) {
            // Две точки — используем одинаковую производную (линейная аппроксимация)
            double h = points[1].x - points[0].x;
            double deriv = (points[1].y - points[0].y) / h;
            yValues[0] = deriv;
            yValues[1] = deriv;
        } else {
            // Три и более точки — используем формулы второго порядка на краях и центральную внутри

            // Предполагаем, что сетка равномерная (шаг h постоянный)
            double h = points[1].x - points[0].x;

            // Первая точка — правосторонняя разность второго порядка
            yValues[0] = (-3 * points[0].y + 4 * points[1].y - points[2].y) / (2 * h);

            // Внутренние точки — центральная разность (второго порядка)
            for (int i = 1; i < count - 1; i++) {
                yValues[i] = (points[i + 1].y - points[i - 1].y) / (2 * h);
            }

            // Последняя точка — левосторонняя разность второго порядка
            yValues[count - 1] = (3 * points[count - 1].y - 4 * points[count - 2].y + points[count - 3].y) / (2 * h);
        }

        // Создаем новую табулированную функцию через фабрику
        return factory.create(xValues, yValues);
    }
}