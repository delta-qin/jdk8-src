package com.deltaqin.p07jvm.classloader;

/**
 * @author deltaqin
 * @date 2021/6/1 下午3:12
 */

// 几个获取类对象的方式

public class Test1 {

    public static void main(String[] args) throws ClassNotFoundException, IllegalAccessException, InstantiationException {

        // class 是关键字，不可以使用
        Class<?> clazz = Class.forName("java.lang.String");
        System.out.println("String: " + clazz.getClassLoader());
        //Object o = clazz.newInstance();

        Test1 test1 = new Test1();
        System.out.println(test1.getClass().getClassLoader());

        System.out.println(Test1.class.getClassLoader());

        Class<?> aClass = Class.forName("com.deltaqin.p07jvm.classloader.Test1");
        System.out.println(aClass.getClassLoader());
    }
}
