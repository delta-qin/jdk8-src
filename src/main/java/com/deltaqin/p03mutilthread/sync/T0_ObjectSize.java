package com.deltaqin.p03mutilthread.sync;

import org.openjdk.jol.info.ClassLayout;

import java.util.concurrent.TimeUnit;

// 查看对象头的信息
public class T0_ObjectSize {

    public static void main(String[] args) throws InterruptedException {
        // 睡眠5秒，等待偏向锁启动
        TimeUnit.SECONDS.sleep(5);
        Object o = new Object();

        // 大小端
        //new 出来是无锁的。Linux，win 是小端的
        //00000001 00000000 00000000 00000000
        // 反转之后：
        //00000000 00000000 00000000 00000001
        // hashcode是懒加载的，不调用就不会有的
        System.out.println(ClassLayout.parseInstance(o).toPrintable());

        // 11000000 10011001 01110100 00001110
        // 11000000 中最后两位是00，是偏向锁
        // JVM启动的时候默认会延迟启动偏向锁。可以使用-XX:-UseBiasedLocking关闭偏向锁
        // 因为自己的程序会大量存在同步块，自己会启动十几个线程，自己会new一些同步块，为了避免锁升级的过程。
        synchronized (o){
            System.out.println(ClassLayout.parseInstance(o).toPrintable());
        }

        // 未启动偏向锁，没有加锁
        // 00000001 00000000 00000000 00000000
        // 启动偏向锁，无锁状态（匿名偏向，也就是可偏向，预备偏向但是还没有偏向，真正成为偏向锁的时候看一下前面的就可以知道自己实际指向的是谁，）
        // 00000101 00000000 00000000 00000000
        // 有同步块
        // 00000101 10011000 10000000 10010100

    }
}
