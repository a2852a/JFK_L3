import sample.callable.CallableDouble;

public final class MathOperation implements CallableDouble {
    @Override
    public double oppositeNumber(double number){
        return 1.0 / number;
    }

    @Override
    public double pow(double x, double p){
        return Math.pow(x,p);
    }
}
