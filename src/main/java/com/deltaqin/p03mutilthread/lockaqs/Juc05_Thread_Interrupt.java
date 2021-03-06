package com.deltaqin.p03mutilthread.lockaqs;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.locks.ReentrantLock;


@Slf4j
public class Juc05_Thread_Interrupt {

    private static ReentrantLock lock = new ReentrantLock(true);

    public static void reentrantLock() throws InterruptedException {
        String threadName = Thread.currentThread().getName();
        boolean flag = false;
        // 抛异常，自己去处理
        lock.lockInterruptibly();
        log.info("Thread:{},加锁成功!", threadName);
        while (true) {
            // 自循环里面设置的，作为开发者，自己处理中断
            if (Thread.interrupted()) {
                break;
            }
            //逻辑,批处理数据
            //逻辑
        }
        lock.unlock();
        log.info("Thread:{},锁退出同步块", threadName);
    }

    public static void main(String[] args) {
        Thread t0 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    reentrantLock();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "t0");
        t0.start();

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    reentrantLock();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    //异常处理
                }
            }
        }, "t1");
        t1.start();

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        t1.interrupt();

    }

}
