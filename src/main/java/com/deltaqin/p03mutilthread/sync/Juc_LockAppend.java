package com.deltaqin.p03mutilthread.sync;


public class Juc_LockAppend {
    StringBuffer stb = new StringBuffer();

    Object object = new Object();

    //锁的消除
    private void method1(){
        Object object1 = new Object();

        synchronized (object1){
            //
            //sdf
            //asdf
            System.out.println();
        }

    }

    private void method(){
        /*stb.append("杨过");
        stb.append("小龙女");
        stb.append("大雕");
        stb.append("郭靖");*/

        synchronized (object){
            System.out.println("");

            System.out.println("");

            System.out.println("");
        }

        /*synchronized (object){
            System.out.println("");
        }

        synchronized (object){
            System.out.println("");
        }*/

    }
}
