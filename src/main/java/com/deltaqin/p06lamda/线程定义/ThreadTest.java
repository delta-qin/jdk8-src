package com.deltaqin.p06lamda.线程定义;

import org.junit.Test;
import org.springframework.ui.context.Theme;

/**
 * @author deltaqin
 * @date 2021/11/27 8:04 下午
 */
public class ThreadTest {
    @Test
    public void test1(){
        Worker worker = new Worker();
        new Thread(worker).start();
    }

    class Worker implements Runnable {
        @Override
        public void run() {
            for (int i = 0; i < 100; i++) {
                dowork();
            }
        }


    }

    public void dowork() {
        System.out.println("do do do");
    }

    @Test
    public void test() {
        new Thread(() -> {
            for (int i = 0; i < 100; i++) {
                dowork();
            }
        }).start();
    }
}
