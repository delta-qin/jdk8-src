package com.deltaqin.mutilthread.atomic;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;


public class T1_AtomicInteger {

    public static int total = 0;
    static AtomicInteger atomic = new AtomicInteger(0);

    public static void main(String[] args) throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(10);

        for(int i=0;i<10;i++){
            new Thread(()->{
                for(int j=0;j<1000;j++){
                    /*synchronized () {
                        total++;//cas
                    }*/
                    atomic.getAndIncrement();
                }
                countDownLatch.countDown();
            }).start();
        }

        countDownLatch.await();
        System.out.println(atomic.get());
    }
}
