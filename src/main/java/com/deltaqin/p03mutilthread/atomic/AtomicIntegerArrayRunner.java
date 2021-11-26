package com.deltaqin.p03mutilthread.atomic;

import java.util.concurrent.atomic.AtomicIntegerArray;

// 数组是拷贝出来修改的，不会修改原来的
public class AtomicIntegerArrayRunner {

    static int[] value = new int[]{1,2};
    static AtomicIntegerArray aiArray = new AtomicIntegerArray(value);

    public static void main(String[] args) {
        //todo 原子修改数组下标0的数值
        aiArray.getAndSet(0,3);
        System.out.println(aiArray.get(0));
        System.out.println(value[0]);
}

}
