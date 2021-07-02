package com.deltaqin.jvm.classloader;

import java.io.*;

/**
 * @author deltaqin
 * @date 2021/6/1 下午6:42
 */

// ClassLoader 是抽象的，是不能被实例化的

public class MyClassLoader extends ClassLoader {

    // 标示性的作用
    private String classLoaderName;

    private String path;

    private final String fileExtension = ".class";

    public MyClassLoader(String classLoaderName) {
        // super 返回的类加载器是系统类加载器
        // this(checkCreateClassLoader(), getSystemClassLoader());
        super();
        this.classLoaderName = classLoaderName;
    }

    public void  setPath(String path) {
        this.path = path;
    }

    public MyClassLoader(ClassLoader parent, String classLoaderName){
        // 显式指定该类加载器的父类加载器，而不是使用默认的父类加载器
        // this(checkCreateClassLoader(), parent);
        super(parent);
        this.classLoaderName = classLoaderName;
    }

    @Override
    public String toString() {
        return "[" + classLoaderName + "]";
    }

    // loadClass发现自己没有加载过，就会 在自己内部会调用这个方法
    // 一个类加载器必须重写findClass方法，但是loadClass 里面其实里面就实现了双亲委派，
    //想打破双亲委派就需要重写 loadClass
    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        System.out.println(name);
        System.out.println(classLoaderName);

        byte[] bytes = loadByteData(name);
        //
        return this.defineClass(classLoaderName, bytes, 0, bytes.length);
    }

    // 加载字节数组，首先是从哪里加载
    // 之后是转换为流读取为字节数组返回
    private byte[] loadByteData(String name) {
        InputStream is = null;
        byte[] data = null;
        ByteArrayOutputStream byteArrayOutputStream = null;
        this.classLoaderName = this.classLoaderName.replace(".", "/");

        try {

            // 注意这name是直接以Classpath为基础去加载类的，所以是不可以加载其他非classpath路径下的类
            is = new FileInputStream(new File(name + this.fileExtension));
            byteArrayOutputStream = new ByteArrayOutputStream();

            int ch = 0;

            while(-1 != (ch = is.read())) {
                byteArrayOutputStream.write(ch);
            }

            data = byteArrayOutputStream.toByteArray();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
                byteArrayOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return data;
    }


    public static void main(String[] args) throws IllegalAccessException, InstantiationException, ClassNotFoundException {
        MyClassLoader myClassLoader = new MyClassLoader("loader1");


        System.out.println("main   " + myClassLoader.getParent());
    }
}
