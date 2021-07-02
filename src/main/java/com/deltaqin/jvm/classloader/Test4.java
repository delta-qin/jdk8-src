package com.deltaqin.jvm.classloader;

/**
 * @author deltaqin
 * @date 2021/6/1 下午3:50
 */
public class Test4 {

    static {
        System.out.println("main");
    }

    public static void main(String[] args) {
        System.out.println(Child3.a);
        Child3.doSomething();
    }
}

class Parent3 {
    static  int a = 1;

    static void doSomething() {
        System.out.println("static m");
    }

    static {
        System.out.println("parent");
    }
}

class Child3 extends Parent3 {
    static  int b = 2;

    static {
        System.out.println("child");
    }
}
