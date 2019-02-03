import jdk.jfr.Description;
import sample.callable.CallableVoid;

import java.util.Random;

@Description("No input double output")
public class VoidOperation implements CallableVoid {
    @Override
    public String randomNumber() {
        Random generator = new Random();
        return Double.toString(generator.nextDouble());
    }
}
