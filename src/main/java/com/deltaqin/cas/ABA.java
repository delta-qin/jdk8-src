package com.deltaqin.cas;

import org.junit.Test;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicStampedReference;

public class ABA {
    AtomicInteger atomicInteger = new AtomicInteger(1);
    static AtomicReference<Integer> atomicReference = new AtomicReference<>(1);
    static AtomicStampedReference<Integer> atomicStampedReference =
            new AtomicStampedReference<>(1,1);

    @Test
    public void aba(){
        new Thread(()->{
            atomicReference.compareAndSet(1,3);
            atomicReference.compareAndSet(3,1);
            atomicInteger.compareAndSet(1,3);
            atomicInteger.compareAndSet(3,1);
        }).start();

        new Thread(()-> {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(atomicReference.compareAndSet(1,4) + "\t"
                    + atomicReference.get());
            System.out.println(atomicInteger.compareAndSet(1,4) + "\t"
                    + atomicInteger.get());
        }).start();

        // 必须睡，主线程不会等待子线程。非阻塞的自旋锁
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void resolveABA() {
        new Thread(()->{

            int version = atomicStampedReference.getReference();
            System.out.println(Thread.currentThread().getName() + "1"+atomicStampedReference.getStamp());

            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            //atomicStampedReference.compareAndSet(3,1,version,version+1);
            atomicStampedReference.compareAndSet(1,3,
                    atomicStampedReference.getStamp(),atomicStampedReference.getStamp()+1);
            System.out.println(Thread.currentThread().getName() + "2"+atomicStampedReference.getStamp());

            atomicStampedReference.compareAndSet(3,1,
                    atomicStampedReference.getStamp(),atomicStampedReference.getStamp()+1);

            System.out.println(Thread.currentThread().getName() + "3"+atomicStampedReference.getStamp());
        },"ta").start();

        new Thread(()->{
            int stamp = atomicStampedReference.getStamp();
            System.out.println(Thread.currentThread().getName()+"1"+stamp);

            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // 版本号已经不一样了，所以修改失败，这里故意使用了之前的temp，而没有重新获取
            boolean b = atomicStampedReference.compareAndSet(1, 6,
                    stamp, stamp + 1);

            System.out.println(Thread.currentThread().getName() + b);

            System.out.println(Thread.currentThread().getName() + atomicStampedReference.getStamp());
        },"tb").start();

        try {
            TimeUnit.SECONDS.sleep(4);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
