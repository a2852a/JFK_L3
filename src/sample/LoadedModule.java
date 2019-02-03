package sample;

import sample.callable.CallableBoolean;
import sample.callable.CallableDouble;
import sample.callable.CallableString;
import sample.callable.CallableVoid;

public class LoadedModule {

    private Class c;
    private String suffix;
    private Object instance;


    public LoadedModule(Class c, String suffix, Object instance){
        this.c = c;
        this.suffix = suffix;
        this.instance = instance;
    }

    public Object InvokeMethod(String key, Object args[]) {

        String methodName = getNativeMethodName(key);

        if (CallableBoolean.class.isAssignableFrom(c))
            return invokeCallableBoolean(methodName, args);
        else if (CallableDouble.class.isAssignableFrom(c))
            return invokeCallableDouble(methodName, args);
        else if (CallableString.class.isAssignableFrom(c))
            return invokeCallableString(methodName, args);
        else if (CallableVoid.class.isAssignableFrom(c))
            return invokeCallableVoid(methodName);

        return null;
    }

    private Double invokeCallableDouble(String methodName, Object args[]) {

        parseArgsToDouble(args);

        switch (methodName) {
            case "pow": {
                return ((CallableDouble) instance).pow((Double) args[0], (Double) args[1]);
            }
            case "oppositeNumber": {
                return ((CallableDouble) instance).oppositeNumber((Double) args[0]);
            }

        }
        return null;
    }

    private Boolean invokeCallableBoolean(String methodName, Object args[]) {

        parseArgsToDouble(args);

        switch (methodName) {
            case "equal": {
                return ((CallableBoolean) instance).equal((Double) args[0], (Double) args[1]);
            }
            case "greater": {
                return ((CallableBoolean) instance).greater((Double) args[0], (Double) args[1]);
            }

        }
        return null;
    }

    private String invokeCallableString(String methodName, Object args[]) {

        switch (methodName) {

            case "toUpperCase": {
                return ((CallableString) instance).toUpperCase((String) args[0]);
            }
            case "toLowerCase": {
                return ((CallableString) instance).toLowerCase((String) args[0]);
            }
            case "concatStrings": {
                return ((CallableString) instance).concatStrings((String) args[0], (String) args[1]);
            }

        }
        return null;
    }

    private String invokeCallableVoid(String methodName) {

        switch (methodName) {
            case "randomNumber": {
                return ((CallableVoid) instance).randomNumber();
            }
        }

        return null;
    }

    private void parseArgsToDouble(Object[] args){

        for (int i = 0; i < args.length; i++) {
            try {
                args[i] = Double.parseDouble((String) args[i]);
            } catch (NumberFormatException e) {
                continue;
            }
        }

    }


    private String getNativeMethodName(String key) {

        if(suffix == null || suffix.equals("")) return key;

        int indexOfSuffix = key.indexOf(suffix);

        if (indexOfSuffix != -1)
            return key.substring(0, indexOfSuffix);
        else
            return key;


    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public Object getInstance() {
        return instance;
    }

    public void setInstance(Object instance) {
        this.instance = instance;
    }

    public Class getC() {
        return c;
    }

    public void setC(Class c) {
        this.c = c;
    }
}
