package com.deltaqin.p00atest;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.concurrent.*;

/**
 * @author deltaqin
 * @date 2021/10/3 上午9:30
 */
public class ProConTest {
    private BlockingQueue blockingQueue;
    private int max = 10;

    public ProConTest() {
        blockingQueue = new LinkedBlockingQueue(max);
    }

    public ProConTest(int max) {
        this.max = max;
        blockingQueue = new LinkedBlockingQueue(max);
    }

    public void produce(String produceName) {
        try {
            blockingQueue.put(produceName);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(Thread.currentThread().getName()  + "生产商品：" +
                 produceName + "目前的商品数量：" + blockingQueue.size());
    }

    public void consume() {
        String productName = null;
        try {
            productName = (String)blockingQueue.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(Thread.currentThread().getName() + "消费商品：" +
                productName + "目前商品数量：" + blockingQueue.size());
    }

    public static void main(String[] args) {
        ProConTest proConTest = new ProConTest();

        //new Thread(() -> {
        //    for (int i = 0; i < 20; i++) {
        //        proConTest.produce("" + i);
        //    }
        //}, "A").start();
        //
        //new Thread(() -> {
        //    for (int i = 0; i < 20; i++) {
        //        proConTest.consume();
        //    }
        //}, "B").start();

        ThreadFactory namedThreadFactory = new ThreadFactoryBuilder()
                .setNameFormat("demo-pool-%d").build();
        ExecutorService fixedThreadPool = new ThreadPoolExecutor(2, 2,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>(1024), namedThreadFactory,
                new ThreadPoolExecutor.AbortPolicy());

        fixedThreadPool.execute(()-> {
            for (int i = 0; i < 20; i++) {
                proConTest.produce("" + i);
            }
        });

        fixedThreadPool.execute(() -> {
            for (int i = 0; i < 20; i++) {
                proConTest.consume();
            }
        });
        fixedThreadPool.shutdown();
    }
}
