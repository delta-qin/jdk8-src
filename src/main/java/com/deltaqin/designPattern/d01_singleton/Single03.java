package com.deltaqin.designPattern.d01_singleton;

/**
 * JVM 的双亲委派保证类只加载一次，保证懒加载内部子类
 *
 * 静态内部类
 * @author deltaqin
 * @date 2021/3/26 2:10 下午
 */
public class Single03 {

    // 子类不会在父类初始化的时候就初始化，而是在调用属性的时候才会
    private static class Single {
        // static类保证只加载一次
        // 内部类保证了只在调用的时候才会初始化，懒加载
        public static final Single03 instance = new Single03();
    }

    public static Single03 getInstance() {
        return Single.instance;
    }

    public static void main(String[] args) {
        for (int i = 0; i < 100; i++) {
            new Thread(()-> {
                System.out.println(Single03.getInstance().hashCode());
            }).start();
        }
    }
}
