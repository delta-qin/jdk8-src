package com.deltaqin.visualVM;

import java.util.Random;
import java.util.UUID;

/**
 * @author deltaqin
 * @date 2021/5/30 9:21 下午
 */


public class TestFinal {

    public static void main(String[] args) {
        System.out.println(Child1.str);
    }
}

class Child1 {
    // 相当于是final的常量直接放到了调用类的常量池里面，和原来的类没有关系了
    //public  static  final String str = "deltaqin";


    // 不会合并，运行时期才可以确定的值是不会放到常量池里面的，即使是final
    public static final String  str = UUID.randomUUID().toString();

    static {
        System.out.println("child");
    }
}
