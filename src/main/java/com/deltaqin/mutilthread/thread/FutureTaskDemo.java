package com.deltaqin.mutilthread.thread;

import java.util.concurrent.LinkedBlockingQueue;
import	java.util.concurrent.ThreadPoolExecutor;
import	java.util.concurrent.Callable;

import org.junit.Test;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FutureTaskDemo {

  @Test
  public void initCallable() throws ExecutionException, InterruptedException {
    FutureTask futureTask = new FutureTask(new Callable<String> () {
      @Override
      public String call() throws Exception {
        Thread.sleep(3000);
        return "nihao";
      }
    });
    futureTask.run();
    log.info("get ing");
    String  result = (String) futureTask.get();
    log.info("拿到值了，为 {}",result);
  }

  @Test
  public void initRunnable() throws ExecutionException, InterruptedException {
    FutureTask<String> futureTask = new FutureTask<String>(new Runnable() {
      @Override
      public void run() {
        log.info("{} is run",Thread.currentThread().getName());
      }
    }, null);
    futureTask.run();
    String result = futureTask.get();
    log.info("run end,result is {}",result);
  }

  @Test
  public void testGet() throws ExecutionException, InterruptedException {
    // FutureTask 将会提交到线程池
    ThreadPoolExecutor executor = new ThreadPoolExecutor(3, 3, 0L, TimeUnit.MILLISECONDS,
                                                         new LinkedBlockingQueue<>());
    // futureTask 我们叫做线程任务，构造器的入参是 Callable
    FutureTask futureTask = new FutureTask(new Callable<String> () {
      @Override
      public String call() throws Exception {
        Thread.sleep(3000);
        return "我是子线程"+Thread.currentThread().getName();
      }
    });
    // 把任务提交到线程池中，线程池会分配线程帮我们执行任务
    executor.submit(futureTask);
    // 得到任务执行的结果
    String result = (String) futureTask.get();
    log.info("result is {}",result);
  }

  @Test
  public void testThreadByCallable() throws ExecutionException, InterruptedException {
    FutureTask futureTask = new FutureTask(new Callable<String> () {
      @Override
      public String call() throws Exception {
        Thread.sleep(3000);
        String result = "我是子线程"+Thread.currentThread().getName();
        log.info("子线程正在运行：{}",Thread.currentThread().getName());
        return result;
      }
    });
    new Thread(futureTask).start();
    log.info("返回的结果是 {}",futureTask.get());
  }

}
