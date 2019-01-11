import sample.callable.CallableBoolean;

public final class BooleanOperation implements CallableBoolean {
    @Override
    public boolean equal(double d1, double d2) {
        return d1 == d2;
    }

    @Override
    public boolean greater(double d1, double d2) {
        return d1 > d2;
    }
}
