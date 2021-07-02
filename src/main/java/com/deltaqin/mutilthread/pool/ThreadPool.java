package com.deltaqin.mutilthread.pool;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadPool {

    public static void main(String[] args) {

        /*for (int i=0;i<300;i++){
            new Thread(new Runnable() {
                    @Override
                    public void run() {
                        while(true){
                            try {
                                Thread.sleep(100);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
            }).start();
        }*/
        //Executors.newCachedThreadPool();

        ThreadPoolExecutor threadPoolExecutor =
                new ThreadPoolExecutor(1, 1,
                        5000, TimeUnit.MILLISECONDS,
                        new ArrayBlockingQueue<Runnable>(5));

        for (int i=0;i<6;i++){
            threadPoolExecutor.submit(new Runnable() {
                @Override
                public void run() {
                    System.out.println("i m task ："+Thread.currentThread().getName());
                }
            },i);
        }

        threadPoolExecutor.shutdown();  //running->shutdown
        threadPoolExecutor.shutdownNow(); //running->stop

    }


}
