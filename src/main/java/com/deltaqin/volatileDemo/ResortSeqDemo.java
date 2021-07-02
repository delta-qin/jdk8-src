package com.deltaqin.volatileDemo;

public class ResortSeqDemo {

    int a=0;
    boolean flag=false;
    /*
    多线程下flag=true可能先执行，还没走到a=1就被挂起。
    其它线程进入method02的判断，修改a的值=5，而不是6。
     */
    public void method01(){
        a=1;
        flag=true;
    }
    public void method02(){
        if (flag){
            a+=5;
            System.out.println("*****最终值a: "+a);
        }
    }

    public static void main(String[] args) {
        ResortSeqDemo resortSeq = new ResortSeqDemo();

        for (int i = 0; i < 100; i++) {
            new Thread(()->{resortSeq.method01();},"ThreadA").start();
            new Thread(()->{resortSeq.method02();},"ThreadB").start();
        }
    }
}
