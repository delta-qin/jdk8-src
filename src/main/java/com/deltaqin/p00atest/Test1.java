package com.deltaqin.p00atest;

/**
 * @author deltaqin
 * @date 2022/6/1 8:16 PM
 */
public class Test1 {
    public static int test() {
        int a = 1;
        int b = 2;
        return a + b;
    }

    public static int test2(boolean flag) {
        int a = 1;
        if (flag) {
            a = 2;
        } else {
            a = 3;
        }
        return a + 1;
    }

    public static void main(String[] args) {
        Test1.test();
    }
}
