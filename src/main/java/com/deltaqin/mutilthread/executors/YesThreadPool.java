package com.deltaqin.mutilthread.executors;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.IntStream;

@Slf4j
public class YesThreadPool {

    BlockingQueue<Runnable> taskQueue;  //存放任务的阻塞队列
    List<YesThread> threads; //线程列表

    YesThreadPool(BlockingQueue<Runnable> taskQueue, int threadSize) {
        this.taskQueue = taskQueue;
        threads = new ArrayList<>(threadSize);
        // 初始化线程，并定义名称
        IntStream.rangeClosed(1, threadSize).forEach((i) -> {
            YesThread thread = new YesThread("yes-task-thread-" + i);
            thread.start();
            threads.add(thread);
        });
    }

    //提交任务只是往任务队列里面塞任务
    public void execute(Runnable task) throws InterruptedException {
        taskQueue.put(task);
    }

    class YesThread extends Thread { //自定义一个线程
        public YesThread(String name) {
            super(name);
        }

        @Override
        public void run() {
            while (true) { //死循环
                Runnable task = null;
                try {
                    task = taskQueue.take(); //不断从任务队列获取任务
                } catch (InterruptedException e) {
                    log.error("记录点东西.....", e);
                }
                task.run(); //执行
            }
        }
    }

    public static void main(String[] args) {
        YesThreadPool pool = new YesThreadPool(new LinkedBlockingQueue<>(10), 3);
        IntStream.rangeClosed(1, 5).forEach((i) -> {
            try {
                pool.execute(() -> {
                    System.out.println(Thread.currentThread().getName() + " 公众号：yes的练级攻略");
                });
            } catch (InterruptedException e) {
                log.error("记录点东西.....", e);
            }
        });
    }
}
