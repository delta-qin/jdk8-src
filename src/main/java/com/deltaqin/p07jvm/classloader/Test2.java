package com.deltaqin.p07jvm.classloader;

/**
 * @author deltaqin
 * @date 2021/6/1 下午3:28
 */
public class Test2 {

    static {
        System.out.println("main");
    }
    public static void main(String[] args) {
        System.out.println(Child.b);
    }
}

class Parent {
    static int a = 1;

    static {
        System.out.println("parent");
    }
}


class Child extends  Parent {
    static int b = 2;

    static {
        System.out.println("child");
    }
}
