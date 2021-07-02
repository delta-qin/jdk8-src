package com.deltaqin.mutilthread.sync;


import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.ReentrantLock;


public class T0_MultiThreadAndUnsafe {

    private static int total = 0;
    private static Object object = new Object();
    private static ReentrantLock lock = new ReentrantLock();

    public static void main(String[] args) throws InterruptedException {
        // 这里可以不使用10而是只使用1，这样下面写的时候await都阻塞，
        // for之后countdown直接减为0，所有人一致起跑线执行
        CountDownLatch countDownLatch = new CountDownLatch(1);

        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                try {
                    countDownLatch.await();
                    for (int j = 0; j < 1000; j++) {
                        try {
                            // 2. 显式锁
                            //lock.lock();
                            //
                            // 1. JVM自动隐式锁
                            // synchronized (object){
                            total++; // 临界资源，不可访问
                            //}

                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            lock.unlock();
                        }
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        }

        Thread.sleep(1000);

        countDownLatch.countDown();

        Thread.sleep(2000);

        System.out.println(total);
    }
}
