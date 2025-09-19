package operations;

import functions.TabulatedFunction;
import functions.Point;
import java.util.Iterator;

public class TabulatedFunctionOperationService {

    public static Point[] asPoints(TabulatedFunction tabulatedFunction) {
        if (tabulatedFunction == null) {
            throw new IllegalArgumentException("TabulatedFunction cannot be null");
        }

        int count = tabulatedFunction.getCount();
        Point[] points = new Point[count];

        int i = 0;
        Iterator<Point> iterator = tabulatedFunction.iterator();
        while (iterator.hasNext()) {
            Point originalPoint = iterator.next();
            points[i] = new Point(originalPoint.x, originalPoint.y);
            i++;
        }

        return points;
    }
}