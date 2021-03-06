package sample;

import jdk.jfr.Description;
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

public class ModuleLoader {

    private HashMap<String, MethodListItem> methodList;
    private LinkedList<LoadedModule> moduleList;

    private static ModuleLoader instance;

    private File directory;

    private ModuleLoader() {
        methodList = new HashMap<>();
        moduleList = new LinkedList<>();
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

    private void readClassFile(File file) throws Exception {
        String path = file.getParent();
        String className = file.getName();
        className = className.substring(0, className.indexOf("."));

        file = new File(path);

        URL url = file.toURI().toURL();
        URL[] urls = new URL[]{url};

        ClassLoader cl = new URLClassLoader(urls);
        Class<?> c = cl.loadClass(className);

        assignClassMethods(c);

    }

    public boolean loadClasses() throws Exception {
        int filesLoaded = 0;
        for (File file : directory.listFiles()) {
            if (file.getName().endsWith(".jar")) {
                readJarFile(file);
                filesLoaded++;
            } else if (file.getName().endsWith(".class")) {
                readClassFile(file);
                filesLoaded++;
            }
        }
        if (filesLoaded != 0)
            return true;
        else
            return false;
    }

    private void assignClassMethods(Class c) throws Exception {

        if (CallableDouble.class.isAssignableFrom(c))
            addModule(CallableDouble.class, c);


        else if (CallableString.class.isAssignableFrom(c))

            addModule(CallableString.class, c);


        else if (CallableBoolean.class.isAssignableFrom(c))

            addModule(CallableBoolean.class, c);


        else if (CallableVoid.class.isAssignableFrom(c))

            addModule(CallableVoid.class, c);


    }

    private void addModule(Class recognizedClass, Class inputClass) throws Exception {
        String suffix = checkForExisting(inputClass);
        LoadedModule newModule = new LoadedModule(recognizedClass, suffix, inputClass.getConstructor().newInstance());

        String description = "";
        Description[] descriptions = (Description[]) inputClass.getAnnotationsByType(Description.class);
        if (descriptions.length > 0) {
            for (Description desc : descriptions) {
                description += desc.value();
            }
            newModule.setDescription(description);
        }

        moduleList.add(newModule);
        addMethodsToMap(suffix, recognizedClass.getMethods(), newModule);

    }


    private void addMethodsToMap(String suffix, Method[] methods, LoadedModule loadedModule) {
        for (Method method : methods) {
            int paramCount = method.getParameterCount();
            methodList.put(method.getName() + suffix, new MethodListItem(loadedModule, paramCount));
        }

    }


    private String checkForExisting(Class c) {

        int repeats = 0;
        for (LoadedModule module : moduleList) {

            for (Class cInterface : c.getInterfaces()) {
                if (module.getC() == cInterface) {
                    repeats++;
                    break;
                }
            }

        }
        if (repeats == 0) return "";
        else
            return "[" + repeats + "]";
    }


    public LinkedList<String> getLoadedMethodNames() {
        LinkedList<String> methodNameList = new LinkedList<>();

        for (String methodName : methodList.keySet()) {
            methodNameList.add(methodName);
        }
        return methodNameList;

    }

    public String invokeMethod(Object key, Object args[]) {

        return String.valueOf(methodList.get(key).getModule().InvokeMethod((String) key, args));

    }


    public int getMethodParamCount(String key) {
        return methodList.get(key).getParamNumber();
    }

    public File getDirectory() {
        return directory;
    }

    public void setDirectory(File directory) {
        this.directory = directory;
    }

    public String getDescription(String key) {
        String description = methodList.get(key).getModule().getDescription();

        if (description == null) return "No description available";
        else
            return description;

    }


}
