package com.deltaqin.p03mutilthread.jmm;

import lombok.extern.slf4j.Slf4j;

/**
 * @description:
 *
 * -server -Xcomp -XX:+UnlockDiagnosticVMOptions -XX:+PrintAssembly -XX:CompileCommand=compileonly,*Jmm03_CodeVisibility.refresh
 *      -Djava.compiler=NONE
 **/
@Slf4j
public class Jmm03_CodeVisibility {

    // 为了保证这个的可见性
    private static boolean initFlag = false;
    // 保证可见性，A就可以看到B 的修改了
    //private volatile static boolean initFlag = false;

    // 这个加上volatile，即使前面的不加，因为缓存行整体换出，导致initFlag也会去主存里面去取
    //private volatile static int counter = 0;

    // 这个也是因为缓存行替换，导致initFlag换出
    //private  static Integer counter = 0;

    // 两个都不是volatile，那就没办法了
    private  static int counter = 0;

    public static void refresh(){
        log.info("refresh data.......");
        initFlag = true;
        log.info("refresh data success.......");
    }

    public static void main(String[] args){
        Thread threadA = new Thread(()->{
            while (!initFlag){
                // 使用Integer可以不用volatile
                // 使用int需要使用volatile
                counter++;
            }
            log.info("线程：" + Thread.currentThread().getName()
                    + "当前线程嗅探到initFlag的状态的改变");
        },"threadA");
        threadA.start();

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Thread threadB = new Thread(()->{
            refresh();
        },"threadB");
        threadB.start();
    }
}
