package com.deltaqin.p01collection.set;

import java.util.Random;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @author ：杨过
 * @date ：Created in 2020/8/23
 * @version: V1.0
 * @slogan: 天下风云出我辈，一入代码岁月催
 * @description:
 **/
public class ConcurrentHashSetRunner {

    public static void main(String[] args) {
        CopyOnWriteArraySet<Integer> copyOnWriteArraySet = new CopyOnWriteArraySet();
        Integer nextInt = new Random().nextInt();
        copyOnWriteArraySet.add(nextInt);

        System.out.println(copyOnWriteArraySet.contains(nextInt));
    }

}
