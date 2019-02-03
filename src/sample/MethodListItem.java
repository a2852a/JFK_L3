package sample;

public class MethodListItem {

    private LoadedModule module;
    private int paramNumber;

    MethodListItem(LoadedModule module, int paramNumber){
        this.module = module;
        this.paramNumber = paramNumber;
    }

    public LoadedModule getModule() {
        return module;
    }

    public void setModule(LoadedModule module) {
        this.module = module;
    }

    public int getParamNumber() {
        return paramNumber;
    }

    public void setParamNumber(int paramNumber) {
        this.paramNumber = paramNumber;
    }
}
