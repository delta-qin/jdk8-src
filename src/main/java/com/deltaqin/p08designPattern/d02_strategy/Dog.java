package com.deltaqin.p08designPattern.d02_strategy;

import lombok.Data;

/**
 * @author deltaqin
 * @date 2021/3/26 3:42 下午
 */
@Data
public class Dog  implements Comparable<Dog>{

    int age;
    int weight;

    public int compareTo(Dog o) {
        if (age < o.age)
            return -1;
        else if(age > o.age)
            return 1;
        else
            return 0;
    }
}
