import sample.callable.CallableVoid;

import java.util.Random;

public class VoidOperation implements CallableVoid {
    @Override
    public String randomNumber() {
        Random generator = new Random();
        return Double.toString(generator.nextDouble());
    }
}
