package sample;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class ModuleLoader {

    final private int compilationUnitLimit = 2;
    final private int classReadLimit = 2;

    private static ModuleLoader instance;

    private File directory;

    private CallableDouble callableDouble;



    public static ModuleLoader getInstance(){
        if(ModuleLoader.instance == null){
            instance = new ModuleLoader();
        }
        return instance;
    }


    private void readJarFile(File file) throws Exception{

        JarFile jarFile = null;
        try
        {
            jarFile = new JarFile(file);
            Enumeration<JarEntry> entries = jarFile.entries();

            URL[] urls = { new URL("jar:file:" + file + "!/") };
            URLClassLoader cl = URLClassLoader.newInstance(urls);

            while (entries.hasMoreElements()) {
                JarEntry je = entries.nextElement();
                if(je.isDirectory() || !je.getName().endsWith(".class"))
                {
                    continue;
                }

                String className = je.getName().substring(0, je.getName().length()-6);
                className = className.replace('/', '.');
                try
                {
                    Class<?> c = cl.loadClass(className);

                    if (CallableDouble.class.isAssignableFrom(c)) {
                        callableDouble = (CallableDouble) c.getDeclaredConstructor().newInstance();

                        if (null == callableDouble)
                            throw new Exception();
                    }

                    //System.out.println(callableDouble.pow(2.0, 2.0));
                    //System.out.println(callableDouble.oppositeNumber(10));
                }
                catch (ClassNotFoundException exp)
                {
                    continue;
                }

            }
        }
        catch (IOException exp)
        { }
        finally
        {
            if (null != jarFile)
                jarFile.close();
        }
    }

    private void loadClassFile(File file){

    }



    public boolean loadClasses(){
        for(File file : directory.listFiles()){
            try {
                if (file.getName().endsWith(".jar")) readJarFile(file);
            }catch (Exception e){return false;}
        }
        if(callableDouble == null) return false;
    return true;
    }


    public File getDirectory() {
        return directory;
    }

    public void setDirectory(File directory) {
        this.directory = directory;
    }

}
