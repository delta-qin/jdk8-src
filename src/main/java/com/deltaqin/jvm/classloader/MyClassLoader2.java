package com.deltaqin.jvm.classloader;

import java.io.*;

/**
 * @author deltaqin
 * @date 2021/6/1 下午6:42
 */



// ClassLoader 是抽象的，是不能被实例化的

//    想使用自己的，保证构建之后classpath下没有要加载的类，有的话就被被系统类加载器加载

//    主要是明确  命名空间  指的是什么。什么情况下一个类会被就加载多次，
//    就是说，每一个类加载器都和自己的父类有一个共同的命名空间
//    同一个父类的子类，他们自己加载的类所在的空间是隔离的，他们被父类加载的类是在同一个共享的命名空间的
public class MyClassLoader2 extends ClassLoader {

    // 标示性的作用
    private String classLoaderName;

    // 指定这个类加载器加载类的路径（这个路径下的所有的类都是这个类加载器加载的）
    private String path;

    private final String fileExtension = ".class";

    public MyClassLoader2(String classLoaderName) {
        // super 返回的类加载器是系统类加载器
        // this(checkCreateClassLoader(), getSystemClassLoader());
        super();
        this.classLoaderName = classLoaderName;
    }

    public void  setPath(String path) {
        this.path = path;
    }

    public MyClassLoader2(ClassLoader parent, String classLoaderName){
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
        // 这里写错了
        return this.defineClass(name, bytes, 0, bytes.length);
    }

    // 加载字节数组，首先是从哪里加载
    // 之后是转换为流读取为字节数组返回
    private byte[] loadByteData(String name) {
        InputStream is = null;
        byte[] data = null;
        ByteArrayOutputStream byteArrayOutputStream = null;
        name = name.replace(".", "/");

        try {

            // 注意这个是可以指定自己的类加载路径的
            is = new FileInputStream(new File(this.path + name + this.fileExtension));
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
        // 1. 两个类加载器（构建之后删除，父类的路径下没有这个类，就会自己去加载），两个命名空间，产生的两个类是不一样的
        // 2. 委托给父类的时候（构建之后不删除），是父类这一个命名空间，产生的类是一样的
        // 3. 一个作为另一个的父类，只会用第一个加载，产生的类是一样的

        // 但是对象肯定是不一样的
        MyClassLoader2 myClassLoader = new MyClassLoader2("loader1");
        myClassLoader.setPath("/Users/deltaqin/Desktop/");
        Class<?> aClass = myClassLoader.loadClass("com.deltaqin.jvm.classloader.Test1");
        System.out.println(aClass.hashCode());
        Object o = aClass.newInstance();
        System.out.println(o);
        System.out.println(o.getClass().getClassLoader());


        // myClassLoader 作为loader2的父加载器
        // 类加载器不是树形的，而是包含的
        // 所以处于同一层次的类加载器就可以成为了父子关系
        MyClassLoader2 myClassLoader1 = new MyClassLoader2(myClassLoader, "loader2");
        myClassLoader1.setPath("/Users/deltaqin/Desktop/");
        Class<?> bClass = myClassLoader1.loadClass("com.deltaqin.jvm.classloader.Test1");
        System.out.println(bClass.hashCode());
        Object o1 = aClass.newInstance();
        System.out.println(o1);
        System.out.println(o1.getClass().getClassLoader());

        // 没删除构建目录下 的类的时候：
        // 189568618
        //com.deltaqin.jvm.classloader.Test1@2f4d3709
        //sun.misc.Launcher$AppClassLoader@18b4aac2
        //189568618
        //com.deltaqin.jvm.classloader.Test1@4e50df2e
        //sun.misc.Launcher$AppClassLoader@18b4aac2


        // 删除构建目录下 的类
        // 189568618
        //com.deltaqin.jvm.classloader.Test1@2f4d3709
        //sun.misc.Launcher$AppClassLoader@18b4aac2
        //189568618
        //com.deltaqin.jvm.classloader.Test1@4e50df2e
        //sun.misc.Launcher$AppClassLoader@18b4aac2


        //
    }
}
