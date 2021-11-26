package com.deltaqin.p03mutilthread.sync;

public class Juc_LockOnClass {
    static int stock;

    // 两个方法只能执行一个
    public static synchronized void decrStock(){
        System.out.println(--stock);
    }

    // 项目里面不要写sout
    // 是一个同步 的而且是单例的，加在同一个print，并发上不去，这里有影响
    public static synchronized void cgg(){
        System.out.println();
    }

    public static void main(String[] args) {
        //Juc_LockOnClass.class对象
        Juc_LockOnClass.decrStock();
    }

}
