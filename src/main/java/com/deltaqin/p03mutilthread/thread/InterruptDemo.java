package com.deltaqin.p03mutilthread.thread;

/**
 * @author deltaqin
 * @date 2021/10/3 下午8:36
 */
public class InterruptDemo {
    public static void main(String[] args) throws InterruptedException {
        Runnable runnable = () -> {

            int num = 0;
            while (!Thread.currentThread().isInterrupted()
                    && num <= 1000) {
                try {

                    System.out.println(num);
                    num++;
                    Thread.sleep(1000);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                    // 手动添加中断信号
                    //Thread.currentThread().interrupt();
                }
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();

        Thread.sleep(5);

        thread.interrupt();
    }
}
