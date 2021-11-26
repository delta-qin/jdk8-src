package com.deltaqin.p07visualVM;

/**
 * @author deltaqin
 * @date 2021/5/30 10:17 下午
 */

// 主要是说明 准备和初始化的不同，，，，static准备是有默认值的，初始化才会赋值程序员显式设置的值
public class TestSingleton {
    public static void main(String[] args) {

        Singleton singleton = Singleton.getSingleton();

        System.out.println(singleton.count);
        System.out.println(singleton.count2);  // 0

    }
}


class Singleton {

    //public static  int count;
    public static  int count = 1;

    public static Singleton singleton = new Singleton();

    // 上面的导致下面的执行
    private Singleton() {
        count++;  // 执行到这里，因为是从上往下执行的，所以已经实现了准备阶段 + 初始化
        count2++;  // 准备阶段是0
    }

    // 下面的覆盖上面的值
    public static  int count2 = 0;

    public  static Singleton getSingleton() {
        return singleton;
    }

}
