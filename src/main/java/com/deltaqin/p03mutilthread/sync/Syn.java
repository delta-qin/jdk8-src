package com.deltaqin.p03mutilthread.sync;

/**
 * @author deltaqin
 * @date 2021/4/6 9:32 上午
 */
public class Syn {
    public synchronized void test1() {
        System.out.println("hello");
    }

    public void test2() {
        synchronized (Syn.class) {
            System.out.println("hello");
        }
    }

    public static void main(String[] args) {
        Syn syn = new Syn();
        syn.test1();
        syn.test2();
    }
}
