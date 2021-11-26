package com.deltaqin.p08designPattern.d01_singleton;

/**
 * @author deltaqin
 * @date 2021/3/26 1:39 下午
 */
public class Single01 {

    private static final Single01 INSTANCE = new Single01();

    public static Single01 getInstance() {
        return INSTANCE;
    }

    public void m() {
        System.out.println("hi");
    }


    public static void main(String[] args) {
        for (int i = 0; i < 100; i++) {
            new Thread(()->{
                System.out.println(getInstance().hashCode());
            }).start();
        }
    }
}
