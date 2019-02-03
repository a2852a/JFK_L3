import jdk.jfr.Description;
import sample.callable.CallableDouble;

@Description("double input, double output")
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
