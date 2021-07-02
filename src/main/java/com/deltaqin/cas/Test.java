package com.deltaqin.cas;


import java.util.concurrent.atomic.AtomicInteger;

public class Test {
    // CAS
    // 判断主内存某个位置的值是否为跟期望值⼀样，相同就进⾏修改，否则⼀直重试，直到⼀致 为⽌。这个过程是原⼦的。
    @org.junit.Test
    public void test(){
        AtomicInteger atomicInteger = new AtomicInteger(1);
        System.out.println(        atomicInteger.compareAndSet(1,6)
                        + "\t当前值：" + atomicInteger.get());
        System.out.println(        atomicInteger.compareAndSet(1,5)
                        + "\t当前值：" + atomicInteger.get());
    }

    class MyData{
        volatile int number = 0;

        public void add(){
            number++;
        }

        AtomicInteger atomicInteger = new AtomicInteger();

        public void atomicAdd() {
            atomicInteger.getAndIncrement();
        }

        public void setTo60(){
            number = 60;
        }
    }
}
