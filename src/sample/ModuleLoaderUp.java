package sample;

import sample.callable.CallableBoolean;
import sample.callable.CallableDouble;
import sample.callable.CallableString;
import sample.callable.CallableVoid;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class ModuleLoaderUp {

    private HashMap<String,MethodListItem> methodList;
    private LinkedList<LoadedModule> moduleList;

    private HashMap<String, CallableDouble> callableDoubleHashMap;
    private HashMap<String, CallableString> callableStringHashMap;
    private HashMap<String, CallableBoolean> callableBooleanHashMap;
    private HashMap<String, CallableVoid> callableVoidHashMap;


    private static ModuleLoaderUp instance;

    private File directory;

    private ModuleLoaderUp(){
        methodList = new HashMap<>();
        moduleList = new LinkedList<>();
    }

    public static ModuleLoaderUp getInstance() {
        if (ModuleLoaderUp.instance == null) {
            instance = new ModuleLoaderUp();
        }
        return instance;
    }

    private void readJarFile(File file) throws Exception {

        JarFile jarFile = null;
        try {
            jarFile = new JarFile(file);
            Enumeration<JarEntry> entries = jarFile.entries();

            URL[] urls = {new URL("jar:file:" + file + "!/")};
            URLClassLoader cl = URLClassLoader.newInstance(urls);

            while (entries.hasMoreElements()) {
                JarEntry je = entries.nextElement();
                if (je.isDirectory() || !je.getName().endsWith(".class")) {
                    continue;
                }

                String className = je.getName().substring(0, je.getName().length() - 6);
                className = className.replace('/', '.');
                try {
                    Class<?> c = cl.loadClass(className);

                    assignClassMethods(c);
                    //assignClassMethods(c);

                } catch (ClassNotFoundException exp) {
                    continue;
                }

            }
        } catch (IOException exp) {
        } finally {
            if (null != jarFile)
                jarFile.close();
        }
    }

    private void readClassFile(File file) throws Exception{
        String path = file.getParent();
        String className = file.getName();
        className = className.substring(0,className.indexOf("."));

        file = new File(path);

        URL url = file.toURI().toURL();
        URL[] urls = new URL[]{url};

        ClassLoader cl = new URLClassLoader(urls);
        Class<?> c = cl.loadClass(className);

        assignClassMethods(c);

    }

    public boolean loadClasses() {
        for (File file : directory.listFiles()) {
            try {
                if (file.getName().endsWith(".jar")) readJarFile(file);
                else
                if(file.getName().endsWith(".class")) readClassFile(file);
            } catch (Exception e) {
                System.out.print(e);
                return false;
            }
        }
            return true;
    }

    private void assignClassMethods(Class c) throws Exception{

        if (CallableDouble.class.isAssignableFrom(c))
        {
            String additional = checkForExisting(c);
            String newKey = c.getName() + additional;
            LoadedModule newModule = new LoadedModule(CallableDouble.class,additional,c.getConstructor().newInstance());
                    moduleList.add(newModule);
            addMethodsToMap(additional,CallableDouble.class.getMethods(),newModule);
            //callableDoubleHashMap.put(newKey,(CallableDouble) c.getConstructor().newInstance());


        }
            //readCallableDoubleMethods(c);
        else if (CallableString.class.isAssignableFrom(c))
        {
            String additional = checkForExisting(c);
            String newKey = c.getName() + additional;
            LoadedModule newModule = new LoadedModule(CallableString.class,additional,c.getConstructor().newInstance());
                    moduleList.add(newModule);
             addMethodsToMap(additional,CallableString.class.getMethods(),newModule);
            //callableStringHashMap.put(newKey,(CallableString) c.getConstructor().newInstance());

        }
            //readCallableStringMethods(c);
        else if (CallableBoolean.class.isAssignableFrom(c))
        {
            String additional = checkForExisting(c);
            String newKey = c.getName() + additional;
            LoadedModule newModule = new LoadedModule(CallableBoolean.class,additional,c.getConstructor().newInstance());
            moduleList.add(newModule);
            addMethodsToMap(additional,CallableBoolean.class.getMethods(),newModule);
        }

        else if (CallableVoid.class.isAssignableFrom(c))
        {
            String additional = checkForExisting(c);
            String newKey = c.getName() + additional;
            LoadedModule newModule = new LoadedModule(CallableVoid.class,additional,c.getConstructor().newInstance());
            moduleList.add(newModule);
            addMethodsToMap(additional,CallableVoid.class.getMethods(),newModule);
        }
    }


    private void addMethodsToMap(String suffix, Method[] methods, LoadedModule loadedModule){
        for(Method method : methods){
            int paramCount = method.getParameterCount();
            methodList.put(method.getName()+suffix,new MethodListItem(loadedModule,paramCount));
        }

    }


    private String checkForExisting(Class c){

        int repeats = 0;
        for(LoadedModule module : moduleList){

            for(Class cInterface : c.getInterfaces()){
                if(module.getC() == cInterface){
                    repeats++;
                    break;
                }
            }

        }
        if(repeats == 0) return "";
        else
        return "[" + repeats + "]";
    }


    public LinkedList<String> getLoadedMethodNames() {
        //LinkedList<Method> methodList = new LinkedList<>(methodMap.values());
        LinkedList<String> methodNameList = new LinkedList<>();

        for (String methodName : methodList.keySet()) {
            methodNameList.add(methodName);
        }
        return methodNameList;

    }

    public String invokeMethod(Object key, Object args[]){

       return String.valueOf(methodList.get(key).getModule().InvokeMethod((String)key,args));

    }


    public int getMethodParamCount(String key){
        return methodList.get(key).getParamNumber();
    }

    public File getDirectory() {
        return directory;
    }

    public void setDirectory(File directory) {
        this.directory = directory;
    }


}