package com.deltaqin.atest;

/**
 * @author deltaqin
 * @date 2021/8/28 下午4:28
 */
public class StaticTest {
    public static void main(String[] args) {
        System.out.println(D.i);
    }
}
class D {
    static {
        i = 2;
        System.out.println("D : 静态代码块1");
    }
    static int i;
}
