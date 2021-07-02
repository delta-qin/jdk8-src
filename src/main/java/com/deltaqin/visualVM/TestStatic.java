package com.deltaqin.visualVM;

/**
 * @author deltaqin
 * @date 2021/5/30 9:20 下午
 */

// -XX:+TraceClassLoading 可以显示类的加载过程

public class TestStatic {

    //     启动类主动使用
    public static void main(String[] args) {
        System.out.println(Child.str);
        // 不调用下面的方法是不会初始化子类的
        //System.out.println(Child.string);
    }
}

class Parent {
    public static String str = "deltaqin";

    static {
        System.out.println("parent");
    }
}

class Child extends Parent {
    public  static String string = "child";

    static {
        System.out.println("parent");
    }
}
