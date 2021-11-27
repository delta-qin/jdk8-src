package com.deltaqin.p03mutilthread;

import org.junit.Test;
import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.LongStream;

public class Main {

    @Test
    public void test() throws InterruptedException {
        vRunnable v = new vRunnable();

        for(int i=0; i<10; i++){
            new Thread(v).start();
        }
        Thread.sleep(1000);
        System.out.println("end");

    }

    @Test
    public void test2() {
        vRunnable a = new vRunnable();
        for(int i=0; i<10; i++) {
            new Thread(a).start();
        }
    }

    @Test
    public void test3(){
        tRunnable t = new tRunnable();

        new Thread(t, "1号").start();
        new Thread(t, "2号").start();
        new Thread(t, "3号").start();
    }

    @Test
    public void test4() {
        ARunnable a = new ARunnable();
        new Thread(new Runnable(){
            @Override
            public void run() {
                for(int i=0; i<10; i++){
                    a.loopA(i);
                }
            }
        }, "A").start();
        new Thread(new Runnable(){
            @Override
            public void run() {
                for(int i=0; i<10; i++){
                    a.loopB(i);
                }
            }
        }, "B").start();
        new Thread(new Runnable(){
            @Override
            public void run() {
                for(int i=0; i<10; i++){
                    a.loopC(i);
                }
            }
        }, "C").start();
    }

    @Test
    public void test5() {
        final Operator operator = new Operator();
        //创建5个读数据的线程
        for (int i = 0; i <5 ; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    while (true) {
                        operator.read();
                    }
                }
            }, "读线程"+i).start();
        }
        //创建5个写数据的线程
        for (int i = 0; i <5 ; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    while(true){
                        operator.write();
                    }
                }
            }, "写线程" + i).start();
        }
    }

    @Test
    public void test6() {
        Instant start=Instant.now();
        // 需要ForkJoinPool的支持
        ForkJoinPool pool=new ForkJoinPool();
        // Future的实现，有返回值
        ForkJoinTask<Long> task=new ForkJoinSumCalculate(0L, 50000000000L);
        Long sum=pool.invoke(task);


        System.out.println(sum);
        Instant end=Instant.now();
        // 耗费时间为：6204
        System.out.println("耗费时间为："+ Duration.between(start, end).toMillis());//耗费时间为：21020
    }

    //一般的方法
    @Test
    public void test7(){
        Instant start=Instant.now();
        long sum=0L;
        for(long i=0L;i<=50000000000L;i++){
            sum+=i;
        }
        System.out.println(sum);
        Instant end=Instant.now();
        // 耗费时间为：12740
        System.out.println("耗费时间为："+Duration.between(start, end).toMillis());//耗费时间为：27040
    }

    //java8 新特性
    @Test
    public void test8(){
        Instant start=Instant.now();
        Long sum= LongStream.rangeClosed(0L,50000000000L).parallel().reduce(0L, Long::sum);
        System.out.println(sum);
        Instant end= Instant.now();
        // 耗费时间为：3700
        System.out.println("耗费时间为："+ Duration.between(start, end).toMillis());//耗费时间为：14281
    }
}

class vRunnable implements Runnable{
    private volatile int num = 0;

    @Override
    public void run(){
        // 放大问题
        try{
            Thread.sleep(200);
        } catch(InterruptedException e){
            e.printStackTrace();
        }

        System.out.println(Thread.currentThread().getName()+ ": " + getnum());
    }

    public int getnum(){
        return num++;
    }
}


class tRunnable implements Runnable{
    private int ticket = 100;
    private Lock lock = new ReentrantLock();
    public void run(){
        while(ticket > 0){
            lock.lock();
            // 一旦sleep是不是就切换到别的线程继续执行了
            // 所以多线程问题就产生了。因为毕竟此时主存里面的变量已经加载到缓存里面了

            // 但是即使满足了内存可见性。由于不是互斥的，对于ticket--这种非原子操作，也会导致出现异常结果
            // 因为 i-- 是会出现 int temp = i;
            // 综上所述，多睡一会儿的作用就是为了让他有线程切换的机会
            try{
                try{
                    Thread.sleep(200);
                } catch(InterruptedException e){
                }
                System.out.println(Thread.currentThread().getName() + "售票剩余"+ticket--);
            } finally {
                // 一定写到finally里面
                lock.unlock();
            }
        }
    }
}


class ARunnable{
    // 当前执行的线程标志
    private int num = 1;

    private Lock lock = new ReentrantLock();
    private Condition con1 = lock.newCondition();
    private Condition con2 = lock.newCondition();
    private Condition con3 = lock.newCondition();

    public void loopA(int loopNum){
        lock.lock();

        try{
            if(num !=1){
                con1.await();
            }

            System.out.println(Thread.currentThread().getName()+"第"  + loopNum + "次循环");

            // 唤醒下一个
            num = 2;
            con2.signal();
        } catch(Exception e){

        }finally {
            lock.unlock();
        }
    }

    public void loopB(int loopNum){
        lock.lock();

        try{
            if(num !=2){
                con2.await();
            }

            System.out.println(Thread.currentThread().getName()+"第"  + loopNum + "次循环");
            // 唤醒下一个
            num = 3;
            con3.signal();
        } catch(Exception e){

        }finally {
            lock.unlock();
        }
    }

    public void loopC(int loopNum){
        lock.lock();

        try{
            if(num !=3){
                con3.await();
            }

            System.out.println(Thread.currentThread().getName()+"第"  + loopNum + "次循环");
            // 唤醒下一个
            num = 1;
            con1.signal();
        } catch(Exception e){

        }finally {
            lock.unlock();
        }
    }
}

class Operator {
    //private Lock lock = new ReentrantLock();
    private ReentrantReadWriteLock rwl
            = new ReentrantReadWriteLock();

    /**
     * 读操作，要添加读锁，希望多个线程同时读取，提高效率
     */
    public void read() {
        //lock.lock();
        rwl.readLock().lock();
        try {
            System.out.println(Thread.currentThread().getName()
                    + "开始读取数据........");
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName()
                    + "读取数据完毕.............");
        } finally {
            //lock.unlock();
            rwl.readLock().unlock();
        }
    }

    /**
     * 写操作，要使用写锁，保证安全性，只有一个线程独占
     */
    public void write() {
        //lock.lock();
        rwl.writeLock().lock();
        try {
            System.out.println(Thread.currentThread().getName()
                    + "开始写数据........");
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName()
                    + "写数据完毕.............");
        } finally {
            //lock.unlock();
            rwl.writeLock().unlock();
        }
    }
}

class ForkJoinSumCalculate extends RecursiveTask<Long> {
    private static final long serialVersionUID=-54565646543212315L;

    private long start;
    private long end;

    private static final long THURSHOLD=10000L;//临界值，小于这个值就不拆了，直接运算

    public ForkJoinSumCalculate(long start,long end){
        this.start=start;
        this.end=end;
    }
    @Override
    protected Long compute() {
        long length=end-start;
        if(length<=THURSHOLD){
            long sum=0L;
            for(long i=start;i<=end;i++){
                sum+=i;
            }
            return sum;
        }else{
            //进行拆分，同时压入线程队列
            long middle=(start+end)/2;
            ForkJoinSumCalculate left=new ForkJoinSumCalculate(start, middle);
            left.fork();
            ForkJoinSumCalculate right=new ForkJoinSumCalculate(middle+1, end);
            right.fork();
            return left.join()+right.join();
        }
    }

}
