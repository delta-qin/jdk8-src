package com.deltaqin.algo.code00_test;

import java.util.*;
public class Main {
    public static void main(String[] args) {
        //Scanner in = new Scanner(System.in);
        //int a = in.nextInt();
        //System.out.println(a);
        //System.out.println("Hello World!");
        int i = func1(4);
        System.out.println(i);
    }

    public static int func1(int n) {

        int sum = 0;
        if (n < 3) {
            return n;
        }
        int first = 1;
        int second = 2;
        for (int i = 2; i < n; i++) {
            sum = first + second;
            first = second;
            second = sum;
        }
        return sum;
    }

    public static int func2 (int n) {
        if (n < 3) {
            return n;
        }
        return func2(n - 1) + func2(n - 2);
    }
}
