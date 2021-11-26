package com.deltaqin.p07jvm.classloader;

/**
 * @author deltaqin
 * @date 2021/6/1 下午3:31
 */
public class Test3 {
    static {
        System.out.println("main");
    }
    public static void main(String[] args) {
        Parent1 parent; // 声明不是主动使用，不会输出任何东西
        System.out.println("---");
        parent = new Parent1(); // 主动使用
        System.out.println("---");
        System.out.println(parent.a); //
        System.out.println("---");
        System.out.println(Child1.b);
    }
}

class Parent1 {
    static int a = 1;

    static  {
        System.out.println("a");
    }
}

class Child1 extends Parent1 {
    static int b = 2;
    static  {
        System.out.println("b");
    }
}
