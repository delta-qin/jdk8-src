package com.deltaqin.p07jvm;

import java.util.ArrayList;

/**
 * @author deltaqin
 * @date 2021/4/25 3:49 下午
 */
public class HeapTest {

    byte[] a = new byte[1024 * 100]; // 100 KB

    public static void main(String[] args) throws InterruptedException {
        ArrayList<HeapTest> objects = new ArrayList<>();
        while (true) {
            objects.add(new HeapTest());
            Thread.sleep(1);
        }
    }

}
