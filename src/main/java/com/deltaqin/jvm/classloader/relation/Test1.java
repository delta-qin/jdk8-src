package com.deltaqin.jvm.classloader.relation;

/**
 * @author deltaqin
 * @date 2021/6/1 下午4:20
 */
public class Test1 {
    public static void main(String[] args) {

        // 获取到系统类加载器
        ClassLoader classLoader = ClassLoader.getSystemClassLoader();

        System.out.println(classLoader);

        while (classLoader != null) {
            classLoader = classLoader.getParent();
            System.out.println(classLoader);
        }

    }
}

class Parent {

}
