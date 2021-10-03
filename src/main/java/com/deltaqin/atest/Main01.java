package com.deltaqin.atest;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author deltaqin
 * @date 2021/8/24 下午6:51
 */
public class Main01 {
    private int num = 0;
    private static Lock myLock = new ReentrantLock();
    private static Condition conditionA = myLock.newCondition();
    private static Condition conditionB = myLock.newCondition();
    private static Condition conditionC = myLock.newCondition();

    public static void main(String[] args) {
        Main01 main01 = new Main01();
        new Thread(() -> {
            main01.proces1(0, conditionA, conditionB);
        }, "A").start();

        new Thread(() -> {
            main01.proces1(1, conditionB, conditionC);
        }, "B").start();

        new Thread(() -> {
            main01.proces1(2, conditionC, conditionA);
        }, "C").start();
    }

    public void proces1(int target, Condition condition1,
                        Condition condition2) {
        for (int i = 0; i < 10; i++) {
            try {
                myLock.lock();
                while (num % 3 != target) {
                    condition1.await();
                }
                num++;
                System.out.println(Thread.currentThread().getName());
                condition2.signal();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                myLock.unlock();
            }
        }
    }
}
