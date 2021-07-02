package com.deltaqin.jvm;

import java.util.ArrayList;

/**
 * @author deltaqin
 * @date 2021/5/11 11:57 下午
 */
public class MATDemo {
    public static void main(String[] args) throws InterruptedException {
        ArrayList<Data> data = new ArrayList<>();

        for (int i = 0; i < 10000; i++) {
            data.add(new Data());
        }

        Thread.sleep(1 * 60 * 60 * 1000);
    }

    static  class Data {

    }
}
