package sample;

import sample.callable.CallableBoolean;
import sample.callable.CallableDouble;
import sample.callable.CallableString;
import sample.callable.CallableVoid;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class ModuleLoader {

    private static ModuleLoader instance;

    private File directory;

    private CallableDouble callableDouble;
    private CallableString callableString;
    private CallableBoolean callableBoolean;
    private CallableVoid callableVoid;

    private HashMap<String, Method> methodMap;

    private ModuleLoader() {
        methodMap = new HashMap<>();
    }


    public static ModuleLoader getInstance() {
        if (ModuleLoader.instance == null) {
            instance = new ModuleLoader();
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
        if (callableDouble == null & callableBoolean == null
                & callableString == null & callableVoid == null)
            return false;
        else
        return true;
    }



    public LinkedList<String> getLoadedMethodNames() {
        LinkedList<Method> methodList = new LinkedList<>(methodMap.values());
        LinkedList<String> methodNameList = new LinkedList<>();

        for (Method method : methodList) {
            methodNameList.add(method.getName());
        }
        return methodNameList;

    }

    public String invokeMethod(Object key, Object args[]) throws IllegalAccessException, InvocationTargetException {
        Method invokedMethod = methodMap.get(key);
        int parCount = invokedMethod.getParameterCount();

        if (parCount != args.length)
            args = Arrays.copyOfRange(args, 0, parCount);


        for (int i = 0; i < args.length; i++) {
            try {
                args[i] = Double.parseDouble((String) args[i]);
            } catch (NumberFormatException e) {
                continue;
            }
        }

        if (isDeclaredInInterface(invokedMethod, CallableDouble.class))
            return methodMap.get(key).invoke(callableDouble, args).toString();
        else if (isDeclaredInInterface(invokedMethod, CallableString.class))
            return methodMap.get(key).invoke(callableString, args).toString();
        else if (isDeclaredInInterface(invokedMethod, CallableBoolean.class))
            return methodMap.get(key).invoke(callableBoolean, args).toString();
        else if (isDeclaredInInterface(invokedMethod, CallableVoid.class))
            return methodMap.get(key).invoke(callableVoid, args).toString();
        else
            return null;

    }

    public static boolean isDeclaredInInterface(Method method, Class<?> interfaceClass) {
        for (Method methodInInterface : interfaceClass.getMethods()) {
            if (methodInInterface.getName().equals(method.getName()))
                return true;
        }
        return false;
    }


    private void assignClassMethods(Class c) throws Exception{

        if (CallableDouble.class.isAssignableFrom(c))
            readCallableDoubleMethods(c);
        else if (CallableString.class.isAssignableFrom(c))
            readCallableStringMethods(c);
        else if (CallableBoolean.class.isAssignableFrom(c))
            readCallableBooleanMethods(c);
        else if (CallableVoid.class.isAssignableFrom(c))
            readCallableVoidMethods(c);

    }

    private void readCallableDoubleMethods(Class c) throws Exception {
        callableDouble = (CallableDouble) c.getDeclaredConstructor().newInstance();
        if (null == callableDouble) throw new Exception();

        Method m;
        for (Method method : CallableDouble.class.getMethods()) {
            m = callableDouble.getClass().getMethod(method.getName(), method.getParameterTypes());
            methodMap.put(m.getName(), m);
        }
    }

    private void readCallableStringMethods(Class c) throws Exception {
        callableString = (CallableString) c.getDeclaredConstructor().newInstance();
        if (null == callableString) throw new Exception();

        Method m;
        for (Method method : CallableString.class.getMethods()) {
            m = callableString.getClass().getMethod(method.getName(), method.getParameterTypes());
            methodMap.put(m.getName(), m);
        }
    }

    private void readCallableBooleanMethods(Class c) throws Exception {
        callableBoolean = (CallableBoolean) c.getDeclaredConstructor().newInstance();
        if (null == callableBoolean) throw new Exception();

        Method m;
        for (Method method : CallableBoolean.class.getMethods()) {
            m = callableBoolean.getClass().getMethod(method.getName(), method.getParameterTypes());
            methodMap.put(m.getName(), m);
        }
    }

    private void readCallableVoidMethods(Class c) throws Exception {
        callableVoid = (CallableVoid) c.getDeclaredConstructor().newInstance();
        if (null == callableVoid) throw new Exception();

        Method m;
        for (Method method : CallableVoid.class.getMethods()) {
            m = callableVoid.getClass().getMethod(method.getName(), method.getParameterTypes());
            methodMap.put(m.getName(), m);
        }
    }


    public File getDirectory() {
        return directory;
    }

    public void setDirectory(File directory) {
        this.directory = directory;
    }

    public HashMap<String, Method> getMethodMap() {
        return methodMap;
    }


}
