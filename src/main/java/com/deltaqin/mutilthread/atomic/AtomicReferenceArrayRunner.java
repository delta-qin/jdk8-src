package com.deltaqin.mutilthread.atomic;

import java.util.concurrent.atomic.AtomicReferenceArray;

// 自定义类型的数组，复制出来修改
public class AtomicReferenceArrayRunner {

    static Tuling[] ovalue = new Tuling[]{new Tuling(1),new Tuling(2)};
    static AtomicReferenceArray<Tuling> objarray = new AtomicReferenceArray(ovalue);

    public static void main(String[] args) {
        System.out.println(objarray.get(0).getSequence());

        objarray.set(0,new Tuling(3));

        System.out.println(objarray.get(0).getSequence());

    }

}
