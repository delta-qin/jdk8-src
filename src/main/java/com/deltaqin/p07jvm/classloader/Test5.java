package com.deltaqin.p07jvm.classloader;

/**
 * @author deltaqin
 * @date 2021/6/1 下午3:54
 */

// 类的加载和初始化

//    ClassLoader.getSystemClassLoader().loadClass不是对类的主动使用，只是加载不初始化
//        Class.forName 才会初始化
public class Test5 {
    public static void main(String[] args) throws ClassNotFoundException {
        ClassLoader systemClassLoader = ClassLoader.getSystemClassLoader();

        Class<?> aClass = systemClassLoader.loadClass("com.deltaqin.p07jvm.classloader.Parent5");
        System.out.println(aClass);
        System.out.println("----------");

        Class<?> aClass1 = Class.forName("com.deltaqin.p07jvm.classloader.Parent5");
        System.out.println(aClass1);
    }
}

class Parent5 {
    static  int a = 1;

    static  {
        System.out.println("p5");
    }
}

