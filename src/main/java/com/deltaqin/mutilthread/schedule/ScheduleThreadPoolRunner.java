package com.deltaqin.mutilthread.schedule;

import lombok.extern.slf4j.Slf4j;
import lombok.var;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Slf4j
public class ScheduleThreadPoolRunner {

    public static void main(String[] args) {
        ScheduledThreadPoolExecutor scheduledThreadPoolExecutor =
                new ScheduledThreadPoolExecutor(1);

        // 1. 非周期性
        //ScheduledFuture<Integer> future = scheduledThreadPoolExecutor.schedule(() -> {
        //    System.out.println("我要延迟1s执行");
        //    return 1;
        //}, 1000, TimeUnit.MILLISECONDS);

        //提交任务的线程可以接着干活
        //try {
        //    // 在这阻塞等待
        //    System.out.println(future.get());
        //} catch (InterruptedException e) {
        //    e.printStackTrace();
        //} catch (ExecutionException e) {
        //    e.printStackTrace();
        //}


        // 2. 周期性执行任务(          任务堆积。。。。
        // 如果任务执行的时间比较长，本身任务已经超过了延迟周期，但是初始的时候只给了一个线程，
        // 不会再开一个线程执行现在到达的任务，就有问题了，并不是按照预期的执行，变成了任务堆积。插入但是没有执行
        //scheduledThreadPoolExecutor.scheduleAtFixedRate(() -> {
        //    log.info("启动后1秒延迟，之后每隔2秒执行");
        //    try {
        //        Thread.sleep(1000);
        //    } catch (InterruptedException e) {
        //        e.printStackTrace();
        //    }
        //}, 1000, 2000, TimeUnit.MILLISECONDS);


        // 3. 周期性执行任务（         任务不堆积。。。。）
        // 3+2
        //scheduledThreadPoolExecutor.scheduleWithFixedDelay(() -> {
        //    log.info("启动后1秒延迟，之后每隔2秒执行,并且只有任务结束之后才会开始执行新的");
        //    try {
        //        Thread.sleep(3000);
        //    } catch (InterruptedException e) {
        //        e.printStackTrace();
        //    }
        //}, 1000, 2000, TimeUnit.MILLISECONDS);


        // 4. 任务结束抛出异常的时候，这个任务就执行结束了，
        // 但是线程不会挂，还是会给另外的任务用，也就是第二个延迟任务会开始执行
        // 这个是会阻塞后面的执行的，不是异步回调的
        //scheduledThreadPoolExecutor.scheduleWithFixedDelay(() -> {
        //    log.info("send heart beat");
        //    long starttime = System.currentTimeMillis(), nowtime = starttime;
        //    while ((nowtime - starttime) < 2000) {
        //        nowtime = System.currentTimeMillis();
        //        try {
        //            Thread.sleep(1000);
        //        } catch (InterruptedException e) {
        //            e.printStackTrace();
        //        }
        //    }
        //    log.info("task over....");
        //    throw new RuntimeException("unexpected error , stop working");
        //}, 1000, 2000, TimeUnit.MILLISECONDS);
        //
        //
        //
        //scheduledThreadPoolExecutor.scheduleAtFixedRate(() -> {
        //    log.info("send heart beat222");
        //    long starttime = System.currentTimeMillis(), nowtime = starttime;
        //    while ((nowtime - starttime) < 2000) {
        //        nowtime = System.currentTimeMillis();
        //        try {
        //            Thread.sleep(1000);
        //        } catch (InterruptedException e) {
        //            e.printStackTrace();
        //        }
        //    }
        //    log.info("task over....");
        //    //throw new RuntimeException("unexpected error , stop working");
        //}, 1000, 2000, TimeUnit.MILLISECONDS);




        // 5. 定时类（不推荐，有异常线程直接挂），单线程的，后面的任务就无法执行了
        // 服务必须重启，上面的线程池不用重启
        //
        //Timer timer = new Timer();
        //timer.scheduleAtFixedRate(new TimerTask() {
        //    @Override
        //    public void run() {
        //        log.info("send heart beat");
        //        // 有异常线程会挂。后面的就不会执行了。因为没有线程了，
        //        throw new RuntimeException("unexpected error , stop working");
        //    }
        //},1000,2000);
        //
        //try {
        //    Thread.sleep(5000);
        //} catch (InterruptedException e) {
        //    e.printStackTrace();
        //}
        //
        //timer.scheduleAtFixedRate(new TimerTask() {
        //    @Override
        //    public void run() {
        //        log.info("send heart beat");
        //        throw new RuntimeException("unexpected error , stop working");
        //    }
        //},1000,2000);


    }
}
