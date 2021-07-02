package com.deltaqin.designPattern.d02_strategy;

import java.util.Arrays;

/**
 * @author deltaqin
 * @date 2021/3/26 2:39 下午
 */
public class Main {
    public static void main(String[] args) {
        // 不使用策略模式
        // 在Cat 或者 Dog 里面写死比较方法，也就是只可以有一种比较方法
        Cat[] cats = new Cat[] {
                new Cat(12,12),
                new Cat(11,11),
                new Cat(14,14)
        };

        //Sorter sorter = new Sorter();
        //sorter.sort(cats);
        //System.out.println(Arrays.toString(cats));


        // 使用策略模式
        // 在使用的时候定义比较的策略，按照固定的要求结果写自己的比较策略
        Sorter<Cat> sorter1 = new Sorter();
        sorter1.sort(cats, (o1, o2)-> {
            if (o1.height < o2.height)
                return -1;
            else if (o1.height > o2.height)
                return 1;
            else
                return 0;
        });
        System.out.println(Arrays.toString(cats));
    }
}
