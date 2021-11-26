package com.deltaqin.p01collection.map;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @description:
 * @author: 图灵学院-杨过
 * QQ：692927914
 * @date: 2019-04-14 21:28
 */
public class MultiThread implements Runnable {
    private static Map<Integer,Integer> map = new ConcurrentHashMap<>(11);

    private static AtomicInteger atomicInteger = new AtomicInteger();

    public void run() {
        while(atomicInteger.get() < 1000000){
            map.put(atomicInteger.get(),atomicInteger.get());
            atomicInteger.incrementAndGet();
        }
    }
}
