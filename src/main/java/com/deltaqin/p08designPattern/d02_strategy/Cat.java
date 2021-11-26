package com.deltaqin.p08designPattern.d02_strategy;

import lombok.Data;

/**
 * @author deltaqin
 * @date 2021/3/26 2:39 下午
 */
@Data
public class Cat implements Comparable<Cat>{
    int height;
    int weight;

    public Cat(int height, int weight) {
        this.weight = weight;
        this.height = height;
    }

    public int compareTo(Cat o) {
        if (height < o.height)
            return -1;
        else if (height > o.height)
            return 1;
        else
            return 0;
    }



}
