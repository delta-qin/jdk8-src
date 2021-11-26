package com.deltaqin.p03mutilthread.lockaqs;

import lombok.extern.slf4j.Slf4j;


@Slf4j
public class Juc01_Thread_LockSupport {

    public static void main(String[] args) {

        Thread t0 = new Thread(new Runnable() {
            @Override
            public void run() {
                Thread current = Thread.currentThread();
                log.info("{},开始执行!",current.getName());
                for(;;){//spin 自旋
                    log.info("准备park住当前线程：{}....",current.getName());
                    // park 如果设置了中断是不会阻塞的，所以下面interrupted会想中断的标志删除
                    // 下次进来的时候park就可以阻塞了
                    //LockSupport.park();
                    if (Thread.interrupted()) {
                        // 自己处理中断逻辑，在AQS里面设置了中断的状态
                        return;
                    }
                    System.out.println(Thread.interrupted());
                    log.info("当前线程{}已经被唤醒....",current.getName());

                }
            }
        },"t0");

        t0.start();

        try {
            Thread.sleep(2000);
            log.info("准备唤醒{}线程!",t0.getName());
            //LockSupport.unpark(t0);
            Thread.sleep(2000);
            t0.interrupt();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
