import jdk.jfr.Description;
import sample.callable.CallableString;

public final class StringOperation implements CallableString {
    @Override
    public String concatStrings(String s1, String s2) {
        return s1.concat(s2);
    }

    @Override
    public String toUpperCase(String s) {
        return "test";
    }

    @Override
    public String toLowerCase(String s) {
        return s.toLowerCase();
    }



}
