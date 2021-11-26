package com.deltaqin.p03mutilthread.lockaqs;

import java.util.concurrent.atomic.AtomicReference;

/**
 * 实现一个 可重入的自旋锁
 * @author deltaqin
 * @date 2021/10/4 下午5:35
 */
public class MyReentrantCASLock {
    private AtomicReference<Thread> owner = new AtomicReference<>();

    private int count = 0;

    public void lock() {
        Thread t = Thread.currentThread();
        if (t == owner.get()) {
            // 重入
            ++count;
            return;
        }

        // 期待之前没有线程获取锁，所以是期望值是null
        while (!owner.compareAndSet(null, t)) {
            System.out.println("自旋了");
        }
    }

    public void unlock() {
        Thread t = Thread.currentThread();
        //只有持有锁的线程才能解锁
        if (t == owner.get()) {
            if (count > 0) {
                --count;
            } else {
                //此处无需CAS操作，因为没有竞争，因为只有线程持有者才能解锁
                owner.set(null);
            }
        }
    }

    public static void main(String[] args) {
        MyReentrantCASLock myReentrantCASLock = new MyReentrantCASLock();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                System.out.println(Thread.currentThread().getName() + "开始尝试获取自旋锁");
                myReentrantCASLock.lock();
                try {
                    System.out.println(Thread.currentThread().getName() + "获取到了自旋锁");
                    Thread.sleep(4000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    myReentrantCASLock.unlock();
                    System.out.println(Thread.currentThread().getName() + "释放了了自旋锁");
                }
            }
        };
        Thread thread1 = new Thread(runnable);
        Thread thread2 = new Thread(runnable);
        thread1.start();
        thread2.start();
    }
}
