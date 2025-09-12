package functions;

public class RungeKuttaFunction implements MathFunction{

    private final MathFunction dEquation;
    private final double x0;
    private final double y0;
    private final double step;

    public RungeKuttaFunction(MathFunction dEquation, double x0, double y0, double step){
        this.dEquation = dEquation;
        this.x0 = x0;
        this.y0 = y0;
        this.step = step;
    }

    @Override
    public double apply(double x){
        if (x == x0){
            return y0;
        }

        int steps = (int)java.lang.Math.round((x - x0)/step);
        double currX = x0;
        double currY = y0;

        for(int i = 0; i < steps; i++){
            currY = rungeKuttaStep(currX, currY, step);
            currX += step;
        }

        return currY;
    }

    private double rungeKuttaStep(double x, double y, double h){
        double k1 = h * dEquation.apply(x);
        double k2 = h * dEquation.apply(x + h/2);
        double k3 = h * dEquation.apply(x + h/2);
        double k4 = h * dEquation.apply(x + h);

        return y + (k1 + 2*k2 + 2*k3 + k4) / 6;
    }
}
